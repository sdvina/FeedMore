package org.sdvina.feedmore.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.local.database.AppDataBaseHelper
import org.sdvina.feedmore.data.AppRepository
import org.sdvina.feedmore.ui.feed.DrawerFeedList
import org.sdvina.feedmore.ui.feed.FeedViewModel
import org.sdvina.feedmore.util.NetworkMonitor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    appNavigation: AppNavigation,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    repository: AppRepository
) {
    ModalDrawerSheet(modifier){
        DrawerHeader()
        Spacer(Modifier.height(12.dp))
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Add, contentDescription = null) },
            label = { Text(stringResource(R.string.add_feeds)) },
            selected = false,
            onClick = {
                appNavigation.navigateToAddFeed()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.RssFeed, contentDescription = null) },
            label = { Text(stringResource(R.string.manage_feeds)) },
            selected = false,
            onClick = {
                appNavigation.navigateToManageFeed()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Newspaper, contentDescription = null) },
            label = { Text(stringResource(R.string.new_entries)) },
            selected = false,
            onClick = {
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Bookmarks, contentDescription = null) },
            label = { Text(stringResource(R.string.favorite_entries)) },
            selected = false,
            onClick = {
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        val viewModel: FeedViewModel = viewModel(factory = FeedViewModel.provideFactory(repository))
        DrawerFeedList(
            navController = rememberNavController(),
            closeDrawer = closeDrawer,
            viewModel = viewModel
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            label = { Text(stringResource(R.string.settings)) },
            selected = false,
            onClick = {
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Filled.Info, contentDescription = null) },
            label = { Text(stringResource(R.string.about)) },
            selected = false,
            onClick = {
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Preview
@Composable
fun AppDrawerPreview(){
    AppDataBaseHelper.onCreate(LocalContext.current)
    AppRepository.init(AppDataBaseHelper.db, NetworkMonitor(LocalContext.current))
    AppDrawer(
        appNavigation = AppNavigation(rememberNavController()),
        closeDrawer = {  },
        repository = AppRepository.get()
    )
}

//@Preview
@Composable
fun DrawerHeader(modifier: Modifier = Modifier) {
    val imageUrl = "https://bing.img.run/1366x768.php"
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .placeholder(R.drawable.feedmore_drawer_header)
            .data(imageUrl)
            .size(360, 240)
            .build(),
        contentDescription = null,
        modifier = modifier.size(360.dp, 240.dp),
        alignment = Alignment.CenterStart,
        contentScale = ContentScale.Crop
    )
}