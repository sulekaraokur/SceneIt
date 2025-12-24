package com.duyguabbasoglu.sceneit.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Background task
        Log.d("SceneItWorker", "Background sync executed")

        // Burada ileride:
        // - favori diziler kontrol edilir
        // - izleme ilerlemesi sync edilir

        return Result.success()
    }
}
