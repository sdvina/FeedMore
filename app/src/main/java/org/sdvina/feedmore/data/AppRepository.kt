package org.sdvina.feedmore.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import org.sdvina.feedmore.data.local.database.AppDatabase
import org.sdvina.feedmore.data.local.database.entity.FeedWithEntries
import org.sdvina.feedmore.data.local.database.entity.Entry
import org.sdvina.feedmore.data.model.entry.EntryToggleable
import org.sdvina.feedmore.data.local.database.entity.Feed
import org.sdvina.feedmore.data.model.cross.FeedTitleWithEntriesToggleable
import org.sdvina.feedmore.data.model.entry.EntryLite
import org.sdvina.feedmore.data.model.feed.FeedWithCategory
import org.sdvina.feedmore.data.model.feed.FeedLite
import org.sdvina.feedmore.data.model.feed.FeedManageable
import org.sdvina.feedmore.util.NetworkMonitor
import java.util.concurrent.Executors

class AppRepository private constructor(
    database: AppDatabase,
    val networkMonitor: NetworkMonitor
) {

    private val dao = database.combinedDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getFeed(feedUrl: String): LiveData<Feed?> = dao.getFeed(feedUrl)

    fun getFeedLites(): Flow<List<FeedLite>> = dao.getFeedLites()

    fun getFeedUrls(): LiveData<List<String>> = dao.getFeedUrls()

    fun getFeedUrlsWithCategories(): Flow<List<FeedWithCategory>> = dao.getFeedUrlsWithCategories()

    fun getFeedUrlsSynchronously(): List<String> = dao.getFeedUrlsSynchronously()

    fun getFeedTitleWithEntriesToggleableSynchronously(feedId: String): FeedTitleWithEntriesToggleable {
        return dao.getFeedTitleAndEntriesToggleableSynchronously(feedId)
    }

    fun getFeedsManageable(): Flow<List<FeedManageable>> = dao.getFeedsManageable()

    fun getEntry(entryUrl: String): LiveData<Entry?> = dao.getEntry(entryUrl)

    fun getPagedEntryLitesByFeed(feedUrl: String): PagingSource<Int, EntryLite> = dao.getPagedEntryLitesByFeed(feedUrl)

    fun getPagedNewEntryLites(): PagingSource<Int, EntryLite> = dao.getPagedNewEntryLites()

    fun getPagedFavoriteEntryLites(): PagingSource<Int, EntryLite> = dao.getPagedFavoriteEntryLites()

    fun getEntriesToggleableByFeedSynchronously(feedId: String): List<EntryToggleable> {
        return dao.getEntriesToggleableByFeedSynchronously(feedId)
    }

    fun addFeeds(vararg feed: Feed) {
        executor.execute { dao.addFeeds(*feed) }
    }

    fun addFeedWithEntries(feedWithEntries: FeedWithEntries) {
        executor.execute {
            dao.addFeedAndEntries(feedWithEntries.feed, feedWithEntries.entries)
        }
    }

    fun updateFeed(feed: Feed) {
        executor.execute { dao.updateFeed(feed) }
    }

    fun updateFeedTitleAndCategory(feedUrl: String, title: String, category: String) {
        executor.execute { dao.updateFeedTitleAndCategory(feedUrl, title, category) }
    }

    fun updateFeedCategory(vararg feedUrl: String, category: String) {
        executor.execute { dao.updateFeedCategory(*feedUrl, category = category) }
    }

    fun updateFeedUnreadCount(feedUrl: String, count: Int) {
        executor.execute { dao.updateFeedUnreadCount(feedUrl, count) }
    }

    fun updateEntryAndFeedUnreadCount(entryUrl: String, isRead: Boolean, isFavorite: Boolean) {
        executor.execute { dao.updateEntryAndFeedUnreadCount(entryUrl, isRead, isFavorite) }
    }

    fun updateEntryIsFavorite(vararg entryUrl: String, isFavorite: Boolean) {
        executor.execute { dao.updateEntryIsFavorite(*entryUrl, isFavorite = isFavorite) }
    }

    fun updateEntryIsRead(vararg entryUrl: String, isRead: Boolean) {
        executor.execute { dao.updateEntryIsReadAndFeedUnreadCount(*entryUrl, isRead = isRead) }
    }

    fun handleEntryUpdates(
        feedUrl: String,
        entriesToAdd: List<Entry>,
        entriesToUpdate: List<Entry>,
        entriesToDelete: List<Entry>,
    ) {
        executor.execute {
            dao.handleEntryUpdates(feedUrl, entriesToAdd, entriesToUpdate, entriesToDelete)
        }
    }

    fun handleBackgroundUpdate(
        feedUrl: String,
        newEntries: List<Entry>,
        oldEntries: List<EntryToggleable>,
        feedImage: String?,
    ) {
        executor.execute {
            dao.handleBackgroundUpdate(feedUrl, newEntries, oldEntries, feedImage)
        }
    }

    fun deleteFeedAndEntriesById(vararg feedUrl: String) {
        executor.execute { dao.deleteFeedAndEntriesById(*feedUrl) }
    }

    fun deleteLeftoverItems() {
        executor.execute { dao.deleteLeftoverItems() }
    }

    companion object {
        private var INSTANCE: AppRepository? = null

        fun init(database: AppDatabase, networkMonitor: NetworkMonitor) {
            if (INSTANCE == null) INSTANCE = AppRepository(database, networkMonitor)
        }

        fun get(): AppRepository {
            return INSTANCE ?: throw IllegalStateException("Repository must be initialized!")
        }
    }
}