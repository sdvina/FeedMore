package org.sdvina.feedmore.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.sdvina.feedmore.data.model.entry.EntryMinimal
import org.sdvina.feedmore.data.remote.FeedFetcher
import java.io.Serializable
import java.util.*

@Entity
data class Entry(
    @PrimaryKey val url: String,
    val title: String,
    val website: String,
    val author: String?,
    val date: Date?,
    val content: String?,
    @ColumnInfo(name = "image_url") var imageUrl: String?,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "is_read") var isRead: Boolean = false
) : Serializable {

    fun isSameAs(entry: Entry): Boolean {
        val checklist = listOf(
            entry.title == title,
            entry.author == author,
            entry.date == date,
            entry.content == content,
            entry.imageUrl == imageUrl
        )
        var count = 0
        for (itemChecked in checklist) {
            if (itemChecked) count += 1 else break
        }
        return count == checklist.size
    }

    fun toMinimal(): EntryMinimal {
        val content = this.content?.removePrefix(FeedFetcher.FLAG_EXCERPT) ?: ""
        return EntryMinimal(title, date, author, content)
    }

    companion object {

        const val SORT_BY_TITLE = 0
        const val SORT_BY_UNREAD = 1
    }
}