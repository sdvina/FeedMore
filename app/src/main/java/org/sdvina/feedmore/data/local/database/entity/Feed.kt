package org.sdvina.feedmore.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Feed(
    @PrimaryKey val url: String,
    var title: String,
    val website: String,
    val description: String? = null,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null,
    var category: String = "Uncategorized",
    @ColumnInfo(name = "unread_count") var unreadCount: Int
): Serializable {

    companion object {

        const val SORT_BY_TITLE = 0
        const val SORT_BY_UNREAD = 1
    }
}