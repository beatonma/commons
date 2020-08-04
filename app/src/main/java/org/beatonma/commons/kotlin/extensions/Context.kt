package org.beatonma.commons.kotlin.extensions

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.text.Html
import android.text.Spanned
import android.text.SpannedString
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.device.Sdk
import org.beatonma.commons.kotlin.data.Dimensions

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

// Strings
@Suppress("DEPRECATION")
fun Context?.stringCompat(@StringRes @PluralsRes resId: Int, vararg formatArgs: Any?, quantity: Int = Int.MIN_VALUE): String = when {
    this == null -> ""
    quantity != Int.MIN_VALUE -> resources.getQuantityString(resId, quantity, *formatArgs)
    Sdk.isMarshmallow -> getString(resId, *formatArgs)
    else -> resources.getString(resId, *formatArgs)
}

fun AndroidViewModel.stringCompat(@StringRes @PluralsRes resId: Int, vararg formatArgs: Any, quantity: Int = Int.MIN_VALUE): String =
    getApplication<Application>().stringCompat(resId, *formatArgs, quantity = quantity)

fun Fragment.stringCompat(@StringRes @PluralsRes resId: Int, vararg formatArgs: Any?, quantity: Int = Int.MIN_VALUE): String =
    context.stringCompat(resId, *formatArgs, quantity = quantity)

fun View?.stringCompat(@StringRes @PluralsRes resId: Int, vararg formatArgs: Any, quantity: Int = Int.MIN_VALUE): String =
    this?.context?.stringCompat(resId, *formatArgs, quantity = quantity) ?: ""

fun RecyclerView.ViewHolder.stringCompat(@StringRes @PluralsRes resId: Int, vararg formatArgs: Any, quantity: Int = Int.MIN_VALUE): String =
    itemView.context.stringCompat(resId, *formatArgs, quantity = quantity)


@Suppress("DEPRECATION")
fun Context?.htmlCompat(@StringRes resId: Int, vararg formatArgs: Any?): Spanned =
    when {
        this == null -> SpannedString("")
        Sdk.isNougat -> Html.fromHtml(getString(resId, *formatArgs))
        else -> Html.fromHtml(resources.getString(resId, *formatArgs), Html.FROM_HTML_MODE_LEGACY)
    }

@Suppress("DEPRECATION")
@ColorInt
fun Context?.colorCompat(@ColorRes resId: Int): Int =
    when {
        this == null -> 0
        else -> ContextCompat.getColor(this, resId)
    }

@Suppress("DEPRECATION")
fun Context?.drawableCompat(@DrawableRes resId: Int, tint: Int? = null): Drawable? =
    when {
        this == null -> null
        resId == 0 -> null
        else -> {
            if (tint == null) {
                ContextCompat.getDrawable(this, resId)
            }
            else {
                ContextCompat.getDrawable(this, resId)?.mutate()?.apply {
                    setTint(tint)
                }
            }
        }
    }

/**
 * Keep ...Compat name for consistency with above methods
 */
fun Context?.dimenCompat(@DimenRes resId: Int): Int = this?.resources?.getDimensionPixelSize(resId) ?: 0


@ColorInt
fun View?.colorCompat(@ColorRes resId: Int): Int = this?.context?.colorCompat(resId) ?: 0
fun View?.drawableCompat(@DrawableRes resId: Int, tint: Int? = null): Drawable? = this?.context?.drawableCompat(resId, tint)
fun View?.dimenCompat(@DimenRes resId: Int) = this?.context?.dimenCompat(resId) ?: 0



fun AndroidViewModel.colorCompat(@ColorRes resId: Int): Int =
    getApplication<Application>().colorCompat(resId)


fun Fragment.colorCompat(@ColorRes resId: Int): Int = context.colorCompat(resId)

fun Fragment.dimenCompat(@DimenRes resId: Int): Int =
    context?.resources?.getDimensionPixelSize(resId) ?: 0

fun Fragment.htmlCompat(@StringRes resId: Int, vararg formatArgs: Any?): Spanned =
    context.htmlCompat(resId, *formatArgs)

fun RecyclerView.ViewHolder.colorCompat(@ColorRes resId: Int): Int =
    itemView.context.colorCompat(resId)

fun RecyclerView.ViewHolder.dimenCompat(@DimenRes resId: Int): Int =
    itemView.context.resources.getDimensionPixelSize(resId)

fun RecyclerView.ViewHolder.htmlCompat(@StringRes resId: Int, vararg formatArgs: Any?): Spanned =
    itemView.context.htmlCompat(resId, *formatArgs)

fun RecyclerView.ViewHolder.drawableCompat(@DrawableRes resId: Int, tint: Int? = null): Drawable? =
    itemView.context.drawableCompat(resId, tint)

val RecyclerView.ViewHolder.context: Context get() = itemView.context



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

fun Context.displaySize(out: Dimensions = Dimensions()): Dimensions {
    with (resources.displayMetrics) {
        out.set(widthPixels, heightPixels)
    }
    return out
}

/**
 * Convert pixel value to dp
 */
fun Context.pxToDp(px: Int): Int = (px / resources.displayMetrics.density).toInt()

fun Context?.openUrl(url: String) {
    this?.tryStartActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
    )
}

fun Context?.dial(phoneNumber: String) {
    this?.tryStartActivity(
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
    )
}

fun Context?.sendMail(emailAddress: String) {
    this?.tryStartActivity(
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$emailAddress")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        }
    )
}

fun Fragment.openUrl(url: String) = context?.openUrl(url)
fun RecyclerView.ViewHolder.openUrl(url: String) = itemView.context?.openUrl(url)


fun Context.tryStartActivity(intent: Intent, otherwise: (() -> Unit)? = null) {
    if (intent.resolveActivity(packageManager) != null) {
        toast(intent.dataString ?: "NO DATA", Toast.LENGTH_SHORT)
//        startActivity(intent)
    }
    else {
        otherwise?.invoke()
    }
}
