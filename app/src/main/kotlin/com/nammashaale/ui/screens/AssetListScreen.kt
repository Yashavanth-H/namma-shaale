package com.nammashaale.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.nammashaale.ui.components.StatusChip
import com.nammashaale.viewmodel.AssetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetListScreen(
    onNavigateToAddAsset: () -> Unit,
    onNavigateToAssetDetails: (Int) -> Unit,
    viewModel: AssetViewModel = viewModel()
) {
    val assets by viewModel.allAssets.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filterOptions = listOf("All", "Working", "Needs Repair", "Broken", "Missing")

    val filteredAssets = assets.filter { asset ->
        val matchesSearch = searchQuery.isBlank() ||
            asset.assetName.contains(searchQuery, ignoreCase = true) ||
            asset.serialNumber.contains(searchQuery, ignoreCase = true) ||
            asset.category.contains(searchQuery, ignoreCase = true) ||
            asset.location.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == "All" || asset.condition == selectedFilter
        matchesSearch && matchesFilter
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventory (${assets.size})") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddAsset,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Asset", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by name, ID, location...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (filteredAssets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Inventory2, contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            if (assets.isEmpty()) "No assets yet.\nTap + to add your first asset!"
                            else "No assets match your search.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredAssets, key = { it.assetId }) { asset ->
                        AssetListItem(asset = asset, onClick = { onNavigateToAssetDetails(asset.assetId) })
                    }
                }
            }
        }
    }
}

@Composable
fun AssetListItem(asset: AssetEntity, onClick: () -> Unit) {
    val conditionColor = when (asset.condition) {
        "Working" -> Color(0xFF4CAF50)
        "Needs Repair" -> Color(0xFFFF9800)
        "Broken" -> Color(0xFFF44336)
        "Missing" -> Color(0xFF9C27B0)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Category icon indicator
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = conditionColor.copy(alpha = 0.12f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (asset.category) {
                                "Electronics" -> Icons.Default.Computer
                                "Furniture" -> Icons.Default.Chair
                                "Sports" -> Icons.Default.SportsBasketball
                                "Library" -> Icons.Default.MenuBook
                                "Lab Equipment" -> Icons.Default.Science
                                else -> Icons.Default.Inventory2
                            },
                            contentDescription = null,
                            tint = conditionColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = asset.assetName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${asset.category} • ${asset.location}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            StatusChip(status = asset.condition)
        }
    }
}
