package com.nammashaale.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammashaale.data.local.AssetEntity
import com.nammashaale.data.local.AuditEntity
import com.nammashaale.data.local.RepairEntity
import com.nammashaale.data.repository.InventoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: InventoryRepository) : ViewModel() {
    val allAssets: StateFlow<List<AssetEntity>> = repository.getAllAssets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class AssetViewModel(private val repository: InventoryRepository) : ViewModel() {
    val allAssets: StateFlow<List<AssetEntity>> = repository.getAllAssets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertAsset(asset: AssetEntity) {
        viewModelScope.launch {
            repository.insertAsset(asset)
        }
    }

    fun updateAsset(asset: AssetEntity) {
        viewModelScope.launch { repository.updateAsset(asset) }
    }

    fun deleteAsset(asset: AssetEntity) {
        viewModelScope.launch { repository.deleteAsset(asset) }
    }
}

class AuditViewModel(private val repository: InventoryRepository) : ViewModel() {
    val allAudits: StateFlow<List<AuditEntity>> = repository.getAllAudits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertAudit(audit: AuditEntity) {
        viewModelScope.launch {
            repository.insertAudit(audit)
        }
    }
}

class RepairViewModel(private val repository: InventoryRepository) : ViewModel() {
    val allRepairs: StateFlow<List<RepairEntity>> = repository.getAllRepairs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertRepair(repair: RepairEntity) {
        viewModelScope.launch { repository.insertRepair(repair) }
    }

    fun updateRepairStatus(repairId: Int, newStatus: String) {
        viewModelScope.launch {
            val current = allRepairs.value.find { it.repairId == repairId }
            if (current != null) {
                repository.updateRepair(current.copy(status = newStatus))
            }
        }
    }
}
