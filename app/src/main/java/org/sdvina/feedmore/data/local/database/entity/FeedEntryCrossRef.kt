package org.sdvina.feedmore.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "feed_entry",
    primaryKeys = ["feed_url", "entry_url"],
    indices = [(Index(value = ["entry_url"]))]
)
data class FeedEntryCrossRef(
    @ColumnInfo(name = "feed_url") val feedUrl: String,
    @ColumnInfo(name = "entry_url")val entryUrl: String
)