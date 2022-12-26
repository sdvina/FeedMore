package org.sdvina.feedmore

import android.app.Application
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sdvina.feedmore.data.local.AppPreferences
import org.sdvina.feedmore.data.local.database.AppDataBaseHelper
import org.sdvina.feedmore.data.AppRepository
import org.sdvina.feedmore.util.NetworkMonitor
import org.sdvina.feedmore.util.work.BackgroundSyncWorker
import org.sdvina.feedmore.util.work.NewEntriesWorker
import org.sdvina.feedmore.util.work.SweeperWorker

class FeedMoreApplication : Application() {
    lateinit var repository: AppRepository
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        AppDataBaseHelper.onCreate(this)
        val networkMonitor = NetworkMonitor(this)
        AppRepository.init(AppDataBaseHelper.db, networkMonitor)
        repository = AppRepository.get()
        // TODO init KKM
        MMKV.initialize(this)

        applicationScope.launch {
            if (AppPreferences.shouldPoll) NewEntriesWorker.start(applicationContext)
            if (AppPreferences.shouldSyncInBackground) BackgroundSyncWorker.start(applicationContext)
            SweeperWorker.start(applicationContext)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "feedmore_new_entries"
    }
}