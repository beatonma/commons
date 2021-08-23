package org.beatonma.commons.app.data.parse

import androidx.annotation.WorkerThread
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.LatLngBounds
import com.google.android.libraries.maps.model.PolygonOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.*
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

private const val PLACEMARK = "Placemark"
private const val MULTIGEOMETRY = "MultiGeometry"
private const val POLYGON = "Polygon"
private const val COORDINATES = "coordinates"


class Geometry {
    private val boundaryBuilder: LatLngBounds.Builder = LatLngBounds.builder()
    val polygons: MutableList<PolygonOptions> = mutableListOf()
    val boundary: LatLngBounds by lazy {
        boundaryBuilder.build()
    }

    fun addNewPolygon(latlong: List<LatLng>) {
        latlong.forEach { latlng -> boundaryBuilder.include(latlng) }
        polygons.add(PolygonOptions().apply {
            addAll(latlong)
        })
    }

    fun setStyle(fillColor: Int, strokeColor: Int, strokeWidth: Float) {
        polygons.forEach { polygon ->
            polygon.fillColor(fillColor)
                .strokeColor(strokeColor)
                .strokeWidth(strokeWidth)
                .visible(true)
        }
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
class KmlParser {
    val geometry = Geometry()

    @WorkerThread
    @Throws(XmlPullParserException::class, NumberFormatException::class)
    private fun createXmlParser(stream: InputStream): XmlPullParser =
        XmlPullParserFactory.newInstance().apply{
            isNamespaceAware = true
        }
            .newPullParser().apply {
                setInput(stream, null)
            }

    @Throws(XmlPullParserException::class, NumberFormatException::class)
    @WorkerThread
    suspend fun parse(stream: InputStream): Geometry {
        return parse(createXmlParser(stream))
    }

    @WorkerThread
    private suspend fun parse(parser: XmlPullParser): Geometry {
        return withContext(Dispatchers.IO) {
            var eventType: Int = parser.eventType
            while (eventType != END_DOCUMENT) {
                if (eventType == START_TAG) {
                    when (parser.name) {
                        PLACEMARK -> parsePlacemark(parser)
                    }
                }
                eventType = parser.next()
            }
            geometry
        }
    }

    @WorkerThread
    private suspend fun parsePlacemark(parser: XmlPullParser) {
        var eventType: Int = parser.eventType
        while (eventType != END_TAG || parser.name != PLACEMARK) {
            if (eventType == START_TAG) {
                when (parser.name) {
                    MULTIGEOMETRY -> parseMultiGeometry(parser)
                    POLYGON -> parsePolygon(parser)
                }
            }
            eventType = parser.next()
        }
    }

    @WorkerThread
    private suspend fun parseMultiGeometry(parser: XmlPullParser) {
        var eventType: Int = parser.eventType
        while (eventType != END_TAG || parser.name != MULTIGEOMETRY) {
            if (eventType == START_TAG) {
                when (parser.name) {
                    POLYGON -> parsePolygon(parser)
                }
            }

            eventType = parser.next()
        }
    }

    @WorkerThread
    private suspend fun parsePolygon(parser: XmlPullParser) {
        var eventType: Int = parser.eventType
        while (eventType != END_TAG || parser.name != POLYGON) {
            when (parser.name) {
                COORDINATES -> {
                    geometry.addNewPolygon(parseLatLong(parser.nextText()))
                }
            }
            eventType = parser.next()
        }
    }
}

@Throws(NumberFormatException::class)
@WorkerThread
private suspend fun parseLatLong(text: String): List<LatLng> {
    val coords = text.split(' ')
    return coords.map { c ->
        val (lng, lat) = c.split(',')
        LatLng(lat.toDouble(), lng.toDouble())
    }
}

private fun XmlPullParser.skip() {
    if (eventType != START_TAG) {
        throw IllegalStateException()
    } else {
        var depth = 1
        while (depth != 0) {
            when(next()) {
                START_TAG -> ++depth
                END_TAG -> --depth
            }
        }
    }
}
