package com.nammashaale.data.repository

import com.nammashaale.data.local.AssetDao
import com.nammashaale.data.local.AssetEntity
import com.nammashaale.data.local.AuditDao
import com.nammashaale.data.local.AuditEntity
import com.nammashaale.data.local.RepairDao
import com.nammashaale.data.local.RepairEntity
import kotlinx.coroutines.flow.Flow

class InventoryRepository(
    private val assetDao: AssetDao,
    private val auditDao: AuditDao,
    private val repairDao: RepairDao
) {
    // Assets
    fun getAllAssets(): Flow<List<AssetEntity>> = assetDao.getAllAssets()
    
    fun getAssetById(id: Int): Flow<AssetEntity?> = assetDao.getAssetById(id)
    
    fun getAssetsByCategory(category: String): Flow<List<AssetEntity>> = assetDao.getAssetsByCategory(category)
    
    fun getAssetsByCondition(condition: String): Flow<List<AssetEntity>> = assetDao.getAssetsByCondition(condition)
    
    suspend fun insertAsset(asset: AssetEntity): Long = assetDao.insertAsset(asset)
    
    suspend fun updateAsset(asset: AssetEntity) = assetDao.updateAsset(asset)
    
    suspend fun deleteAsset(asset: AssetEntity) = assetDao.deleteAsset(asset)
    
    // Audits
    fun getAllAudits(): Flow<List<AuditEntity>> = auditDao.getAllAudits()
    
    fun getAuditsForAsset(assetId: Int): Flow<List<AuditEntity>> = auditDao.getAuditsForAsset(assetId)
    
    suspend fun insertAudit(audit: AuditEntity) = auditDao.insertAudit(audit)
    
    // Repairs
    fun getAllRepairs(): Flow<List<RepairEntity>> = repairDao.getAllRepairs()
    
    fun getRepairsByStatus(status: String): Flow<List<RepairEntity>> = repairDao.getRepairsByStatus(status)
    
    suspend fun insertRepair(repair: RepairEntity) = repairDao.insertRepair(repair)
    
    suspend fun updateRepair(repair: RepairEntity) = repairDao.updateRepair(repair)
}
