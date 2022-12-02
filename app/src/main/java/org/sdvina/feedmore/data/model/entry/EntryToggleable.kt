package org.sdvina.feedmore.data.model.entry

import androidx.room.ColumnInfo

data class EntryToggleable(
    val url: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "is_read") val isRead: Boolean,
)