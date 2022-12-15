package org.sdvina.feedmore.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sdvina.feedmore.data.local.AppPreferences
import org.sdvina.feedmore.data.model.entry.EntryLite
import org.sdvina.feedmore.repository.AppRepository

data class EntryViewState(
    val entryLites: PagingData<EntryLite> = PagingData.empty(),
    val selectedEntryUrl: String? = null,
    val isEntryOpen: Boolean = false,
    val favorites: Set<String> = emptySet(),
    val messages: List<Map<Long, String>> = listOf(),
    val refreshing: Boolean = false,
    val searchInput: String = "",
)

class EntryViewModel(
    private val repository: AppRepository
): ViewModel() {
    private val pagingConfig = PagingConfig(pageSize = 20)
    private val refreshing = MutableStateFlow(false)
    private val _state = MutableStateFlow(EntryViewState())
    val state: StateFlow<EntryViewState>
        get() = _state

    init{
        viewModelScope.launch {
            combine(
                getEntryLiteFlow(AppPreferences.lastViewedFeedUrl),
                refreshing
            ) { entryLites, refreshing ->
                EntryViewState(
                    entryLites = entryLites,
                    refreshing = refreshing
                )

            }.catch { throwable ->
                // TODO
                throw throwable
            }.collect {
                _state.value = it
            }
            //refresh("")
        }
    }

    private fun refresh(feedUrl: String) {
        viewModelScope.launch {
            runCatching {
                refreshing.value = true
                    //TODO
            }
            refreshing.value = false
        }
    }

    private fun getEntryLiteFlow(feedUrl: String?): Flow<PagingData<EntryLite>> {
        feedUrl?.let {
            return Pager(pagingConfig){
                repository.getPagedEntryLitesByFeed(feedUrl)
            }.flow.cachedIn(viewModelScope)
        }
        return emptyFlow()
    }

    fun showMessage(messageId: Long) {
        _state.update { currentState ->
            val messages = currentState.messages.filterNot {it.containsKey(messageId)}
            currentState.copy(messages = messages)
        }
    }

    fun onEntrySelected(entryUrl: String){
        // TODO
    }

    fun onSearchInputChanged(searchInput: String) {
        _state.update {
            it.copy(searchInput = searchInput)
        }
    }

    companion object {
        fun provideFactory(
            repository: AppRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EntryViewModel(repository) as T
            }
        }
    }
}