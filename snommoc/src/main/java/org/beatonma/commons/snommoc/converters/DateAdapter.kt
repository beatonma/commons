package org.beatonma.commons.snommoc.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.time.LocalDate

private const val API_FORMAT = "yyyy-MM-dd"

class DateAdapter: JsonAdapter<LocalDate>() {

    @FromJson
    override fun fromJson(reader: JsonReader): LocalDate? {
        return takeFirstSuccessful(
            { LocalDate.parse(reader.nextString()) },
            { reader.nextNull() },
            { LocalDate.ofEpochDay(reader.nextLong()) }
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        when (value) {
            null -> writer.nullValue()
            else -> writer.value(value.toString())
        }
    }
}
