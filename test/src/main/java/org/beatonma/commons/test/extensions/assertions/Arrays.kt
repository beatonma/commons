package org.beatonma.commons.test.extensions.assertions

import org.junit.Assert as JUnit

fun Array<*>?.assertArrayEquals(other: Any?) {
    if (other is Array<*>) {
        JUnit.assertArrayEquals(this, other)
    }
    else {
        JUnit.assertEquals(this, other)
    }
}

fun IntArray?.assertArrayEquals(other: Any?) {
    if (other is IntArray) {
        JUnit.assertArrayEquals(this, other)
    }
    else {
        JUnit.assertEquals(this, other)
    }
}

fun BooleanArray?.assertArrayEquals(other: Any?) {
    if (other is BooleanArray) {
        JUnit.assertArrayEquals(this, other)
    }
    else {
        JUnit.assertEquals(this, other)
    }
}

fun ByteArray?.assertArrayEquals(other: Any?) {
    if (other is ByteArray) {
        JUnit.assertArrayEquals(this, other)
    }
    else {
        JUnit.assertEquals(this, other)
    }
}

fun CharArray?.assertArrayEquals(other: Any?) {
    if (other is CharArray) {
        JUnit.assertArrayEquals(this, other)
    }
    else {
        JUnit.assertEquals(this, other)
    }
}

fun LongArray?.assertArrayEquals(other: Any?) {
    if (other is LongArray) {
        JUnit.assertArrayEquals(this, other)
    }
    else {
        JUnit.assertEquals(this, other)
    }
}

fun ShortArray?.assertArrayEquals(other: Any?) {
    if (other is ShortArray) {
        JUnit.assertArrayEquals(this, other)
    }
    else {
        JUnit.assertEquals(this, other)
    }
}
