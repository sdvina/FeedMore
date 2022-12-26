package org.sdvina.feedmore.util.work

import android.content.Context
import androidx.work.*
import org.sdvina.feedmore.data.AppRepository
import org.sdvina.feedmore.data.local.AppPreferences
import org.sdvina.feedmore.data.local.database.entity.Entry
import org.sdvina.feedmore.data.local.database.entity.FeedWithEntries
import org.sdvina.feedmore.data.model.entry.EntryToggleable
import org.sdvina.feedmore.data.remote.FeedFetcher
import java.util.concurrent.TimeUnit

open class BackgroundSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    val repo = AppRepository.get()
    private val fetcher = FeedFetcher()
    
    override suspend fun doWork(): Result {
        val feedUrls = repo.getFeedUrlsSynchronously()
        if (feedUrls.isEmpty()) return Result.success()

        for (url in feedUrls) {
            val storedEntries = repo.getEntriesToggleableByFeedSynchronously(url)
            val storedEntryIds: List<String> = storedEntries.map { it.url }
            val feedWithEntries: FeedWithEntries? = fetcher.requestSynchronously(url)

            feedWithEntries?.let { fwe ->
                val newEntries = fwe.entries.filterNot { storedEntryIds.contains(it.url) }
                handleRetrievedData(fwe, storedEntries, newEntries)
            }
        }

        return Result.success()
    }

    fun handleRetrievedData(
        fwe: FeedWithEntries,
        storedEntries: List<EntryToggleable>,
        newEntries: List<Entry>
    ) {
        val entryIds = fwe.entries.map { it.url }
        val oldEntries = storedEntries.filterNot { entryIds.contains(it.url) }
        val entriesToDelete = when (AppPreferences.shouldKeepOldUnreadEntries) {
            true -> oldEntries.filter { !it.isFavorite && it.isRead }
            false -> oldEntries.filter { !it.isFavorite }
        }

        repo.handleBackgroundUpdate(fwe.feed.url, newEntries, entriesToDelete, fwe.feed.imageUrl)
    }

    companion object {

        private const val WORK_NAME = "org.sdvina.feedmore.utils.work.BackgroundSyncWorker"

        private val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()

        fun start(context: Context) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                PeriodicWorkRequest
                    .Builder(BackgroundSyncWorker::class.java, 24, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .build()
            )
        }

        fun runOnce(context: Context) {
            WorkManager.getInstance(context).enqueue(OneTimeWorkRequest
                .Builder(BackgroundSyncWorker::class.java)
                .setConstraints(constraints)
                .build())
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}