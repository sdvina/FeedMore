package org.sdvina.feedmore.ui.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sdvina.feedmore.ui.components.MoreActionsButton
import org.sdvina.feedmore.ui.theme.FeedMoreTheme
import org.sdvina.feedmore.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryListSreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Feed Name",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(R.string.cd_menu)
                        )
                    }
                },
                actions = {
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
                                    contentDescription = stringResource(R.string.cd_visit_website)
                                )})
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val list = (0..5).map { it.toString() }
                items(count = list.size) {
                    Text(
                        text = list[it],
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun EntryScreenPreview(){
    FeedMoreTheme {
        EntryListSreen()
    }
}