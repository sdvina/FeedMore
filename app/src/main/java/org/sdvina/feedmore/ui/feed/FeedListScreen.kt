package org.sdvina.feedmore.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.model.feed.FeedLite
import org.sdvina.feedmore.ui.AppDestinations



@Composable
fun ManageableFeedList(
    navController: NavController,
) {

}



@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DrawerFeedList(
    navController: NavController,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel
){
    val viewState by viewModel.sate.collectAsStateWithLifecycle()
    val feedLites = viewState.feedLites
    val defaultCategory = "Uncategorized"
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val categories = mutableSetOf<String>()
        feedLites.forEach { if(it.category != defaultCategory) categories.add(it.category) }
        val defaultSection = feedLites.filter { it.category == defaultCategory }
        val otherSection = feedLites.filter { it.category != defaultCategory }
        if(defaultSection.isNotEmpty()){
            var sectionUnreadCount = 0
            defaultSection.forEach { sectionUnreadCount += it.unreadCount }
            FeedSection(
                navController = navController,
                closeDrawer = closeDrawer,
                label = stringResource(R.string.uncategorized),
                unreadCount = sectionUnreadCount,
                section = defaultSection
            )
        }
        categories.forEach { category ->
            val section = otherSection.filter { it.category == category }
            var sectionUnreadCount = 0
            section.forEach { sectionUnreadCount += it.unreadCount }
            FeedSection (
                navController = navController,
                closeDrawer = closeDrawer,
                label = category,
                unreadCount = sectionUnreadCount,
                section = section
            )
        }
    }
}

@Composable
fun FeedSection(
    navController: NavController,
    closeDrawer: () -> Unit,
    label: String = "",
    unreadCount: Int,
    section: List<FeedLite>
) {
    var isOpen by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isOpen = !isOpen }
    ){
        Icon(imageVector = when(isOpen) {
            true -> Icons.Filled.ExpandLess
            false -> Icons.Filled.ExpandMore
        },
            modifier = Modifier.padding(16.dp),
            contentDescription = null)
        Text(
            text = label,
            letterSpacing = 0.7.sp,
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
        )
        if (!isOpen && unreadCount != 0) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp),
                text = unreadCount.toString(),
                textAlign = TextAlign.Start
            )
        }
    }
    if(isOpen) {
        section.forEach {
            FeedItem(
                navController = navController,
                closeDrawer = closeDrawer,
                imageUrl = it.imageUrl,
                label = it.category,
                unreadCount = it.unreadCount
            )
        }
    }
}

@Composable
fun FeedItem(
    navController: NavController,
    closeDrawer: () -> Unit,
    imageUrl: String?,
    label: String,
    unreadCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(AppDestinations.ENTRY_LIST_ROUTE) {
                    launchSingleTop = true
                    restoreState = true
                }
                closeDrawer()
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .placeholder(R.drawable.feed_icon_small)
                .data(imageUrl)
                .size(18, 18)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp, 18.dp)
                .padding(16.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = label,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.Start
        )
        if (unreadCount != 0) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp),
                text = unreadCount.toString(),
                textAlign = TextAlign.Start
            )
        }
    }
}