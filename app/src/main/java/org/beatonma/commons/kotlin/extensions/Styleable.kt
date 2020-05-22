@file:JvmName("Styleable")
package org.beatonma.commons.kotlin.extensions

import android.content.Context
import android.content.res.TypedArray
import androidx.annotation.*

/**
 * Extensions for [TypedArray] to simplify reading of values that may be either linked as
 * resources or given as literals
 */

/**
 * Read either a boolean resource or a literal boolean value
 */
fun TypedArray.boolean(context: Context, @StyleableRes @BoolRes styleable: Int, default: Boolean): Boolean {
    val resId = getResourceId(styleable, 0)
    return when (resId) {
        0 -> getBoolean(styleable, default)
        else -> context.resources.getBoolean(styleable)
    }
}

/**
 * Read either a @dimen resource or a literal dp value
 */
fun TypedArray.dimen(context: Context, @StyleableRes @DimenRes styleable: Int, default: Float = 0F): Float {
    val resId = getResourceId(styleable, 0)
    return when (resId) {
        0 -> getDimension(styleable, default)
        else -> context.resources.getDimension(resId)
    }
}

/**
 * Read either a @color resource or a literal color value
 */
fun TypedArray.color(context: Context, @StyleableRes @ColorRes styleable: Int, default: Int = 0): Int {
    val resId = getResourceId(styleable, 0)
    return when (resId) {
        0 -> getColor(styleable, default)
        else -> context.colorCompat(resId)
    }
}


/**
 * Read either an integer resource or a literal integer value
 */
fun TypedArray.int(context: Context, @StyleableRes @IntegerRes styleable: Int, default: Int = 0): Int {
    val resId = getResourceId(styleable, 0)
    return when (resId) {
        0 -> getInteger(styleable, default)
        else -> context.resources.getInteger(styleable)
    }
}

/**
 * Same as int(), cast to Long.
 */
fun TypedArray.long(context: Context, @StyleableRes @IntegerRes styleable: Int, default: Int = 0): Long {
    return int(context, styleable, default).toLong()
}


/**
 * Read either a @string resource or a literal string value
 */
fun TypedArray.string(context: Context, @StyleableRes @StringRes styleable: Int, vararg formatArgs: String): String? {
    val resId = getResourceId(styleable, 0)
    return when (resId) {
        0 -> getString(styleable)
        else -> context.stringCompat(resId, *formatArgs)
    }
}


/**
 * Read either a @string resource or a literal string value, maintaining html styling tags
 */
fun TypedArray.text(context: Context, @StyleableRes @StringRes styleable: Int, default: CharSequence): CharSequence? {
    val resId = getResourceId(styleable, 0)
    return when (resId) {
        0 -> getText(styleable)
        else -> context.resources.getText(resId, default)
    }
}

/**
 * Read either a string-array resource or a literal string array value, maintaining html styling tags
 */
fun TypedArray.textArray(context: Context, @StyleableRes @ArrayRes styleable: Int): Array<CharSequence?> {
    val resId = getResourceId(styleable, 0)
    return when (resId) {
        0 -> getTextArray(styleable)
        else -> context.resources.getTextArray(resId)
    }
}


inline fun <reified T: Enum<T>> TypedArray.enum(context: Context, @StyleableRes @IntegerRes styleable: Int, default: T): T {
    val value = int(context, styleable)
    return try {
        enumValues<T>()[value]
    } catch (e: Exception) {
        default
    }
}
