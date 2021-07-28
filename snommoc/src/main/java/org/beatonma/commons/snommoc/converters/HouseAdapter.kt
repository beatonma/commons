package org.beatonma.commons.snommoc.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import org.beatonma.commons.core.House
import java.util.Locale

class HouseAdapter: JsonAdapter<House>() {
    @FromJson
    override fun fromJson(reader: JsonReader): House {
        return House.valueOf(reader.nextString().lowercase(Locale.ROOT))
    }

    override fun toJson(writer: JsonWriter, value: House?) {
        when (value) {
            null -> writer.nullValue()
            else -> writer.value(value.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            })
        }
    }
}
