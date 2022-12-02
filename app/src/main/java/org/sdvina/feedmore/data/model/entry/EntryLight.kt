package org.sdvina.feedmore.data.model.entry

import androidx.room.ColumnInfo
import java.util.*

data class EntryLight(
    val url: String,
    val title: String,
    val website: String,
    val date: Date?,
    val image: String?,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "is_read") var isRead: Boolean = false
)