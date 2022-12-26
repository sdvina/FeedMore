package org.sdvina.feedmore.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.local.database.entity.Feed
import org.sdvina.feedmore.data.model.feed.FeedLite
import org.sdvina.feedmore.data.model.feed.FeedManageable
import org.sdvina.feedmore.data.model.feed.FeedWithCategory
import org.sdvina.feedmore.data.remote.FeedFetcher
import org.sdvina.feedmore.data.AppRepository
import java.util.*

data class FeedViewState(
    val feedLites: List<FeedLite> = emptyList(),
    val feedsManageable: List<FeedManageable> = emptyList(),
    val feedUrlsWithCategories: List<FeedWithCategory> = emptyList(),
    val openedFeedUrl: String? = null,
    val selectedFeedUrls: Set<String>? = null,
    val messages: List<Pair<Long, Int>> = emptyList(),
    val refreshing: Boolean = false
)

class FeedViewModel(
    private val repository: AppRepository
): ViewModel() {
    private val _selectedFeedUrls = MutableStateFlow<Set<String>?>(null)
    private val refreshing = MutableStateFlow(false)
    private var _state = MutableStateFlow(FeedViewState())
    private val fetcher = FeedFetcher()
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

    fun addFeeds(vararg feed: Feed) {
        repository.addFeeds(*feed)
        _state.update { it.copy(messages = it.messages + Pair(UUID.randomUUID().mostSignificantBits, R.string.import_opml)) }
    }

    fun messageShown(messageId: Long) {
        _state.update { currentState ->
            val messages = currentState.messages.filterNot { it.first == messageId }
            currentState.copy(messages = messages)
        }
    }


    fun onFeedSelected(vararg feedUrl: String){
        _selectedFeedUrls.value = setOf(*feedUrl)
    }

    fun deleteFeeds(vararg feedUrl: String) {
    }

    fun requestUrl(feedUrl: String) {
        viewModelScope.launch {
            fetcher.request(feedUrl)
            //_state.update { it.copy(messages = listOf(Pair(2L, "abc"))) }
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