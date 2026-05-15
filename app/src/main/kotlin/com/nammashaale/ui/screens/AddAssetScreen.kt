package com.nammashaale.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nammashaale.data.local.AssetEntity
import com.nammashaale.viewmodel.AssetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(
    onNavigateBack: () -> Unit,
    viewModel: AssetViewModel = viewModel()
) {
    var assetName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Working") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Asset") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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
            OutlinedTextField(
                value = assetName,
                onValueChange = { assetName = it },
                label = { Text("Asset Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = serialNumber,
                onValueChange = { serialNumber = it },
                label = { Text("Serial Number") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location / Room") },
                modifier = Modifier.fillMaxWidth()
            )

            // Simplistic drop-down alternative (Segmented Button or ExposedDropdown)
            // For now, using text field for simplicity, ideally ExposedDropdownMenu
            OutlinedTextField(
                value = condition,
                onValueChange = { condition = it },
                label = { Text("Condition (Working, Needs Repair, Broken, Missing)") },
                modifier = Modifier.fillMaxWidth()
            )

            var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }
            val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.GetContent()
            ) { uri: android.net.Uri? ->
                imageUri = uri
            }

            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (imageUri == null) "Capture / Select Photo" else "Photo Selected ✅")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val newAsset = AssetEntity(
                        assetName = assetName,
                        category = category,
                        serialNumber = serialNumber,
                        purchaseDate = System.currentTimeMillis(),
                        location = location,
                        condition = condition,
                        imagePath = imageUri?.toString()
                    )
                    viewModel.insertAsset(newAsset)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = assetName.isNotBlank() && category.isNotBlank()
            ) {
                Text("Save Asset")
            }
        }
    }
}
