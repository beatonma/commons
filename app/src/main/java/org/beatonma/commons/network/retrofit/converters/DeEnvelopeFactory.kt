package org.beatonma.commons.network.retrofit.converters

import com.squareup.moshi.*
import java.lang.reflect.Type

/**
 * Based on a combination of https://github.com/android/plaid/blob/master/core/src/main/java/io/plaidapp/core/data/api/DeEnvelopingConverter.kt
 * and https://medium.com/@naturalwarren/moshi-made-simple-jsonqualifier-b99559c826ad
 */

/**
 * Indicates an endpoint wraps a response in a JSON Object.
 * When deserializing the response we should only return
 * what's inside the outer most object.
 */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
internal annotation class EnvelopePayload(val value: String = "results")

class DeEnvelopeFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        val payloadName = getPayloadName(annotations) ?: return null
        val delegateAnnotations =
            Types.nextAnnotations(annotations, EnvelopePayload::class.java) ?: return null
        val delegate = moshi.nextAdapter<Any>(this, type, delegateAnnotations)
        return EnvelopeJsonAdapter(payloadName, delegate)
    }

    private fun getPayloadName(annotations: MutableSet<out Annotation>?): String? {
        val annotation = annotations?.firstOrNull { it is EnvelopePayload }
        return if (annotation != null) {
            (annotation as EnvelopePayload).value
        } else {
            null
        }
    }

    private class EnvelopeJsonAdapter(
        val payloadName: String,
        val delegate: JsonAdapter<*>
    ): JsonAdapter<Any>() {
        override fun fromJson(reader: JsonReader): Any? {
            reader.beginObject()
            while (reader.hasNext()) {
                if (payloadName == reader.nextName()) {
                    val envelope = delegate.fromJsonValue(reader.readJsonValue())
                    reader.endObject()
                    return envelope
                }
                else {
                    reader.skipValue()
                }
            }
            reader.endObject()
            return null
        }

        override fun toJson(writer: JsonWriter, value: Any?) {
            throw UnsupportedOperationException("@EnvelopePayload is only used to deserialize objects.")
        }
    }
}
