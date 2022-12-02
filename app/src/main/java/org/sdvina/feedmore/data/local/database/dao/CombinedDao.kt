package org.sdvina.feedmore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Transaction
import org.sdvina.feedmore.data.model.cross.FeedTitleWithEntriesToggleable
import org.sdvina.feedmore.data.local.database.entity.Entry
import org.sdvina.feedmore.data.model.entry.EntryToggleable
import org.sdvina.feedmore.data.local.database.entity.Feed

@Dao
interface CombinedDao: FeedsDao, EntriesDao, FeedEntryCrossRefsDao {

    @Transaction
    fun addFeedAndEntries(feed: Feed, entries: List<Entry>) {
        addFeeds(feed)
        addEntries(entries)
        addFeedEntryCrossRefs(feed.url, entries)
    }

    @Transaction
    fun handleEntryUpdates(
        feedId: String,
        entriesToAdd: List<Entry>,
        entriesToUpdate: List<Entry>,
        entriesToDelete: List<Entry>,
    ) {
        addEntries(entriesToAdd)
        addFeedEntryCrossRefs(feedId, entriesToAdd)
        updateEntries(entriesToUpdate)
        deleteFeedEntryCrossRefs(feedId, entriesToDelete.map { it.url })
        deleteEntries(entriesToDelete)
    }

    @Transaction
    fun handleBackgroundUpdate(
        feedId: String,
        newEntries: List<Entry>,
        oldEntries: List<EntryToggleable>,
        feedImage: String?
    ) {
        addEntries(newEntries)
        addFeedEntryCrossRefs(feedId, newEntries)
        oldEntries.map { it.url }.let { entryUrls ->
            deleteEntriesByUrl(entryUrls)
            deleteFeedEntryCrossRefs(feedId, entryUrls)
        }
        incrementFeedUnreadCount(feedId, (newEntries.size - oldEntries.filter { !it.isRead }.size))
        feedImage?.let { updateFeedImage(feedId, it) }
    }

    @Transaction
    fun updateEntryAndFeedUnreadCount(
        entryId: String,
        isRead: Boolean,
        isFavorite: Boolean
    ) {
        updateEntryIsFavorite(entryId, isFavorite = isFavorite)
        updateEntryIsReadAndFeedUnreadCount(entryId, isRead = isRead)
    }

    @Transaction
    fun updateEntryIsReadAndFeedUnreadCount(vararg entryUrl: String, isRead: Boolean) {
        updateEntryIsRead(*entryUrl, isRead = isRead)
        (if (isRead) -1 else 1).let { addend ->
            entryUrl.forEach { incrementFeedUnreadCountByEntry(it, addend) }
        }
    }

    @Transaction
    fun deleteFeedAndEntriesById(vararg feedUrl: String) {
        deleteEntriesByFeed(*feedUrl)
        deleteCrossRefsByFeed(*feedUrl)
        deleteFeeds(*feedUrl)
    }

    @Transaction
    fun deleteLeftoverItems() {
        deleteLeftoverCrossRefs()
        deleteLeftoverEntries()
    }
}