package org.beatonma.commons.testcompose.actions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.swipe

fun TouchInjectionScope.swipeUp(
    distance: Float,
    start: Offset = center,
    durationMillis: Long = 200,
) {
    val end = Offset(start.x, start.y - distance)
    println("swipeUp($distance) $start -> $end")
    swipe(start, end, durationMillis)
}

fun TouchInjectionScope.swipeDown(
    distance: Float,
    start: Offset = center,
    durationMillis: Long = 200,
) {
    val end = Offset(start.x, start.y + distance)
    println("swipeDown($distance) $start -> $end")
    swipe(start, end, durationMillis)
}

fun TouchInjectionScope.swipeLeft(
    distance: Float,
    start: Offset = center,
    durationMillis: Long = 200,
) {
    val x = start.x - distance
    val end = Offset(x, start.y)
    println("swipeLeft($distance) $start -> $end")
    swipe(start, end, durationMillis)
}

fun TouchInjectionScope.swipeRight(
    distance: Float,
    start: Offset = center,
    durationMillis: Long = 200,
) {
    val x = start.x + distance
    val end = Offset(x, start.y)
    println("swipeRight($distance) $start -> $end")
    swipe(start, end, durationMillis)
}
