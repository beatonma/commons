package org.beatonma.commons.network.retrofit.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import org.beatonma.commons.data.core.room.entities.member.House
import java.util.*

class HouseAdapter: JsonAdapter<House>() {
    @FromJson
    override fun fromJson(reader: JsonReader): House? {
        return House.valueOf(reader.nextString().toLowerCase(Locale.ROOT))
    }

    override fun toJson(writer: JsonWriter, value: House?) {
        when (value) {
            null -> writer.nullValue()
            else -> writer.value(value.name.capitalize(Locale.ROOT))
        }
    }
}
