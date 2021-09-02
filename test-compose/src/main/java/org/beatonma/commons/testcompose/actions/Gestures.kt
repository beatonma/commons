package org.beatonma.commons.testcompose.actions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.GestureScope
import androidx.compose.ui.test.center
import androidx.compose.ui.test.swipe

fun GestureScope.swipeUp(distance: Float, start: Offset = center, durationMillis: Long = 200) {
    val end = Offset(start.x, start.y - distance)
    println("swipeUp($distance) $start -> $end")
    swipe(start, end, durationMillis)
}

fun GestureScope.swipeDown(distance: Float, start: Offset = center, durationMillis: Long = 200) {
    val end = Offset(start.x, start.y + distance)
    println("swipeDown($distance) $start -> $end")
    swipe(start, end, durationMillis)
}

fun GestureScope.swipeLeft(distance: Float, start: Offset = center, durationMillis: Long = 200) {
    val x = start.x - distance
    val end = Offset(x, start.y)
    println("swipeLeft($distance) $start -> $end")
    swipe(start, end, durationMillis)
}

fun GestureScope.swipeRight(distance: Float, start: Offset = center, durationMillis: Long = 200) {
    val x = start.x + distance
    val end = Offset(x, start.y)
    println("swipeRight($distance) $start -> $end")
    swipe(start, end, durationMillis)
}
