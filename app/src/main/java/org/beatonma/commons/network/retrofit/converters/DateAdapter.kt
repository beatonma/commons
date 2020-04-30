package org.beatonma.commons.network.retrofit.converters

import com.squareup.moshi.*
import org.beatonma.commons.kotlin.takeFirstSuccessful
import org.beatonma.lib.util.kotlin.extensions.dump
import java.text.SimpleDateFormat
import java.util.*

private const val API_FORMAT = "yyyy-MM-dd"

class DateAdapter: JsonAdapter<Date>() {
    private val format = SimpleDateFormat(API_FORMAT, Locale.getDefault()).dump()

    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
        return takeFirstSuccessful(
            { format.parse(reader.nextString()) },
            { reader.nextNull() },
            { Date(reader.nextLong()) }
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            writer.value(value.time)
        }
        else {
            writer.nullValue()
        }
    }
}
