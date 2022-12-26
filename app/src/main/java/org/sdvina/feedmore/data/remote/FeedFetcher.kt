package org.sdvina.feedmore.data.remote

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rometools.rome.feed.synd.SyndEnclosure
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okio.IOException
import org.jdom2.Element
import org.sdvina.feedmore.data.local.database.entity.FeedWithEntries
import org.sdvina.feedmore.data.local.database.entity.Entry
import org.sdvina.feedmore.data.local.database.entity.Feed
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class FeedFetcher {
    private var _feedWithEntriesLive = MutableLiveData<FeedWithEntries?>()
    val feedWithEntriesLive: LiveData<FeedWithEntries?> get() = _feedWithEntriesLive

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun createCall(url: String): Call {
        val request = Request.Builder()
            .url(url)
            .header("User-agent", "Mozilla/5.0 (compatible) AppleWebKit Chrome Safari")
            .addHeader("accept", "*/*")
            .build()
        return client.newCall(request)
    }

    fun requestSynchronously(url: String): FeedWithEntries? {
        val call = createCall(url)
        call.execute().use { response ->
            try{
                val stream = response.body.byteStream()
                val xmlReader = XmlReader(stream)
                val rawFeed = SyndFeedInput().build(xmlReader)
                val feed = FeedParser.fromRawFeed(url, rawFeed)
                val entries = rawFeed.entries.map { EntryParser.fromRawEntry(it) }
                return FeedWithEntries(feed, entries)
            } catch(e: Exception){
                Log.e("EXP", e.toString())
            }
            return null
        }
    }

    private fun requestAsynchronously(url: String): FeedWithEntries? {
        var feedWithEntries: FeedWithEntries? = null
        createCall(url).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val stream = response.body.byteStream()
                    val xmlReader = XmlReader(stream)
                    try{
                        val rawFeed = SyndFeedInput().build(xmlReader)
                        val feed = FeedParser.fromRawFeed(url, rawFeed)
                        val entries = rawFeed.entries.map { EntryParser.fromRawEntry(it) }
                        feedWithEntries = FeedWithEntries(feed, entries)
                    } catch(e: Exception){
                        Log.e("EXP", e.toString())
                    }
                }
            }
        })
        return feedWithEntries
    }

    suspend fun request(url: String) {
        try {
            val feedWithEntries = withContext(Dispatchers.IO) { requestSynchronously(url) }
            _feedWithEntriesLive.postValue(feedWithEntries)
        } catch(e: Exception) {
            _feedWithEntriesLive.postValue(null)
        }
    }

    fun cancel() {
        _feedWithEntriesLive = MutableLiveData<FeedWithEntries?>(null)
    }

     private object FeedParser {
        fun fromRawFeed(url: String, rawFeed: SyndFeed): Feed {
            return Feed(
                url = url,
                title = rawFeed.title,
                website = rawFeed.link,
                description = rawFeed.description,
                imageUrl = rawFeed.image?.url,
                unreadCount = rawFeed.entries.size
            )
        }
    }

    private object EntryParser {
        fun fromRawEntry(rawEntry: SyndEntry): Entry {
            val content: String? = if (rawEntry.contents.size != 0)
                rawEntry.contents?.joinToString { it.value }.toString()
                else rawEntry.description?.value

            return Entry(
                url = rawEntry.link,
                title = rawEntry.title,
                website = rawEntry.source?.link ?: rawEntry.link,
                author = rawEntry.author,
                date = rawEntry.updatedDate ?: rawEntry.publishedDate,
                content = content,
                imageUrl = parseImageFromEnclosures(rawEntry.enclosures)
                    ?: parseImageFromForeignMarkup(rawEntry.foreignMarkup)
                    ?: parseImageFromContent(content)
            )
        }

        private fun parseImageFromEnclosures(enclosures: List<SyndEnclosure>): String? {
            enclosures.forEach { enclosure ->
                if (enclosure.type.contains("image")) {
                    return enclosure.url
                }
            }

            return null
        }

        private fun parseImageFromForeignMarkup(elements: List<Element>): String? {
            elements.forEach { element ->
                if (element.namespace?.prefix == "media" && element.name == "content") {
                    element.attributes.forEach { attr ->
                        if (attr.name == "url") {
                            return attr.value
                        }
                    }
                }
            }
            return null
        }

        private fun parseImageFromContent(content: String?): String? {
            // Find HTML image tag.
            val img = content
                ?.substringAfter("<img", "")
                ?.substringBefore("/>", "")
                ?: ""
            return if (img.isNotEmpty()) img.substringAfter("src=\"").substringBefore("\"")
                else null
        }
    }

    private fun cancelCallOnSchedule(call: Call, delay: Long, unit: TimeUnit){
        val executor = Executors.newScheduledThreadPool(1)
        val startNanos = System.nanoTime()
        executor.schedule({
            System.out.printf("%.2f Canceling call.%n", (System.nanoTime() - startNanos) / 1e9f)
            call.cancel()
            System.out.printf("%.2f Canceled call.%n", (System.nanoTime() - startNanos) / 1e9f)
        }, delay, unit)
    }

    companion object {
        const val FLAG_EXCERPT = "org.sdvina.feedmore.excerpt "
        const val TAG = "FeedFetcher"
    }
}