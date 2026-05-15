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
import com.nammashaale.data.local.RepairEntity
import com.nammashaale.viewmodel.AssetViewModel
import com.nammashaale.viewmodel.RepairViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairScreen(
    repairViewModel: RepairViewModel = viewModel(),
    assetViewModel: AssetViewModel = viewModel()
) {
    val repairs by repairViewModel.allRepairs.collectAsState()
    val assets by assetViewModel.allAssets.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Pending", "In Progress", "Resolved")
    val statuses = listOf("Pending", "In Progress", "Resolved")

    val filteredRepairs = repairs.filter { it.status == statuses[selectedTab] }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repairs") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Summary row
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                statuses.forEachIndexed { idx, status ->
                    val count = repairs.count { it.status == status }
                    val color = when (status) {
                        "Pending" -> Color(0xFFFF9800)
                        "In Progress" -> MaterialTheme.colorScheme.primary
                        "Resolved" -> Color(0xFF4CAF50)
                        else -> Color.Gray
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        color = color.copy(alpha = 0.12f)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(count.toString(), style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = color))
                            Text(status, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                }
            }

            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            if (filteredRepairs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Build,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No ${tabs[selectedTab].lowercase()} repairs.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredRepairs, key = { it.repairId }) { repair ->
                        RepairCard(
                            repair = repair,
                            assetName = assets.find { it.assetId == repair.assetId }?.assetName ?: "Asset #${repair.assetId}",
                            onStatusChange = { newStatus ->
                                repairViewModel.updateRepairStatus(repair.repairId, newStatus)
                                if (newStatus == "Resolved") {
                                    val asset = assets.find { it.assetId == repair.assetId }
                                    if (asset != null) {
                                        assetViewModel.updateAsset(asset.copy(condition = "Working"))
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RepairCard(repair: RepairEntity, assetName: String, onStatusChange: (String) -> Unit) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val statusColor = when (repair.status) {
        "Pending" -> Color(0xFFFF9800)
        "In Progress" -> Color(0xFF2196F3)
        "Resolved" -> Color(0xFF4CAF50)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(assetName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(
                        dateFormat.format(Date(repair.requestDate)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        repair.status,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = statusColor, fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Issue description
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    repair.issue,
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Action buttons based on current status
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when (repair.status) {
                    "Pending" -> {
                        Button(
                            onClick = { onStatusChange("In Progress") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Start Repair", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    "In Progress" -> {
                        Button(
                            onClick = { onStatusChange("Resolved") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Mark Resolved", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
