package com.nammashaale.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashaale.viewmodel.AssetViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(viewModel: AssetViewModel = viewModel()) {
    val assets by viewModel.allAssets.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isGenerating by remember { mutableStateOf(false) }

    val totalAssets = assets.size
    val working = assets.count { it.condition.equals("Working", ignoreCase = true) }
    val needsRepair = assets.count { it.condition.equals("Needs Repair", ignoreCase = true) }
    val damaged = assets.count { it.condition.equals("Broken", ignoreCase = true) }
    val missing = assets.count { it.condition.equals("Missing", ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Section
            Text("Inventory Summary", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryMiniCard("Total", totalAssets.toString(), MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                SummaryMiniCard("Working", working.toString(), MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryMiniCard("Needs Repair", needsRepair.toString(), MaterialTheme.colorScheme.error.copy(alpha = 0.7f), Modifier.weight(1f))
                SummaryMiniCard("Missing", missing.toString(), MaterialTheme.colorScheme.error, Modifier.weight(1f))
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Export Buttons
            Text("Export Reports", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

            ReportButton(
                title = "Export Full Inventory (CSV)",
                subtitle = "$totalAssets assets • All categories",
                icon = Icons.Default.FileDownload,
                isLoading = isGenerating,
                onClick = {
                    scope.launch {
                        isGenerating = true
                        val file = generateCsvReport(context, assets.map {
                            listOf(it.assetName, it.category, it.serialNumber, it.location, it.condition)
                        })
                        isGenerating = false
                        if (file != null) {
                            shareFile(context, file)
                        } else {
                            Toast.makeText(context, "Failed to generate report", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

            ReportButton(
                title = "Export Damaged Assets (CSV)",
                subtitle = "${damaged + needsRepair} assets need attention",
                icon = Icons.Default.Description,
                isLoading = isGenerating,
                onClick = {
                    scope.launch {
                        isGenerating = true
                        val filtered = assets.filter {
                            it.condition.equals("Broken", ignoreCase = true) ||
                            it.condition.equals("Needs Repair", ignoreCase = true)
                        }.map {
                            listOf(it.assetName, it.category, it.serialNumber, it.location, it.condition)
                        }
                        val file = generateCsvReport(context, filtered, "damaged_assets")
                        isGenerating = false
                        if (file != null) {
                            shareFile(context, file)
                        } else {
                            Toast.makeText(context, "No damaged assets to export", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )

            ReportButton(
                title = "Share Summary",
                subtitle = "Send inventory summary via WhatsApp or Messages",
                icon = Icons.Default.Share,
                isLoading = false,
                onClick = {
                    val summary = buildString {
                        appendLine("📊 Namma Shaale Inventory Report")
                        appendLine("📅 ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())}")
                        appendLine("---")
                        appendLine("📦 Total Assets: $totalAssets")
                        appendLine("✅ Working: $working")
                        appendLine("🔧 Needs Repair: $needsRepair")
                        appendLine("❌ Broken: $damaged")
                        appendLine("🔍 Missing: $missing")
                    }
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, summary)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Report"))
                }
            )
        }
    }
}

@Composable
fun SummaryMiniCard(label: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = color))
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportButton(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isLoading: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = { if (!isLoading) onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        }
    }
}

suspend fun generateCsvReport(context: Context, rows: List<List<String>>, filename: String = "inventory"): File? {
    return withContext(Dispatchers.IO) {
        try {
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                ?: context.filesDir
            val date = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
            val file = File(dir, "${filename}_$date.csv")
            FileWriter(file).use { writer ->
                writer.appendLine("Asset Name,Category,Serial Number,Location,Condition")
                rows.forEach { row ->
                    writer.appendLine(row.joinToString(",") { "\"$it\"" })
                }
            }
            file
        } catch (e: Exception) {
            null
        }
    }
}

fun shareFile(context: Context, file: File) {
    try {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share CSV Report"))
    } catch (e: Exception) {
        Toast.makeText(context, "Report saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    }
}
