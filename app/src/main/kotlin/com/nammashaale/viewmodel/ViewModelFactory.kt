package com.nammashaale.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nammashaale.data.repository.InventoryRepository

class AppViewModelFactory(
    private val repository: InventoryRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AssetViewModel::class.java) -> {
                AssetViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AuditViewModel::class.java) -> {
                AuditViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RepairViewModel::class.java) -> {
                RepairViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
