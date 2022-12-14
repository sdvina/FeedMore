package org.sdvina.feedmore.ui.feed

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.sdvina.feedmore.data.model.feed.FeedLite
import org.sdvina.feedmore.data.model.feed.FeedManageable
import org.sdvina.feedmore.data.model.feed.FeedWithCategory
import org.sdvina.feedmore.repository.AppRepository

data class FeedViewState(
    val feedLites: List<FeedLite> = emptyList(),
    val feedsManageable: List<FeedManageable> = emptyList(),
    val feedUrlsWithCategories: List<FeedWithCategory> = emptyList(),
    val openedFeedUrl: String? = null,
    val selectedFeedUrls: Set<String>? = null,
    val refreshing: Boolean = false
)

class FeedViewModel(
    private val repository: AppRepository
): ViewModel() {
    private val _selectedFeedUrls = MutableStateFlow<Set<String>?>(null)
    private val refreshing = MutableStateFlow(false)
    private var _state = MutableStateFlow(FeedViewState())
    val sate: StateFlow<FeedViewState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                repository.getFeedLites(),
                repository.getFeedsManageable(),
                repository.getFeedUrlsWithCategories(),
                refreshing,
                _selectedFeedUrls
            ) { feedLites, feedsManageable, feedUrlsWithCategories, refreshing, selectedFeedUrls ->
                FeedViewState(
                    feedLites = feedLites,
                    feedsManageable = feedsManageable,
                    feedUrlsWithCategories = feedUrlsWithCategories,
                    refreshing = refreshing,
                    selectedFeedUrls = selectedFeedUrls
                )
            }.catch { throwable ->
                throw throwable // TODO
            }.collect { _state.value = it }
        }
    }

    fun onFeedSelected(vararg feedUrl: String){
        _selectedFeedUrls.value = setOf(*feedUrl)
    }

    fun deleteFeeds(vararg feedUrl: String) {
    }

    fun addFeedByUrl(feedUrl: String) {

    }

    fun importOmpl(uri: Uri) {
        viewModelScope.launch {
            //repository.addFolder(folderUri)
        }
    }
    companion object {
        fun provideFactory(
            repository: AppRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FeedViewModel(repository) as T
            }
        }
    }
}