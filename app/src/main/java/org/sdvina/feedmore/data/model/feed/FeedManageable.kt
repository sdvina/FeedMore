package org.sdvina.feedmore.data.model.feed

import androidx.room.ColumnInfo
import java.io.Serializable

data class FeedManageable(
    val url: String,
    var title: String,
    val website: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    val description: String?,
    var category: String
): Serializable