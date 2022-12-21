package org.sdvina.feedmore.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import org.sdvina.feedmore.data.model.feed.FeedManageable
import org.sdvina.feedmore.util.extensions.sortedByCategory
import com.rometools.opml.feed.opml.Opml
import com.rometools.opml.feed.opml.Outline
import com.rometools.opml.io.impl.OPML20Generator
import com.rometools.rome.io.WireFeedOutput
import java.io.OutputStreamWriter
import java.net.URL
import java.util.*

class OpmlExporter(
    context: Context,
    private val onExportAttempted: (isSuccessful: Boolean, fileName: String?) -> Unit
) {

    private val contentResolver = context.contentResolver
    private var feeds = listOf<FeedManageable>()
        get() = field.sortedByCategory()
    private var categories = arrayOf<String>()

    fun submitFeeds(feeds: List<FeedManageable>) {
        this.feeds = feeds
        categories = feeds.map { feed -> feed.category }.toSet().toTypedArray()
    }

    fun executeExport(uri: Uri) {
        val outputStream = contentResolver.openOutputStream(uri)
        if (outputStream == null) onExportAttempted(false, null)
        if (outputStream != null) {
            try {
                OutputStreamWriter(outputStream, Charsets.UTF_8).use { writer ->
                    if (feeds.isNotEmpty()) writeOpml(writer)
                }

                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                        onExportAttempted(true, fileName)
                    }
                }
            } catch (e: Exception) { onExportAttempted(false, null) }
        }
    }

    private fun writeOpml(writer: OutputStreamWriter) {
        val opml = Opml().apply {
            feedType = OPML20Generator().type
            encoding = "utf-8"
            created = Date()
            outlines = categories.map { category ->
                Outline(category, null, null).apply {
                    children = feeds.filter { feed ->
                        feed.category == category
                    }.map { feed ->
                        Outline(feed.title, URL(feed.url), URL(feed.website))
                    }
                }
            }
        }

        WireFeedOutput().output(opml, writer)
    }
}