package org.sdvina.feedmore.ui.entry

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.launch
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.local.database.AppDataBaseHelper
import org.sdvina.feedmore.repository.AppRepository
import org.sdvina.feedmore.ui.components.BookmarkButton
import org.sdvina.feedmore.ui.components.MoreActionsButton
import org.sdvina.feedmore.ui.theme.AppTheme
import org.sdvina.feedmore.utils.NetworkMonitor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun EntryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel
) {
    val viewSate by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viewSate.searchInput, // TODO FIX
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    } },
                actions = {
                    BookmarkButton(isBookmarked = true) {
                        
                    }
                    IconButton(onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar( "test snackbar")
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.OpenInBrowser, contentDescription = stringResource(R.string.cd_open_in_browser))
                    }
                    MoreActionsButton {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_share)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = { Icon(Icons.Filled.Share, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_copy_link)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = { Icon(Icons.Filled.Link, contentDescription = null) }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.item_change_text_size)) },
                            onClick = { /* Handle! */ },
                            leadingIcon = { Icon(Icons.Filled.TextFields, contentDescription = null) }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){ innerPadding ->
        WebView(
            state = rememberWebViewState(url = "https://wwww.baidu.com"),
            modifier = modifier.padding(innerPadding),
            navigator = rememberWebViewNavigator()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun EntryScreenPreview(){
    AppDataBaseHelper.onCreate(LocalContext.current)
    AppRepository.init(AppDataBaseHelper.db, NetworkMonitor(LocalContext.current))
    AppTheme() {
        EntryScreen(
            navController = rememberAnimatedNavController(),
            viewModel = viewModel(factory = EntryViewModel.provideFactory(AppRepository.get()))
        )
    }
}