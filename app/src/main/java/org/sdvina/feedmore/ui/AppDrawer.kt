package org.sdvina.feedmore.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.local.database.AppDataBaseHelper
import org.sdvina.feedmore.repository.AppRepository
import org.sdvina.feedmore.ui.feed.DrawerFeedList
import org.sdvina.feedmore.ui.feed.FeedViewModel
import org.sdvina.feedmore.util.NetworkMonitor

@Composable
fun AppDrawer(
    navController: NavController,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    repository: AppRepository
) {

    LazyColumn(modifier = modifier) {
        item { DrawerHeader() }
        item { Spacer(modifier.padding(top = 8.dp)) }
        item { DrawerButton(icon = Icons.Filled.Add, label = stringResource(R.string.add_feeds)){
            navController.navigate(AppDestinations.ADD_FEED_ROUTE) {
                launchSingleTop = true
                restoreState = true
            }
            closeDrawer()
        }}
        item { DrawerButton(icon = Icons.Filled.RssFeed, label = stringResource(R.string.manage_feeds)){
            navController.navigate(AppDestinations.MANAGE_FEED_ROUTE) {
                launchSingleTop = true
                restoreState = true
            }
            closeDrawer()
        }}
        item { DrawerButton(icon = Icons.Filled.Newspaper, label = stringResource(R.string.new_entries)){
            navController.navigate(AppDestinations.NEW_ENTRY_LIST_ROUTE) {
                launchSingleTop = true
                restoreState = true
            }
            closeDrawer()
        }}
        item { DrawerButton(icon = Icons.Filled.Bookmark, label = stringResource(R.string.favorite_entries)){
            navController.navigate(AppDestinations.FAVORITE_ENTRY_LIST_ROUTE) {
                launchSingleTop = true
                restoreState = true
            }
            closeDrawer()
        }}
        item {
            val viewModel: FeedViewModel = viewModel( factory = FeedViewModel.provideFactory(repository) )
            DrawerFeedList(
                navController = navController,
                closeDrawer = closeDrawer,
                viewModel = viewModel
            )
        }
        item { DrawerButton(icon = Icons.Filled.Settings, label = stringResource(R.string.settings)){
            /*navController.navigate(AppDestinations.SETTINGS_ROUTE) {
                launchSingleTop = true
                restoreState = true
            }*/
            closeDrawer()
        }}
        item { DrawerButton(icon = Icons.Filled.Info, label = stringResource(R.string.about)){
            navController.navigate(AppDestinations.ABOUT_ROUTE) {
                launchSingleTop = true
                restoreState = true
            }
            closeDrawer()
        }}
    }
}

@Composable
fun DrawerButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    action: () -> Unit) {

    Row(modifier = modifier.clickable {
        action()
    }) {
        Icon(imageVector = icon, modifier = Modifier.padding(16.dp), contentDescription = null)
        Text(
            modifier = modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = label,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Preview
@Composable
fun AppDrawerPreview(){
    AppDataBaseHelper.onCreate(LocalContext.current)
    AppRepository.init(AppDataBaseHelper.db, NetworkMonitor(LocalContext.current))
    AppDrawer(
        navController = rememberNavController(),
        closeDrawer = { /*TODO*/ },
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
            .size(120, 120)
            .build(),
        contentDescription = null,
        modifier = modifier.size(120.dp, 120.dp),
        alignment = Alignment.CenterStart,
        contentScale = ContentScale.Crop
    )
}