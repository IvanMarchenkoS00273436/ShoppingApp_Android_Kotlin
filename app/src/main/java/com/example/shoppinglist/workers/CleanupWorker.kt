package com.example.shoppinglist.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shoppinglist.data.dbcontext.AppDatabase
import java.util.concurrent.TimeUnit

class CleanupWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val db = AppDatabase.getDatabase(applicationContext)
            // Calculate time: Current Time - 30 Days (in milliseconds)
            val thirtyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)

            db.productDao().deleteOldProducts(thirtyDaysAgo)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}