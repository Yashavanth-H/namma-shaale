package com.nammashaale.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun AssetDetailsScreen(
    assetId: Int,
    onNavigateBack: () -> Unit,
    assetViewModel: AssetViewModel = viewModel(),
    repairViewModel: RepairViewModel = viewModel()
) {
    val assets by assetViewModel.allAssets.collectAsState()
    val asset = assets.find { it.assetId == assetId }

    var showRepairDialog by remember { mutableStateOf(false) }
    var repairIssue by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var snackbarMsg by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMsg) {
        if (snackbarMsg.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackbarMsg)
            snackbarMsg = ""
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(asset?.assetName ?: "Asset Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (asset != null) {
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (asset != null) {
                FloatingActionButton(
                    onClick = { showRepairDialog = true },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(Icons.Default.Build, contentDescription = "Request Repair")
                }
            }
        }
    ) { innerPadding ->
        if (asset == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Condition banner
                val conditionColor = when (asset.condition) {
                    "Working" -> Color(0xFF4CAF50)
                    "Needs Repair" -> Color(0xFFFF9800)
                    "Broken" -> MaterialTheme.colorScheme.error
                    "Missing" -> Color(0xFF9C27B0)
                    else -> MaterialTheme.colorScheme.secondary
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = conditionColor.copy(alpha = 0.12f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Circle, contentDescription = null, tint = conditionColor)
                        Text(
                            asset.condition,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = conditionColor)
                        )
                    }
                }

                // Details Card
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Asset Information", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                        DetailRow("Name", asset.assetName)
                        DetailRow("Category", asset.category)
                        DetailRow("Serial Number", if (asset.serialNumber.isNotBlank()) asset.serialNumber else "N/A")
                        DetailRow("Location", asset.location)
                        DetailRow("Purchase Date",
                            if (asset.purchaseDate > 0)
                                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(asset.purchaseDate))
                            else "Not specified"
                        )
                    }
                }

                // Action buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Quick condition change
                    val conditions = listOf("Working", "Needs Repair", "Broken", "Missing")
                    conditions.forEach { cond ->
                        if (cond != asset.condition) {
                            val btnColor = when (cond) {
                                "Working" -> Color(0xFF4CAF50)
                                "Needs Repair" -> Color(0xFFFF9800)
                                "Broken" -> MaterialTheme.colorScheme.error
                                else -> Color(0xFF9C27B0)
                            }
                            if (cond == "Working" || cond == asset.condition.let { "Needs Repair" }) {
                                OutlinedButton(
                                    onClick = {
                                        assetViewModel.updateAsset(asset.copy(condition = cond))
                                        snackbarMsg = "Condition updated to $cond"
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = btnColor)
                                ) {
                                    Text("Mark $cond", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Dialog
    if (showEditDialog && asset != null) {
        var editedName by remember { mutableStateOf(asset.assetName) }
        var editedLocation by remember { mutableStateOf(asset.location) }
        var editedSerial by remember { mutableStateOf(asset.serialNumber) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Asset") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Asset Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedLocation,
                        onValueChange = { editedLocation = it },
                        label = { Text("Location / Room") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editedSerial,
                        onValueChange = { editedSerial = it },
                        label = { Text("Serial Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (editedName.isNotBlank()) {
                        assetViewModel.updateAsset(
                            asset.copy(
                                assetName = editedName,
                                location = editedLocation,
                                serialNumber = editedSerial
                            )
                        )
                        showEditDialog = false
                        snackbarMsg = "Asset updated successfully!"
                    }
                }) { Text("Save Changes") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Repair Dialog
    if (showRepairDialog && asset != null) {
        AlertDialog(
            onDismissRequest = { showRepairDialog = false },
            title = { Text("Request Repair") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Describe the issue with ${asset.assetName}:")
                    OutlinedTextField(
                        value = repairIssue,
                        onValueChange = { repairIssue = it },
                        label = { Text("Issue Description") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (repairIssue.isNotBlank()) {
                        repairViewModel.insertRepair(
                            RepairEntity(
                                assetId = asset.assetId,
                                issue = repairIssue,
                                status = "Pending",
                                requestDate = System.currentTimeMillis()
                            )
                        )
                        assetViewModel.updateAsset(asset.copy(condition = "Needs Repair"))
                        repairIssue = ""
                        showRepairDialog = false
                        snackbarMsg = "Repair request submitted!"
                    }
                }) { Text("Submit") }
            },
            dismissButton = {
                TextButton(onClick = { showRepairDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium), modifier = Modifier.padding(start = 8.dp))
    }
    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}
