package org.sdvina.feedmore.ui.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FeedListScreen() {

}

@Composable
fun FeedLiteList(){

}

@Composable
fun FeedCategoryItem(title: String = "", entryCount: String = "") {
    Row(
        modifier = Modifier.clickable {
            // TODO
        }
    ){
        Icon(imageVector = Icons.Filled.ExpandLess, contentDescription = null)
        Text(
            text = title,
            letterSpacing = 0.7.sp,
            fontSize = 12.sp,
            modifier = Modifier.padding(16.dp)
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
fun FeedItem(icon: ImageVector, title: String, entryCount: String = "") {

    Row(
        modifier = Modifier.clickable {
            // TODO
        }
    ) {
        Icon(imageVector = icon, modifier = Modifier.padding(16.dp), contentDescription = null)
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = title,
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
