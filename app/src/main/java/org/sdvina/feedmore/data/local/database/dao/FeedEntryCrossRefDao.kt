package org.sdvina.feedmore.data.local.database.dao

import androidx.room.*
import org.sdvina.feedmore.data.local.database.entity.FeedEntryCrossRef
import org.sdvina.feedmore.data.local.database.entity.Entry

interface FeedEntryCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFeedEntryCrossRefs(crossRefs: List<FeedEntryCrossRef>)

    @Transaction
    fun addFeedEntryCrossRefs(feedUrl: String, entries: List<Entry>) {
        addFeedEntryCrossRefs(entries.map { FeedEntryCrossRef(feedUrl, it.url) })
    }

    @Query("DELETE FROM feed_entry WHERE feed_url = :feedUrl AND entry_url IN (:entryUrls)")
    fun deleteFeedEntryCrossRefs(feedUrl: String, entryUrls: List<String>)

    @Query("DELETE FROM feed_entry WHERE feed_url IN (:feedUrl)")
    fun deleteCrossRefsByFeed(vararg feedUrl: String)

    @Query("DELETE FROM feed_entry WHERE feed_url NOT IN (SELECT url FROM Feed)")
    fun deleteLeftoverCrossRefs()
}