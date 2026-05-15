package com.nammashaale.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashaale.data.local.AssetEntity
import com.nammashaale.data.local.AuditEntity
import com.nammashaale.viewmodel.AssetViewModel
import com.nammashaale.viewmodel.AuditViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditScreen(
    assetViewModel: AssetViewModel = viewModel(),
    auditViewModel: AuditViewModel = viewModel()
) {
    val assets by assetViewModel.allAssets.collectAsState()
    val audits by auditViewModel.allAudits.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Start Audit", "History")

    // Audit state: assetId -> status chosen
    val auditSelections = remember { mutableStateMapOf<Int, String>() }
    var isAuditCompleted by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isAuditCompleted) {
        if (isAuditCompleted) {
            snackbarHostState.showSnackbar("Audit submitted successfully!")
            isAuditCompleted = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Audit") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> AuditWorkflowTab(
                    assets = assets,
                    auditSelections = auditSelections,
                    onSubmit = {
                        auditSelections.forEach { (assetId, status) ->
                            auditViewModel.insertAudit(
                                AuditEntity(
                                    assetId = assetId,
                                    status = status,
                                    checkedDate = System.currentTimeMillis(),
                                    remarks = null
                                )
                            )
                            val asset = assets.find { it.assetId == assetId }
                            if (asset != null) {
                                assetViewModel.updateAsset(asset.copy(condition = status))
                            }
                        }
                        auditSelections.clear()
                        isAuditCompleted = true
                    }
                )
                1 -> AuditHistoryTab(audits = audits, assets = assets)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditWorkflowTab(
    assets: List<AssetEntity>,
    auditSelections: MutableMap<Int, String>,
    onSubmit: () -> Unit
) {
    val conditionOptions = listOf("Working", "Needs Repair", "Broken", "Missing")

    if (assets.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No assets to audit yet.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val completedCount = auditSelections.size
        LinearProgressIndicator(
            progress = if (assets.isEmpty()) 0f else completedCount.toFloat() / assets.size,
            modifier = Modifier.fillMaxWidth().height(6.dp)
        )
        Text(
            "$completedCount / ${assets.size} assets checked",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            color = MaterialTheme.colorScheme.primary
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(assets, key = { it.assetId }) { asset ->
                val selectedCondition = auditSelections[asset.assetId]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedCondition != null)
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(asset.assetName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Text("${asset.category} • ${asset.location}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                            }
                            if (selectedCondition != null) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            conditionOptions.forEach { cond ->
                                val isSelected = selectedCondition == cond
                                val color = when (cond) {
                                    "Working" -> Color(0xFF4CAF50)
                                    "Needs Repair" -> Color(0xFFFF9800)
                                    "Broken" -> Color(0xFFF44336)
                                    "Missing" -> Color(0xFF9C27B0)
                                    else -> Color.Gray
                                }
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        if (isSelected) auditSelections.remove(asset.assetId)
                                        else auditSelections[asset.assetId] = cond
                                    },
                                    label = { Text(cond, style = MaterialTheme.typography.labelSmall) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = color.copy(alpha = 0.2f),
                                        selectedLabelColor = color
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Submit Button
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(52.dp),
            enabled = auditSelections.isNotEmpty(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Submit Audit (${auditSelections.size} items)", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AuditHistoryTab(audits: List<AuditEntity>, assets: List<AssetEntity>) {
    if (audits.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No audits conducted yet.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
        return
    }
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    // Group by date
    val grouped = audits.groupBy {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it.checkedDate))
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        grouped.forEach { (date, entries) ->
            item {
                Text(date, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary), modifier = Modifier.padding(vertical = 4.dp))
            }
            items(entries) { audit ->
                val assetName = assets.find { it.assetId == audit.assetId }?.assetName ?: "Asset #${audit.assetId}"
                val statusColor = when (audit.status) {
                    "Working" -> Color(0xFF4CAF50)
                    "Needs Repair" -> Color(0xFFFF9800)
                    "Broken" -> Color(0xFFF44336)
                    "Missing" -> Color(0xFF9C27B0)
                    else -> Color.Gray
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.08f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(assetName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text(dateFormat.format(Date(audit.checkedDate)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        }
                        Surface(shape = RoundedCornerShape(20.dp), color = statusColor.copy(alpha = 0.15f)) {
                            Text(audit.status, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall.copy(color = statusColor, fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}
