package org.sdvina.feedmore.ui.feed

import androidx.lifecycle.ViewModel
import org.sdvina.feedmore.data.local.AppPreferences
import org.sdvina.feedmore.repository.FeedMoreRepository

class FeedViewModel(
    private val preferences: AppPreferences,
    private val repository: FeedMoreRepository
): ViewModel() {

}