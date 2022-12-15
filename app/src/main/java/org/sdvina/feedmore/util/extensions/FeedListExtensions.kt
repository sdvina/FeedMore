package org.sdvina.feedmore.util.extensions

import org.sdvina.feedmore.data.model.feed.FeedLite
import org.sdvina.feedmore.data.model.feed.FeedManageable

@JvmName("sortedByTitleFeedLight")
fun List<FeedLite>.sortedByTitle() = this.sortedBy { it.title }

fun List<FeedLite>.sortedByUnreadCount() = this.sortedByDescending { it.unreadCount }

fun List<FeedManageable>.sortedByTitle() = this.sortedBy { it.title }

fun List<FeedManageable>.sortedByCategory() = this.sortedBy { it.category }