package org.sdvina.feedmore.util.extensions

import org.sdvina.feedmore.data.local.database.entity.Entry
import org.sdvina.feedmore.data.model.entry.EntryLite


fun List<Entry>.sortedByDate() = this.sortedByDescending { it.date }

@JvmName("sortedByDateEntryLight")
fun List<EntryLite>.sortedByDate() = this.sortedByDescending { it.date }

fun List<EntryLite>.sortedUnreadOnTop() = this.sortedByDate().sortedBy { it.isRead }