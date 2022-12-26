package org.sdvina.feedmore.util

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import org.sdvina.feedmore.util.work.NewEntriesWorker

class NotificationReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        if (resultCode != Activity.RESULT_OK) return
        if (resultCode == Activity.RESULT_OK) {
            val requestCode = intent.getIntExtra(NewEntriesWorker.EXTRA_REQUEST_CODE, 0)
            val notification = intent.getParcelableExtra(NewEntriesWorker.EXTRA_NOTIFICATION, Notification::class.java)
            notification?.let { NotificationManagerCompat.from(context).notify(requestCode, it) }
        }
    }
}