package org.sdvina.feedmore.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.sdvina.feedmore.FeedMoreApplication

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as FeedMoreApplication).repository
        setContent {
            FeedMoreApp(repository)
        }
    }

    companion object {

        private const val REQUEST_CODE_ADD_FEED = 0
        private const val FRAGMENT_MAIN = 0
        private const val FRAGMENT_NAVIGATION = 1

        const val EXTRA_FEED_ID = "org.sdvina.feedmore.feed_id"
        const val EXTRA_ENTRY_ID = "org.sdvina.feedmore.entry_id"

        fun newIntent(context: Context, feedId: String, latestEntryId: String): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_FEED_ID, feedId)
                putExtra(EXTRA_ENTRY_ID, latestEntryId)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }
    }
}