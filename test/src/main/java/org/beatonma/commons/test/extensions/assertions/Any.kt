package org.beatonma.commons.test.extensions.assertions

import kotlin.reflect.KClass
import org.junit.Assert as JUnit


internal class NullReceiverException(
        methodName: String, message: String?
) : AssertionError("[$message] Receiver is null on method '$methodName'")

fun Boolean.assertTrue(message: String? = null) = JUnit.assertTrue(message, this)

/**
 * Convenience for [JUnit.assertNull]([this])
 */
fun Any?.assertNull(message: String? = null) {
    JUnit.assertNull(message, this)
}

/**
 * Convenience for assertNotNull([this])
 */
fun Any?.assertNotNull(message: String? = null) {
    JUnit.assertNotNull(message, this)
}

fun Nothing?.assertNotNull(message: String? = null) {
    JUnit.assertNotNull(message, this)
}

/**
 * Convenience for assertEquals([expected], [this])
 */
fun Any?.assertEquals(expected: Any?, message: String? = null) {
    JUnit.assertEquals(message, expected, this)
}

/**
 * Convenience for assertNotEquals(unexpected, [this])
 */
fun Any?.assertNotEquals(unexpected: Any?, message: String? = null) {
    JUnit.assertNotEquals(message, unexpected, this)
}

/**
 * Assert that the receiver is an instance of the given Kotlin class
 */
fun Any?.assertInstanceOf(cls: KClass<*>, message: String? = "$this should be an instance of $cls") {
    JUnit.assertTrue(message, cls.isInstance(this))
}

/**
 * Assert that the receiver is an instance of the given Java class
 */
fun Any?.assertInstanceOf(cls: Class<*>, message: String? = "$this should be an instance of $cls") {
    JUnit.assertTrue(message, cls.isInstance(this))
}

/**
 * Assert that the receiver is not an instance of the given Kotlin class
 */
fun Any?.assertNotInstanceOf(cls: KClass<*>, message: String? = "$this should not be an instance of $cls") {
    JUnit.assertFalse(message, cls.isInstance(this))
}

/**
 * Assert that the receiver is not an instance of the given Java class
 */
fun Any?.assertNotInstanceOf(
    cls: Class<*>,
    message: String? = "$this should not be an instance of $cls"
) {
    JUnit.assertFalse(message, cls.isInstance(this))
}


infix fun <T> T.shouldbe(expected: T?) = JUnit.assertEquals(expected, this)
infix fun <T> Array<T>.shouldbe(expected: Array<T>) = JUnit.assertArrayEquals(expected, this)
infix fun <T> T.shouldNotBe(expected: T?) = JUnit.assertNotEquals(expected, this)
infix fun <T> T.shouldBeInstanceOf(cls: KClass<*>) = JUnit.assertTrue(cls.isInstance(this))
