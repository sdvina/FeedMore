package org.sdvina.feedmore.ui

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
}