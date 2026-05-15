package com.nammashaale.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Query("SELECT * FROM assets ORDER BY assetName ASC")
    fun getAllAssets(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM assets WHERE assetId = :id")
    fun getAssetById(id: Int): Flow<AssetEntity?>

    @Query("SELECT * FROM assets WHERE category = :category ORDER BY assetName ASC")
    fun getAssetsByCategory(category: String): Flow<List<AssetEntity>>

    @Query("SELECT * FROM assets WHERE condition = :condition")
    fun getAssetsByCondition(condition: String): Flow<List<AssetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: AssetEntity): Long

    @Update
    suspend fun updateAsset(asset: AssetEntity)

    @Delete
    suspend fun deleteAsset(asset: AssetEntity)
}

@Dao
interface AuditDao {
    @Query("SELECT * FROM audits ORDER BY checkedDate DESC")
    fun getAllAudits(): Flow<List<AuditEntity>>

    @Query("SELECT * FROM audits WHERE assetId = :assetId ORDER BY checkedDate DESC")
    fun getAuditsForAsset(assetId: Int): Flow<List<AuditEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudit(audit: AuditEntity)
}

@Dao
interface RepairDao {
    @Query("SELECT * FROM repairs ORDER BY requestDate DESC")
    fun getAllRepairs(): Flow<List<RepairEntity>>

    @Query("SELECT * FROM repairs WHERE status = :status ORDER BY requestDate DESC")
    fun getRepairsByStatus(status: String): Flow<List<RepairEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepair(repair: RepairEntity)

    @Update
    suspend fun updateRepair(repair: RepairEntity)
}
