package org.sdvina.feedmore.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import org.sdvina.feedmore.data.local.database.entity.Entry
import org.sdvina.feedmore.data.model.entry.EntryLite

interface EntriesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEntries(entries: List<Entry>)

    @Query("SELECT * FROM entry WHERE url = :entryUrl")
    fun getEntry(entryUrl: String): LiveData<Entry?>

    @Query("SELECT * FROM entry WHERE is_read = 0 ORDER BY date DESC")
    fun getPagedNewEntries(): PagingSource<Int, Entry>

    @Query("SELECT * FROM entry WHERE is_favorite = 1")
    fun getPagedFavoriteEntries(): PagingSource<Int, Entry>

    @Query(
        "SELECT url, title, website, date, image_url, is_favorite, is_read FROM entry " +
            "INNER JOIN feed_entry ON entry.url = feed_entry.entry_url " +
            "WHERE feed_entry.feed_url = :feedUrl"
    )
    fun getPagedEntryLitesByFeed(feedUrl: String): PagingSource<Int, EntryLite>

    @Update
    fun updateEntries(entries: List<Entry>)

    @Query("UPDATE entry SET is_favorite = :isFavorite WHERE url IN (:entryUrl)")
    fun updateEntryIsFavorite(vararg entryUrl: String, isFavorite: Boolean)

    @Query("UPDATE entry SET is_read = :isRead WHERE url IN (:entryUrl)")
    fun updateEntryIsRead(vararg entryUrl: String, isRead: Boolean)

    @Delete
    fun deleteEntries(entries: List<Entry>)

    @Query(
        "DELETE FROM Entry WHERE url IN " +
                "(SELECT url FROM feed_entry " +
                "INNER JOIN entry ON (feed_entry.entry_url = entry.url) " +
                "WHERE feed_entry.feed_url IN (:feedUrl))"
    )
    fun deleteEntriesByFeed(vararg feedUrl: String)

    @Query("DELETE FROM entry WHERE url IN (:entryUrls)")
    fun deleteEntriesByUrl(entryUrls: List<String>)

    @Query("DELETE FROM entry WHERE url NOT IN (SELECT entry_url FROM feed_entry)")
    fun deleteLeftoverEntries()
}