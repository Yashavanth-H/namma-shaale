package com.nammashaale.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class AuditReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Here we would typically send a local notification
        // reminding the user to perform the monthly audit.
        println("AuditReminderWorker: Time to perform the monthly asset audit!")
        
        return Result.success()
    }
}
