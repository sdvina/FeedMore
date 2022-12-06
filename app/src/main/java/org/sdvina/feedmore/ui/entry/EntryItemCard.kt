package org.sdvina.feedmore.ui.entry

import android.content.Context
import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.model.entry.EntryLite
import org.sdvina.feedmore.ui.components.BookmarkButton
import org.sdvina.feedmore.ui.theme.FeedMoreTheme
import java.util.Date

@Composable
fun EntryItemCard(
    entryItem: EntryLite,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.elevatedCardColors(),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(120.dp)
            .clickable(onClick = {
                /*navController.navigate("EntryScreen/${entryLight.comicId}") {
                    launchSingleTop = true
                    restoreState = true
                }*/
            })
    ) {
        ConstraintLayout( modifier = Modifier.fillMaxWidth()) {
            var headerStr = "-${entryItem.website}"
            entryItem.date?.let{
                headerStr = when(DateUtils.isToday(it.time)){
                    true -> DateUtils.formatDateTime(LocalContext.current, it.time, DateUtils.FORMAT_SHOW_TIME) + headerStr
                    false -> DateUtils.formatDateTime(LocalContext.current, it.time, DateUtils.FORMAT_SHOW_DATE) + headerStr
                }
            }
            val (image, header, title, bookmark) = createRefs()
            EntryItemImage(entryItem.imageUrl, Modifier.constrainAs(image) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })
            EntryItemHeader(headerStr, Modifier.constrainAs(header){
                start.linkTo(image.end, 4.dp)
                end.linkTo(bookmark.start)
                top.linkTo(parent.top)
                bottom.linkTo(title.top)
            })
            BookmarkButton(entryItem.isFavorite, Modifier.constrainAs(bookmark){
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(title.top)
            }, onClick = { /*TODO*/ })
            EntryItemTitle(entryItem.title, Modifier.constrainAs(title){
                start.linkTo(image.end)
                end.linkTo(parent.end)
                top.linkTo(header.bottom)
            })
        }
    }

}

@Composable
fun EntryItemImage(imageUrl: String?, modifier: Modifier = Modifier) {

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .placeholder(R.drawable.feed_icon)
            .data(imageUrl)
            .size(120, 120)
            .build(),
        contentDescription = null,
        modifier = modifier.size(120.dp, 120.dp),
        alignment = Alignment.CenterStart,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun EntryItemHeader(content: String, modifier: Modifier = Modifier){
    Text(
        text = content,
        modifier = modifier,
        textAlign = TextAlign.Left,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun EntryItemTitle(content: String, modifier: Modifier = Modifier){
    Text(
        text = content,
        modifier = modifier,
        textAlign = TextAlign.Left,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

//@Preview
@Composable
fun EntryItemImagePreview(){
    EntryItemImage(imageUrl = "", modifier = Modifier)
}

@Preview
@Composable
fun EntryItemCardPreview(){
    FeedMoreTheme {
        EntryItemCard(entryItem = EntryLite(
            url = "xxx",
            title = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
            website = "https://www.zhihu.com",
            date = Date(),
            imageUrl = "",
            isFavorite = true,
            isRead = false
        ))
    }
}


