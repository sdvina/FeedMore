package org.sdvina.feedmore.ui.entry

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sdvina.feedmore.ui.components.MoreActionsButton
import org.sdvina.feedmore.ui.theme.AppTheme
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.local.database.AppDataBaseHelper
import org.sdvina.feedmore.repository.AppRepository
import org.sdvina.feedmore.util.NetworkMonitor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun EntryListScreen(
    openDrawer: () -> Unit,
    navController: NavHostController,
    entryViewModel: EntryViewModel
) {
    val viewState by entryViewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val isRefreshing = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Feed Name",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) },
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(R.string.cd_menu)
                        )
                    } },
                actions = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.cd_search)
                        ) }
                    MoreActionsButton {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_filter)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Filter,
                                    contentDescription = stringResource(R.string.cd_filter)
                                )})
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_mark_all_as_read)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.CheckCircle,
                                    contentDescription = stringResource(R.string.cd_mark_all_as_read)
                                )})
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_bookmark_all)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Bookmark,
                                    contentDescription = stringResource(R.string.cd_bookmark_all)
                                )})
                        Divider()
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_feed_info)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = stringResource(R.string.cd_feed_info)
                                )})
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_visit_website)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Link,
                                    contentDescription = stringResource(R.string.cd_visit_website)
                                )})
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_remove_feed)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = stringResource(R.string.cd_remove_feed)
                                )})
                    }
                }
            )
        }
    ){ innerPadding ->
        val contentModifier = Modifier.padding(innerPadding)
        SwipeRefreshScreen(
            modifier = contentModifier,
            isRefreshing = isRefreshing.value,
            onRefresh = {
                scope.launch {
                    isRefreshing.value = true
                    delay(2000)
                    //items.value = items.value.shuffled()
                    isRefreshing.value = false
                }
            }
        )
    }
}




@Composable
fun SwipeRefreshScreen(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
) {
    val refreshState = rememberSwipeRefreshState(isRefreshing)

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SwipeRefresh(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = refreshState,
            onRefresh = { onRefresh() }
        ) {
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {

            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun EntryListScreenPreview(){
    AppTheme {
        AppDataBaseHelper.onCreate(LocalContext.current)
        AppRepository.init(AppDataBaseHelper.db, NetworkMonitor(LocalContext.current))
        EntryListScreen(
            openDrawer = {},
            navController = rememberAnimatedNavController(),
            entryViewModel = viewModel(factory = EntryViewModel.provideFactory(AppRepository.get()))
        )
    }
}