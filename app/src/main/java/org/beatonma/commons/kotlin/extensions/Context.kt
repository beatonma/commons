package org.beatonma.commons.kotlin.extensions

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.device.Sdk

private const val DEFAULT_PREFS = "prefs"

/**
 * Pixel value for 1dp on current device
 */
val Context.dp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1F, resources.displayMetrics)

/**
 * Return the pixel value of the given dp value
 */
fun Context?.dp(value: Float = 1F): Float {
    if (this == null) return 0F
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics)
}

fun Context?.dp(value: Int = 1): Int = dp(value.toFloat()).toInt()

fun Context.getPrefs(
    name: String = DEFAULT_PREFS,
    mode: Int = Context.MODE_PRIVATE,
): SharedPreferences = getSharedPreferences(name, mode)

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text.trim(), duration).show()
}

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

@Suppress("DEPRECATION")
fun Context?.stringCompat(@StringRes resId: Int, vararg formatArgs: Any?): String =
    when {
        this == null -> ""
        Sdk.isMarshmallow -> getString(resId, *formatArgs)
        else -> resources.getString(resId, *formatArgs)
    }

@Suppress("DEPRECATION")
@ColorInt
fun Context?.colorCompat(@ColorRes resId: Int): Int =
    when {
        this == null -> 0
        else -> ContextCompat.getColor(this, resId)
    }

@Suppress("DEPRECATION")
fun Context?.drawableCompat(@DrawableRes resId: Int): Drawable? =
    when {
        this == null -> null
        resId == 0 -> null
        else -> ContextCompat.getDrawable(this, resId)
    }

/**
 * Keep ...Compat name for consistency with above methods
 */
fun Context?.dimenCompat(@DimenRes resId: Int): Int = this?.resources?.getDimensionPixelSize(resId) ?: 0


@ColorInt
fun View?.colorCompat(@ColorRes resId: Int): Int = this?.context?.colorCompat(resId) ?: 0
fun View?.drawableCompat(@DrawableRes resId: Int): Drawable? = this?.context?.drawableCompat(resId)
fun View?.stringCompat(@StringRes resId: Int, vararg formatArgs: Any): String =
    this?.context?.stringCompat(resId, *formatArgs) ?: ""
fun View?.dimenCompat(@DimenRes resId: Int) = this?.context?.dimenCompat(resId) ?: 0


fun AndroidViewModel.stringCompat(@StringRes resId: Int, vararg formatArgs: Any): String =
    getApplication<Application>().stringCompat(resId, *formatArgs)

fun AndroidViewModel.colorCompat(@ColorRes resId: Int): Int =
    getApplication<Application>().colorCompat(resId)


fun Fragment.stringCompat(@StringRes resId: Int, vararg formatArgs: Any?): String =
    context.stringCompat(resId, *formatArgs)

fun Fragment.colorCompat(@ColorRes resId: Int): Int = context.colorCompat(resId)

fun Fragment.dimenCompat(@DimenRes resId: Int): Int =
    context?.resources?.getDimensionPixelSize(resId) ?: 0

fun RecyclerView.ViewHolder.stringCompat(@StringRes resId: Int, vararg formatArgs: Any): String =
    itemView.context.stringCompat(resId, *formatArgs)

fun RecyclerView.ViewHolder.colorCompat(@ColorRes resId: Int): Int =
    itemView.context.colorCompat(resId)

fun RecyclerView.ViewHolder.dimenCompat(@DimenRes resId: Int): Int =
    itemView.context.resources.getDimensionPixelSize(resId)



val Context?.accentColor: Int?
    @TargetApi(LOLLIPOP)
    get() {
        if (this == null) return null
        val typedValue = TypedValue()

        val a = obtainStyledAttributes(typedValue.data, intArrayOf(android.R.attr.colorAccent))
        val color = a.getColor(0, 0)

        a.recycle()

        return color
    }

val Context.deviceWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.deviceHeight: Int
    get() = resources.displayMetrics.heightPixels

val Context.maxDimension: Int
    get() = resources.displayMetrics.run { widthPixels.coerceAtLeast(heightPixels) }

val Context.minDimension: Int
    get() = resources.displayMetrics.run { widthPixels.coerceAtMost(heightPixels) }

val Context.deviceWidthDp: Int
    get() = (deviceWidth / resources.displayMetrics.density).toInt()

val Context.deviceHeightDp: Int
    get() = (deviceHeight / resources.displayMetrics.density).toInt()

/**
 * Convert pixel value to dp
 */
fun Context.pxToDp(px: Int): Int = (px / resources.displayMetrics.density).toInt()

fun Context?.openUrl(url: String) {
//    this?.startActivity(
//        Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
//    )
    this?.toast("url $url", Toast.LENGTH_SHORT)
}

fun Fragment.openUrl(url: String) = context?.openUrl(url)
fun RecyclerView.ViewHolder.openUrl(url: String) = itemView.context?.openUrl(url)
