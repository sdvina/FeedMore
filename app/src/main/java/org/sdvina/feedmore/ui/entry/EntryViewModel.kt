package org.sdvina.feedmore.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.sdvina.feedmore.data.local.AppPreferences
import org.sdvina.feedmore.data.model.entry.EntryLite
import org.sdvina.feedmore.repository.FeedMoreRepository
import org.sdvina.feedmore.utils.ErrorMessage

private data class EntryViewModelState(
    val pagedEntryLites: PagingSource<Int, EntryLite>? =  null,
    val selectedEntryUrl: String? = null,
    val isEntryOpen: Boolean = false,
    val favorites: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: List<ErrorMessage> = emptyList(),
    val searchInput: String = "",
)

class EntryViewModel(
    preferences: AppPreferences,
    repository: FeedMoreRepository
): ViewModel() {
    private val viewModelState = MutableStateFlow(EntryViewModelState(isLoading = true))
    private val pagingConfig = PagingConfig(pageSize = 20)
    // TODO
    init {
        preferences.lastViewedFeedId?.let {
            Pager(pagingConfig) {
                repository.getPagedEntryLitesByFeed(it)
            }.flow.cachedIn(viewModelScope)
        }
    }
}