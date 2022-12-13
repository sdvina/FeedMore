package org.sdvina.feedmore.ui.feed

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.local.database.AppDataBaseHelper
import org.sdvina.feedmore.repository.FeedMoreRepository
import org.sdvina.feedmore.ui.theme.FeedMoreTheme
import org.sdvina.feedmore.utils.NetworkMonitor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun FeedAddScreen(
    navController: NavController,
    viewModel: FeedViewModel
){
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_feeds),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.cd_back))
                    }
                }
            )
        }
    ){ innerPadding ->
        val viewState by viewModel.sate.collectAsStateWithLifecycle()
        var operator by remember { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            OperatorItem(
                label = stringResource(R.string.add_feed_by_url),
                imageVector = Icons.Filled.Link) {
                operator = 1
            }
            OperatorItem(
                label = stringResource(R.string.import_opml),
                imageVector = Icons.Filled.Upload) {
                operator = 2
            }
            OperatorItem(
                label = stringResource(R.string.add_feed_by_radar),
                imageVector = Icons.Filled.Radar) {
                operator = 3
            }

            when(operator) {
                1 -> {
                    AddFeedByUrl(Modifier.padding(innerPadding))
                    operator = 0
                }
            }
        }
    }
}

@Composable
fun OperatorItem(
    label: String,
    imageVector: ImageVector,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(8.dp)
        )
    }
}

@Composable
fun AddFeedByUrl(modifier: Modifier){
    Text(text = "1111111111")
    Snackbar(modifier = modifier) {
        OperatorItem(label="", imageVector = Icons.Filled.Link, {})
        Divider()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun FeedAddScreenPreview(){
    AppDataBaseHelper.onCreate(LocalContext.current)
    FeedMoreRepository.init(AppDataBaseHelper.db, NetworkMonitor(LocalContext.current))
    FeedMoreTheme {
        FeedAddScreen(
           navController = rememberAnimatedNavController(),
            viewModel = viewModel(factory = FeedViewModel.provideFactory(FeedMoreRepository.get()))
        )
    }
}