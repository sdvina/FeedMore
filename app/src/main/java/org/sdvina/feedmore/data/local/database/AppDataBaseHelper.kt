package org.sdvina.feedmore.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDataBaseHelper {
    lateinit var db: AppDatabase
    private const val DB_NAME = "FEED_MORE"

    // Migration path definition from version 1 to version 2.
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // do migration
        }
    }

    fun onCreate(context: Context) {
        db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            //.enableMultiInstanceInvalidation() // support multi-instance
            //.fallbackToDestructiveMigration() // support fall back
            .build()
    }
}