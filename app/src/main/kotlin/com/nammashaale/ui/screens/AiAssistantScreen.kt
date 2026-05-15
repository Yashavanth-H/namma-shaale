package com.nammashaale.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashaale.ai.GeminiAiService
import com.nammashaale.data.local.AssetEntity
import com.nammashaale.viewmodel.AssetViewModel
import com.nammashaale.viewmodel.AuditViewModel
import com.nammashaale.viewmodel.RepairViewModel
import com.nammashaale.BuildConfig
import kotlinx.coroutines.launch

// 🔑 OpenRouter API key loaded from BuildConfig
private val GEMINI_API_KEY = BuildConfig.OPENROUTER_API_KEY

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(
    assetViewModel: AssetViewModel = viewModel(),
    auditViewModel: AuditViewModel = viewModel(),
    repairViewModel: RepairViewModel = viewModel()
) {
    val assets by assetViewModel.allAssets.collectAsState()
    val audits by auditViewModel.allAudits.collectAsState()
    val repairs by repairViewModel.allRepairs.collectAsState()

    val scope = rememberCoroutineScope()
    val aiService = remember { GeminiAiService(GEMINI_API_KEY) }

    var selectedAsset by remember { mutableStateOf<AssetEntity?>(null) }
    var aiResult by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var currentFeature by remember { mutableStateOf("") }

    val shimmerAlpha by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "alpha"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("🤖", fontSize = 20.sp)
                        Text("AI Assistant")
                    }
                },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header gradient card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Powered by Gemini AI", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Text("Smart maintenance predictions & audit reports", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        
                        Spacer(Modifier.height(8.dp))
                        
                        OutlinedButton(
                            onClick = {
                                currentFeature = "API Check"
                                isLoading = true
                                aiResult = ""
                                scope.launch {
                                    aiResult = aiService.testApiKey()
                                    isLoading = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            enabled = !isLoading
                        ) {
                            Icon(Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Verify AI Status", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Feature 1: Audit Summary
            AiFeatureCard(
                title = "📊 Generate Audit Summary",
                description = "AI will analyze your latest audit and generate a natural language summary for the principal.",
                icon = Icons.Default.Summarize,
                enabled = audits.isNotEmpty() && assets.isNotEmpty(),
                disabledReason = if (audits.isEmpty()) "Conduct an audit first to use this feature." else null,
                onClick = {
                    currentFeature = "Audit Summary"
                    isLoading = true
                    aiResult = ""
                    scope.launch {
                        val recentAudits = audits.take(20)
                        val auditMap = recentAudits.associate { it.assetId to it.status }
                        aiResult = aiService.generateAuditSummary(assets, auditMap)
                        isLoading = false
                    }
                }
            )

            // Feature 2: Maintenance Prediction
            Text("🔮 Maintenance Prediction", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Text("Select an asset to predict its next maintenance need:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            var dropdownExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedAsset?.assetName ?: "Select an asset...",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    assets.forEach { asset ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(asset.assetName, style = MaterialTheme.typography.bodyMedium)
                                    Text("${asset.category} • ${asset.condition}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                }
                            },
                            onClick = {
                                selectedAsset = asset
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val asset = selectedAsset ?: return@Button
                    currentFeature = "Maintenance Prediction"
                    isLoading = true
                    aiResult = ""
                    scope.launch {
                        val assetRepairs = repairs.filter { it.assetId == asset.assetId }
                        val assetAudits = audits.filter { it.assetId == asset.assetId }
                        aiResult = aiService.predictMaintenance(asset, assetRepairs, assetAudits)
                        isLoading = false
                    }
                },
                enabled = selectedAsset != null && !isLoading,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Predict Maintenance")
            }

            // Feature 3: Smart Repair Suggestion
            AiFeatureCard(
                title = "🔧 Smart Repair Insights",
                description = "AI analyzes your ${repairs.count { it.status == "Pending" }} pending repairs and suggests likely fixes.",
                icon = Icons.Default.Build,
                enabled = repairs.isNotEmpty(),
                disabledReason = if (repairs.isEmpty()) "Submit a repair request first to use this feature." else null,
                onClick = {
                    currentFeature = "Repair Insights"
                    isLoading = true
                    aiResult = ""
                    scope.launch {
                        val pendingRepairs = repairs.filter { it.status == "Pending" || it.status == "In Progress" }
                        if (pendingRepairs.isEmpty()) {
                            aiResult = "No pending repairs found."
                            isLoading = false
                            return@launch
                        }
                        val firstRepair = pendingRepairs.first()
                        val asset = assets.find { it.assetId == firstRepair.assetId }
                        aiResult = aiService.suggestRepairSolution(
                            assetName = asset?.assetName ?: "Unknown Asset",
                            category = asset?.category ?: "General",
                            issue = firstRepair.issue
                        )
                        isLoading = false
                    }
                }
            )

            // Results Display
            AnimatedVisibility(visible = isLoading || aiResult.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("🤖", fontSize = 18.sp)
                            Text(
                                if (isLoading) "Thinking..." else currentFeature,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            )
                        }
                        if (isLoading) {
                            repeat(4) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(if (it == 3) 0.6f else 1f)
                                        .height(12.dp)
                                        .alpha(shimmerAlpha)
                                        .background(
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                                            RoundedCornerShape(6.dp)
                                        )
                                )
                                Spacer(Modifier.height(4.dp))
                            }
                        } else {
                            Text(aiResult, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun AiFeatureCard(
    title: String,
    description: String,
    icon: ImageVector,
    enabled: Boolean,
    disabledReason: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled)
                MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(if (enabled) 2.dp else 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
                Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            }
            Text(
                disabledReason ?: description,
                style = MaterialTheme.typography.bodySmall,
                color = if (enabled)
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth().height(42.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Run AI Analysis", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
