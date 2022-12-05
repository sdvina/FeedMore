package org.sdvina.feedmore.ui.entry

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.sdvina.feedmore.R
import org.sdvina.feedmore.data.model.entry.EntryLite

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
            .clickable(onClick = {
                /*navController.navigate("EntryScreen/${entryLight.comicId}") {
                    launchSingleTop = true
                    restoreState = true
                }*/
            })
    ) {
    }

}

@Composable
fun EntryItemImage(imageUrl: String, modifier: Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .placeholder(R.drawable.feed_icon)
            .data(imageUrl)
            .size(120, 120)
            .build(),
        contentDescription = null,
        alignment = Alignment.CenterStart,
        contentScale = ContentScale.Crop,
        modifier = modifier.size(120.dp, 120.dp)
    )
}

@Preview
@Composable
fun EntryItemImagePreview(){
    EntryItemImage(imageUrl = "", modifier = Modifier)
}


