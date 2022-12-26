package org.sdvina.feedmore.util

import android.content.Context
import android.net.Uri
import com.rometools.opml.feed.opml.Opml
import com.rometools.rome.io.WireFeedInput
import org.sdvina.feedmore.data.local.database.entity.Feed
import java.io.BufferedInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringReader

class OpmlImporter(
    context: Context,
    private val onOpmlParsed: (isSuccessful: Boolean, feeds: List<Feed>?) -> Unit
) {
    private val contentResolver = context.contentResolver

    fun submitUri(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream == null)  onOpmlParsed(false, null)
        if (inputStream != null) {
            try {
                InputStreamReader(inputStream).use { reader -> parseOpml(reader) }
            } catch (e: Exception) {
                try {
                    val content = BufferedInputStream(inputStream).bufferedReader().readText()
                    val fixedReader = StringReader(content.replace(
                        "<opml version=['\"][0-9]\\.[0-9]['\"]>".toRegex(),
                        "<opml>"
                    ))
                    parseOpml(fixedReader)
                } catch (e: Exception) { onOpmlParsed(false, null) }
            }
        }
    }

    private fun parseOpml(opmlReader: Reader) {
        val feeds = mutableListOf<Feed>()
        val opml = WireFeedInput().build(opmlReader) as Opml

        for (outline in opml.outlines) {
            if(outline.xmlUrl == null) {
                if (outline.children.isNotEmpty()) {
                    for (child in outline.children.filterNot { it.xmlUrl.isNullOrEmpty() }) {
                        val feed = Feed(
                            url = child.xmlUrl,
                            website = child.htmlUrl ?: child.xmlUrl,
                            title = child.title ?: child.xmlUrl.substringAfter("://"),
                            category = outline.title,
                            unreadCount = 0
                        )
                        feeds.add(feed)
                    }
                }
            }
            if (outline.xmlUrl != null) {
                val feed = Feed(
                    url = outline.xmlUrl,
                    website = outline.htmlUrl ?: outline.url ?: "",
                    title = outline.title,
                    unreadCount = 0
                )
                feeds.add(feed)
            }
        }
        onOpmlParsed(true, feeds)
    }
}