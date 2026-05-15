package com.nammashaale.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey(autoGenerate = true)
    val assetId: Int = 0,
    val assetName: String,
    val category: String,
    val serialNumber: String,
    val purchaseDate: Long,
    val location: String,
    val condition: String,
    val imagePath: String?
)

@Entity(tableName = "audits")
data class AuditEntity(
    @PrimaryKey(autoGenerate = true)
    val auditId: Int = 0,
    val assetId: Int,
    val status: String,
    val checkedDate: Long,
    val remarks: String?
)

@Entity(tableName = "repairs")
data class RepairEntity(
    @PrimaryKey(autoGenerate = true)
    val repairId: Int = 0,
    val assetId: Int,
    val issue: String,
    val status: String, // Pending, In Progress, Resolved
    val requestDate: Long
)
