package org.sdvina.feedmore.data.model.feed

import androidx.room.ColumnInfo

data class FeedLight(
    val url: String,
    var title: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    var category: String,
    @ColumnInfo(name = "unread_count") var unreadCount: Int
)