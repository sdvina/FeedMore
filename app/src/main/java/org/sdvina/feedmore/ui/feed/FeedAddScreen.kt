package org.sdvina.feedmore.ui.feed

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import org.sdvina.feedmore.repository.AppRepository
import org.sdvina.feedmore.ui.theme.AppTheme
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
        val operator = remember { mutableStateOf(0) }
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(), onResult = { uri ->
            operator.value = 0
            uri?.let { viewModel.importOmpl(it) }
        })
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            OperatorItem(
                label = stringResource(R.string.add_feed_by_url),
                imageVector = Icons.Filled.Link
            ) { operator.value = 1 }
            OperatorItem(
                label = stringResource(R.string.import_opml),
                imageVector = Icons.Filled.Upload
            ) { operator.value = 2 }
            OperatorItem(
                label = stringResource(R.string.add_feed_by_radar),
                imageVector = Icons.Filled.Radar
            ) { operator.value = 3 }

            when(operator.value) {
                1 -> {
                    AddFeedByUrlDialog(
                        operator = operator,
                        onSubmit = { viewModel.addFeedByUrl(it) }
                    )
                }
                2 -> { launcher.launch("document/*.opml") }
                3 -> {
                    AddFeedByRadarDialog(
                        operator = operator,
                        onSubmit = { /** TODO */ }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFeedByUrlDialog(operator: MutableState<Int>,  onSubmit: (String) -> Unit) {
    var alertDialog by remember { mutableStateOf(true) }
    if(!alertDialog)  operator.value = 0
    if (alertDialog) {
        var text by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { alertDialog = false },
            title = {
                OperatorItem(
                    label = stringResource(R.string.add_feed_by_url),
                    imageVector = Icons.Filled.Link,
                    onClick = { }
                )
            },
            text= {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(text = "URL") },
                    placeholder = { Text(text = "Please input a feed url") },
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onSubmit(text)}
                ) { Text(text = stringResource(R.string.submit)) }
            },
            dismissButton = {
                TextButton(onClick = { alertDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFeedByRadarDialog(operator: MutableState<Int>, onSubmit: (String) -> Unit) {
    var alertDialog by remember { mutableStateOf(true) }
    if(!alertDialog)  operator.value = 0
    if (alertDialog) {
        var text by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { alertDialog = false },
            title = {
                OperatorItem(
                    label = stringResource(R.string.add_feed_by_radar),
                    imageVector = Icons.Filled.Radar,
                    onClick = { }
                )
            },
            text= {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(text = "URL") },
                    placeholder = { Text(text = "Please input a website url to scan") },
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onSubmit(text) }
                ) { Text(text = stringResource(R.string.submit)) }
            },
            dismissButton = {
                TextButton(onClick = { alertDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun OperatorItem(
    imageVector: ImageVector,
    label: String,
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

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun FeedAddScreenPreview(){
    AppDataBaseHelper.onCreate(LocalContext.current)
    AppRepository.init(AppDataBaseHelper.db, NetworkMonitor(LocalContext.current))
    AppTheme {
        FeedAddScreen(
           navController = rememberAnimatedNavController(),
            viewModel = viewModel(factory = FeedViewModel.provideFactory(AppRepository.get()))
        )
    }
}