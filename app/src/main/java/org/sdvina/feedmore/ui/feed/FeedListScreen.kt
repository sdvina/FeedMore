package org.sdvina.feedmore.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.sdvina.feedmore.R

@Composable
fun FeedListScreen() {

}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DrawerFeedList(
    navController: NavController,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel
){
    val openedCategories = remember { emptySet<String>() }
    val viewState by viewModel.sate.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        FeedCategoryItem(
            label = stringResource(R.string.uncategorized),
            entryCount = "100"
        )
        viewState.feedLites.forEach{ feedLite ->
            FeedItem(
                icon = Icons.Filled.RssFeed,
                label = feedLite.title
            )
        }
    }
}

@Composable
fun FeedCategoryItem(
    label: String = "",
    entryCount: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
            // TODO
        }
    ){
        Icon(imageVector = Icons.Filled.ExpandLess, modifier = Modifier.padding(16.dp), contentDescription = null)
        Text(
            text = label,
            letterSpacing = 0.7.sp,
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
        )
        if (entryCount.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp),
                text = entryCount,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun FeedItem(
    icon: ImageVector,
    label: String,
    entryCount: String = ""
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
            // TODO
        }
    ) {
        Icon(imageVector = icon, modifier = Modifier.padding(16.dp), contentDescription = null)
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
        if (entryCount.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(16.dp),
                text = entryCount,
                textAlign = TextAlign.Start
            )
        }
    }
}
