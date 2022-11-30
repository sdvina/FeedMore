package org.sdvina.feedmore.data.model.feed

data class FeedLight(
    val url: String,
    var title: String,
    val imageUrl: String?,
    var category: String,
    var unreadCount: Int
)