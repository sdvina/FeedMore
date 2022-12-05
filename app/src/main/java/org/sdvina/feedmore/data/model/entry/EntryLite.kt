package org.sdvina.feedmore.data.model.entry

import androidx.room.ColumnInfo
import java.util.*

data class EntryLite(
    val url: String,
    val title: String,
    val website: String,
    val date: Date?,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "is_read") var isRead: Boolean = false
)