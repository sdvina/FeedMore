package org.sdvina.feedmore.data.model.feed

import androidx.room.ColumnInfo

data class FeedLite(
    val url: String,
    var title: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    var category: String = "Uncategorized",
    @ColumnInfo(name = "unread_count") var unreadCount: Int
)