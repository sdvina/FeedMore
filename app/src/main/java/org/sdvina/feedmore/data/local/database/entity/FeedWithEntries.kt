package org.sdvina.feedmore.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

// one to many
data class FeedWithEntries(
    @Embedded val feed: Feed,
    @Relation(
        parentColumn = "url",
        entityColumn = "url",
        associateBy = Junction(
            value = FeedEntryCrossRef::class,
            parentColumn = "feed_url",
            entityColumn = "entry_url"
        )
    )
    val entries: List<Entry>
)