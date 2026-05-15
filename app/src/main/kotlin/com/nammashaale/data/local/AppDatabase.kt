package com.nammashaale.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AssetEntity::class, AuditEntity::class, RepairEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun assetDao(): AssetDao
    abstract fun auditDao(): AuditDao
    abstract fun repairDao(): RepairDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_shaale_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
