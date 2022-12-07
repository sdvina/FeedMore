package org.sdvina.feedmore.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sdvina.feedmore.data.local.AppPreferences
import org.sdvina.feedmore.data.model.entry.EntryLite
import org.sdvina.feedmore.repository.FeedMoreRepository
import org.sdvina.feedmore.utils.ErrorMessage

private data class EntryViewModelState(
    val entryLiteFlow: Flow<PagingData<EntryLite>>? =  null,
    val selectedEntryUrl: String? = null,
    val isEntryOpen: Boolean = false,
    val favorites: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val searchInput: String = "",
)

class EntryViewModel(
    private val preferences: AppPreferences,
    private val repository: FeedMoreRepository
): ViewModel() {
    private val viewModelState = MutableStateFlow(EntryViewModelState(isLoading = true))
    private val pagingConfig = PagingConfig(pageSize = 20)
    // TODO
    //init {
/*        preferences.lastViewedFeedUrl?.let {
            viewModelState.update {
                it.entryLiteFlow = Pager(pagingConfig) {
                    repository.getPagedEntryLitesByFeed(it)
                }.flow.cachedIn(viewModelScope)
            }
        }*/
    //}
    init{
        viewModelScope.launch {
            preferences.lastViewedFeedUrl?.let { url ->
                viewModelState.update {
                    it.copy(entryLiteFlow = getEntryLiteFlow(url))
                }
            }
        }
    }

    private fun getEntryLiteFlow(feedUrl: String): Flow<PagingData<EntryLite>> {
       return Pager(pagingConfig){
            repository.getPagedEntryLitesByFeed(feedUrl)
        }.flow.cachedIn(viewModelScope)
    }

    fun errorShown(errorId: Long) {
        viewModelState.update { currentState ->
            val errorMessages = currentState.errorMessages.filterNot { it.id == errorId }
            currentState.copy(errorMessages = errorMessages)
        }
    }

    fun onSearchInputChanged(searchInput: String) {
        viewModelState.update {
            it.copy(searchInput = searchInput)
        }
    }

}