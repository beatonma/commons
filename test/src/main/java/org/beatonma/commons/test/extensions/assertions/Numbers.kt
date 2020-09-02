package org.beatonma.commons.test.extensions.assertions

import org.junit.Assert as JUnit

/**
 * Convenience for assertTrue([this] > [value])
 */
fun Int?.assertGreaterThan(value: Int, message: String? = null) {
    this?.let { JUnit.assertTrue(message, this > value) }
            ?: throw NullReceiverException("Int.assertGreaterThan", message)
}

/**
 * Convenience for assertTrue([this] > [value])
 */
fun Float?.assertGreaterThan(value: Float, message: String? = null) {
    this?.let { JUnit.assertTrue(message, this > value) }
            ?: throw NullReceiverException("Float.assertGreaterThan", message)
}

/**
 * Convenience for assertTrue([this] < [value])
 */
fun Int?.assertLessThan(value: Int, message: String? = null) {
    this?.let { JUnit.assertTrue(message, this < value) }
            ?: throw NullReceiverException("Int.assertLessThan", message)
}


/**
 * Convenience for assertTrue([this] < [value])
 */
fun Float?.assertLessThan(value: Float, message: String? = null) {
    this?.let { JUnit.assertTrue(message, this < value) }
            ?: throw NullReceiverException("Float.assertLessThan", message)
}

/**
 * Convenience for assertTrue([this] >= [value])
 */
fun Int?.assertGreaterThanOrEquals(value: Int, message: String? = null) {
    this?.let { JUnit.assertTrue(message, this >= value) }
            ?: throw NullReceiverException("Int.assertGreaterThanOrEquals", message)
}

/**
 * Convenience for assertTrue([this] >= [value])
 */
fun Float?.assertGreaterThanOrEquals(value: Float, message: String? = null) {
    this?.let { JUnit.assertTrue(message, this >= value) }
            ?: throw NullReceiverException("Float.assertGreaterThanOrEquals", message)
}

/**
 * Convenience for assertTrue([this] <= [value])
 */
fun Int?.assertLessThanOrEquals(value: Int, message: String? = null) {
    this?.let { JUnit.assertTrue(message, this < value) }
            ?: throw NullReceiverException("Int.assertLessThanOrEquals", message)
}


/**
 * Convenience for assertTrue([this] <= [value])
 */
fun Float?.assertLessThanOrEquals(value: Float, message: String? = null) {
    this?.let { JUnit.assertTrue(message, this < value) }
            ?: throw NullReceiverException("Float.assertLessThanOrEquals", message)
}

/**
 * Assert that the value is between min and max and not equal to either of those values
 * i.e. [min] < [this] < [max]
 */
fun Int?.assertBetweenExclusive(min: Int, max: Int, message: String? = null) {
    this?.let {
        JUnit.assertTrue(message, this > min)
        JUnit.assertTrue(message, this < max)
    } ?: throw NullReceiverException("Int.assertBetweenExclusive", message)
}

/**
 * Assert that the value is between min and max, or equal to one of those values
 * i.e. [min] <= [this] <= [max]
 */
fun Int?.assertBetweenInclusive(min: Int, max: Int, message: String? = null) {
    this?.let {
        JUnit.assertTrue(message, this >= min)
        JUnit.assertTrue(message, this <= max)
    } ?: throw NullReceiverException("Int.assertBetweenInclusive", message)
}

/**
 * Assert that the value is between min and max and not equal to either of those values
 * i.e. [min] < [this] < [max]
 */
fun Float?.assertBetweenExclusive(min: Float, max: Float, message: String? = null) {
    this?.let {
        JUnit.assertTrue(message, this > min)
        JUnit.assertTrue(message, this < max)
    } ?: throw NullReceiverException("Float.assertBetweenExclusive", message)
}

/**
 * Assert that the value is between min and max, or equal to one of those values
 * i.e. [min] <= [this] <= [max]
 */
fun Float?.assertBetweenInclusive(min: Float, max: Float, message: String? = null) {
    this?.let {
        JUnit.assertTrue(message, this >= min)
        JUnit.assertTrue(message, this <= max)
    } ?: throw NullReceiverException("Float.assertBetweenInclusive", message)
}

/**
 * Assert that the value is in the range (actual - fuzz)..(actual + fuzz) \[inclusive\]
 * Useful for testing pixel values which may have undergone rounding at render time
 */
fun Int?.assertFuzzyEquals(expected: Int, fuzz: Int = 1, message: String? = null) {

    if (expected == this) {
        JUnit.assertEquals(message, expected, this)
    } else {
        this?.let {
            JUnit.assertTrue("[expected $expected, actual $this] $message", this.fuzzyEquals(expected, fuzz = fuzz))
            println("(actual) $this != $expected (expected) but is within acceptable range (+/-$fuzz inclusive)")
        } ?: throw NullReceiverException("assertFuzzyEquals", message)
    }
}

/**
 * Not an assertion but may be used
 */
fun Int.fuzzyEquals(expected: Int, fuzz: Int = 1): Boolean =
        this >= expected - fuzz && this <= expected + fuzz


fun Float.assertFuzzyEquals(expected: Float, fuzz: Float = .025F, message: String? = null) {
    JUnit.assertEquals(message, expected,this, fuzz)
}
