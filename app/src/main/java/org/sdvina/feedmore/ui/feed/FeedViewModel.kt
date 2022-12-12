package org.sdvina.feedmore.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.sdvina.feedmore.data.model.feed.FeedLite
import org.sdvina.feedmore.repository.FeedMoreRepository
import org.sdvina.feedmore.ui.entry.EntryViewModel

data class FeedViewState(
    val feedLites: List<FeedLite> = emptyList(),
    val openedFeedUrl: String? = null,
    val selectedFeedUrls: Set<String>? = null,
    val refreshing: Boolean = false
)

class FeedViewModel(
    private val repository: FeedMoreRepository
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
                refreshing,
                _selectedFeedUrls
            ) { feedLites, refreshing, selectedFeedUrls ->
                FeedViewState(
                    feedLites = feedLites,
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

    fun deleteFeeds(vararg feedUrl: String){
    }

    companion object {
        fun provideFactory(
            repository: FeedMoreRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FeedViewModel(repository) as T
            }
        }
    }
}