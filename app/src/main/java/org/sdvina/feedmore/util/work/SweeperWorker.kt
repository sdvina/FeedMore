package org.sdvina.feedmore.util.work

import android.content.Context
import androidx.work.*
import org.sdvina.feedmore.data.AppRepository
import java.util.concurrent.TimeUnit

class SweeperWorker(
    context: Context,
    workerParams: WorkerParameters
): Worker(context, workerParams) {

    private val repository = AppRepository.get()

    override fun doWork(): Result {
        repository.deleteLeftoverItems() // Just in case
        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "org.sdvina.feedmore.utils.work.SweeperWorker"

        fun start(context: Context) {
            val request = PeriodicWorkRequest.Builder(
                SweeperWorker::class.java, 3, TimeUnit.DAYS
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}