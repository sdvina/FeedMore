package org.sdvina.feedmore.data.model.feed

import java.io.Serializable

data class FeedManageable(
    val url: String,
    var title: String,
    val website: String,
    val imageUrl: String?,
    val description: String?,
    var category: String
): Serializable