package org.sdvina.feedmore.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.sdvina.feedmore.data.local.database.entity.Feed
import org.sdvina.feedmore.data.model.feed.FeedWithCategory
import org.sdvina.feedmore.data.model.feed.FeedLight
import org.sdvina.feedmore.data.model.feed.FeedManageable

interface FeedsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFeeds(vararg feed: Feed)

    @Query("SELECT * FROM feed WHERE url = :feedUrl")
    fun getFeed(feedUrl: String): LiveData<Feed?>

    @Query("SELECT url, title, image_url, category, unread_count FROM feed")
    fun getFeedsLight(): LiveData<List<FeedLight>>

    @Query("SELECT url, title, website, image_url, description, category FROM feed")
    fun getFeedsManageable(): LiveData<List<FeedManageable>>

    @Query("SELECT url FROM feed")
    fun getFeedUrls(): LiveData<List<String>>

    @Query("SELECT url, category FROM feed")
    fun getFeedUrlsWithCategories(): LiveData<List<FeedWithCategory>>

    @Update
    fun updateFeed(feed: Feed)

    @Query("UPDATE feed SET title = :title WHERE url = :feedUrl")
    fun updateFeedTitle(feedUrl: String, title: String)

    @Query("UPDATE feed SET category = :category WHERE url IN (:feedUrl)")
    fun updateFeedCategory(vararg feedUrl: String, category: String)

    @Transaction
    fun updateFeedTitleAndCategory(feedUrl: String, title: String, category: String) {
        updateFeedTitle(feedUrl, title)
        updateFeedCategory(feedUrl, category = category)
    }

    @Query("UPDATE feed SET image_url = :feedImage WHERE url = :feedUrl")
    fun updateFeedImage(feedUrl: String, feedImage: String)

    @Query("UPDATE feed SET unread_count = :newCount WHERE url = :feedUrl")
    fun updateFeedUnreadCount(feedUrl: String, newCount: Int)

    @Query("UPDATE feed SET unread_count = (unread_count + :addend) WHERE url = :feedUrl")
    fun incrementFeedUnreadCount(feedUrl: String, addend: Int)

    @Query(
        "UPDATE Feed SET unread_count = (unread_count + :addend) WHERE url IN " +
            "(SELECT url FROM feed_entry " +
            "INNER JOIN feed ON (feed_entry.feed_url = feed.url) " +
            "WHERE feed_entry.entry_url = (:entryUrl))"
    )
    fun incrementFeedUnreadCountByEntry(entryUrl: String, addend: Int)

    @Query("DELETE FROM feed WHERE url IN (:feedUrl)")
    fun deleteFeeds(vararg feedUrl: String)
}