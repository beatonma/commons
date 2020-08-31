package org.beatonma.commons.network.retrofit.converters

import com.squareup.moshi.*
import org.beatonma.commons.core.takeFirstSuccessful
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

// e.g. "2020-06-04T18:26:38.380651Z"
private val DATETIME_FORMAT = DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    .appendLiteral('Z')
    .toFormatter(Locale.ROOT)

class DateTimeAdapter: JsonAdapter<LocalDateTime>() {
    @FromJson
    override fun fromJson(reader: JsonReader): LocalDateTime? {
        return takeFirstSuccessful(
            { LocalDateTime.parse(reader.nextString(), DATETIME_FORMAT) },
            { reader.nextNull() },
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        if (value != null) {
            writer.value(value.toString())
        }
        else {
            writer.nullValue()
        }
    }
}
