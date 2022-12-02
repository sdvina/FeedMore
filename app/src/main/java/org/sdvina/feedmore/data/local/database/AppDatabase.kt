package org.sdvina.feedmore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.sdvina.feedmore.data.local.database.dao.CombinedDao
import org.sdvina.feedmore.data.local.database.entity.Entry
import org.sdvina.feedmore.data.local.database.entity.Feed
import org.sdvina.feedmore.data.local.database.entity.FeedEntryCrossRef

@Database(
    entities = [Feed::class, Entry::class, FeedEntryCrossRef::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun combinedDao(): CombinedDao
}