package org.sdvina.feedmore.ui.feed

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.local.database.AppDataBaseHelper
import org.sdvina.feedmore.data.model.feed.FeedManageable
import org.sdvina.feedmore.repository.AppRepository
import org.sdvina.feedmore.ui.components.MoreActionsButton
import org.sdvina.feedmore.utils.NetworkMonitor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun FeedManageScreen(
    openDrawer: () -> Unit,
    viewModel: FeedViewModel
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.manage_feeds),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) },
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = stringResource(R.string.cd_menu))
                    } },
                actions = {
                    IconButton(
                        onClick = {  }
                    ) { Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.cd_search)) }
                    MoreActionsButton {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_sort)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = {
                                Icon(Icons.Filled.Filter, contentDescription = null)
                            })
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_export_opml)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = {
                                Icon(Icons.Filled.Download, contentDescription = null)
                            })
                        Divider()
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.add_feeds)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = {
                                Icon(Icons.Filled.Add, contentDescription = null)
                            })
                    }
                }
            )
        }
    ){ innerPadding ->
        val viewState by viewModel.sate.collectAsStateWithLifecycle()
        val feedsManageable = viewState.feedsManageable
        ManageableFeedList(
            modifier = Modifier.padding(innerPadding),
                    feedsManageable = feedsManageable
        )

    }
}

@Composable
fun ManageableFeedList(
    modifier: Modifier = Modifier,
    feedsManageable: List<FeedManageable>
){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        var checkedAll by remember{ mutableStateOf(false) }
        var checkedCount by remember{ mutableStateOf(0) }
        Row(modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = when(checkedCount){
                0 -> false
                feedsManageable.size -> true
                else -> checkedAll
            },
            onCheckedChange = {
                checkedAll = !checkedAll
                checkedCount = when(checkedAll){
                    true -> feedsManageable.size
                    false -> 0
                }
            })
            Text(text = stringResource(R.string.check_all))
            if(checkedCount > 0){
                Text(text = stringResource(R.string.feeds_selected, checkedCount))
            }
        }
        feedsManageable.forEach {
            var checked by remember { mutableStateOf(false) }
            Row(modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = when(checkedCount){
                        0 -> false
                        feedsManageable.size -> true
                        else -> checked
                    },
                    onCheckedChange = {
                        checked = !checked
                        when(checked){
                            true -> checkedCount++
                            false -> if(checkedCount != 0) checkedCount--
                        }
                    }
                )
                Column() {
                    Text(
                        text ="${it.title} ${it.website}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = it.category,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FeedManageScreenPreview(){
    AppDataBaseHelper.onCreate(LocalContext.current)
    AppRepository.init(AppDataBaseHelper.db, NetworkMonitor(LocalContext.current))
    FeedManageScreen(
        openDrawer = { },
        viewModel = viewModel(factory = FeedViewModel.provideFactory(AppRepository.get()))
    )
}