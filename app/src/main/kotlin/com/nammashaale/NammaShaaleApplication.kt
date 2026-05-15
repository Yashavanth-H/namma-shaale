package com.nammashaale

import android.app.Application
import com.nammashaale.data.local.AppDatabase
import com.nammashaale.data.repository.InventoryRepository

class NammaShaaleApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { 
        InventoryRepository(
            database.assetDao(),
            database.auditDao(),
            database.repairDao()
        ) 
    }
}
