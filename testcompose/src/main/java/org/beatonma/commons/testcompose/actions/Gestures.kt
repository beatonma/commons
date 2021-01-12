package org.beatonma.commons.testcompose.actions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.GestureScope
import androidx.compose.ui.test.center
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.milliseconds

fun GestureScope.swipeUp(distance: Float, start: Offset = center, durationMillis: Int = 200) {
    val end = Offset(start.x, start.y - distance)
    println("swipeUp($distance) $start -> $end")
    swipe(start, end, durationMillis.milliseconds)
}

fun GestureScope.swipeDown(distance: Float, start: Offset = center, durationMillis: Int = 200) {
    val end = Offset(start.x, start.y + distance)
    println("swipeDown($distance) $start -> $end")
    swipe(start, end, durationMillis.milliseconds)
}

fun GestureScope.swipeLeft(distance: Float, start: Offset = center, durationMillis: Int = 200) {
    val x = start.x - distance
    val end = Offset(x, start.y)
    println("swipeLeft($distance) $start -> $end")
    swipe(start, end, durationMillis.milliseconds)
}

fun GestureScope.swipeRight(distance: Float, start: Offset = center, durationMillis: Int = 200) {
    val x = start.x + distance
    val end = Offset(x, start.y)
    println("swipeRight($distance) $start -> $end")
    swipe(start, end, durationMillis.milliseconds)
}
