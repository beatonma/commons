package org.beatonma.commons.ukparliament.converters

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.beatonma.commons.core.extensions.fastForEach
import java.lang.reflect.Type


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
internal annotation class NestedPayload(vararg val path: String = ["result", "items"])

/**
 * Extract a nested object from some parent structure.
 * e.g.
 * {
 *     ...
 *     "result": {
 *          ...
 *          "items": [
 *              // This is the stuff we want
 *          ]
 *     }
 *
 */
class NestedPayloadFactory: JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi,
    ): JsonAdapter<*>? {
        val payload = getPayloadPath(annotations) ?: return null
        val delegateAnnotations =
            Types.nextAnnotations(annotations, NestedPayload::class.java) ?: return null
        val delegate = moshi.nextAdapter<Any>(this, type, delegateAnnotations)
        return NestedPayloadAdapter(payload, delegate)
    }

    private fun getPayloadPath(annotations: MutableSet<out Annotation>?): Array<out String>? {
        return (annotations?.firstOrNull { it is NestedPayload } as? NestedPayload)?.path
    }

    private class NestedPayloadAdapter(
        val payloadPath: Array<out String>,
        val delegate: JsonAdapter<*>,
    ): JsonAdapter<Any>() {
        override fun fromJson(reader: JsonReader): Any? {
            return findTarget(payloadPath, reader)
        }

        override fun toJson(writer: JsonWriter, value: Any?) {
            throw UnsupportedOperationException("@WrappedPayload is only used to deserialize objects.")
        }

        private fun findTarget(path: Array<out String>, reader: JsonReader): Any? {
            if (path.size == 1) {
                return findTarget(path[0], reader)
            }

            var result: Any? = null
            path.fastForEach { name ->
                reader.jsonObject {
                    while (hasNext()) {
                        if (nextName() == name) {
                            result = findTarget(
                                path.tail(),
                                reader = this
                            )
                        } else {
                            skipValue()
                        }
                    }
                }

                if (result != null) {
                    return result
                }
            }
            return null
        }

        private fun findTarget(name: String, reader: JsonReader): Any? {
            var result: Any? = null
            reader.jsonObject {
                while (hasNext()) {
                    if (name == nextName()) {
                        result = delegate.fromJsonValue(readJsonValue())
                    } else {
                        skipValue()
                    }
                }
            }
            return result
        }
    }
}

private inline fun JsonReader.jsonObject(block: JsonReader.() -> Unit) {
    beginObject()
    block()
    endObject()
}

private inline fun <reified T> Array<out T>.tail(): Array<out T> =
    if (size == 1) arrayOf()
    else this.sliceArray(1 until size)
