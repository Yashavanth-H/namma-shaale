package com.nammashaale.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashaale.ui.components.DashboardCard
import com.nammashaale.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel()
) {
    val assets by viewModel.allAssets.collectAsState()

    val totalAssets = assets.size
    val workingAssets = assets.count { it.condition == "Working" }
    val needsRepair = assets.count { it.condition == "Needs Repair" }
    val missingAssets = assets.count { it.condition == "Missing" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
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
                .padding(16.dp)
        ) {
            Text(
                text = "Asset Summary",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    DashboardCard(
                        title = "Total Assets",
                        value = totalAssets.toString(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    DashboardCard(
                        title = "Working",
                        value = workingAssets.toString(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                item {
                    DashboardCard(
                        title = "Needs Repair",
                        value = needsRepair.toString(),
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
                item {
                    DashboardCard(
                        title = "Missing",
                        value = missingAssets.toString(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
