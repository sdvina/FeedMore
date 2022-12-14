package org.sdvina.feedmore

import android.app.Application
import com.tencent.mmkv.MMKV
import org.sdvina.feedmore.data.local.AppPreferences
import org.sdvina.feedmore.data.local.database.AppDataBaseHelper
import org.sdvina.feedmore.repository.AppRepository
import org.sdvina.feedmore.utils.NetworkMonitor

class FeedMoreApplication : Application() {
    lateinit var repository: AppRepository

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        AppDataBaseHelper.onCreate(this)
        val networkMonitor = NetworkMonitor(this)
        AppRepository.init(AppDataBaseHelper.db, networkMonitor)
        repository = AppRepository.get()
        // TODO init KKM
        MMKV.initialize(this)
    }
}