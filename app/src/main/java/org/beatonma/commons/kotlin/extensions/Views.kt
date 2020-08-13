package org.beatonma.commons.kotlin.extensions

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.iterator
import org.beatonma.commons.kotlin.data.Dimensions
import org.beatonma.commons.kotlin.data.asStateList

private const val TAG = "Views.ext"

val View.ratio: Float
    get() = width.toFloat() / height.toFloat()
val View.widthF: Float
    get() = width.toFloat()
val View.heightF: Float
    get() = height.toFloat()

fun View.size(out: Dimensions = Dimensions()): Dimensions {
    out.set(width, height)
    return out
}

fun View.center(outPoint: Point? = null): Point {
    return (outPoint ?: Point()).apply {
        x = width / 2
        y = height / 2
    }
}

fun View.bounds(outRect: Rect = Rect()): Rect {
    outRect.set(left, top, right, bottom)
    return outRect
}

fun View.boundsF(outRect: RectF = RectF()): RectF {
    outRect.set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    return outRect
}

fun ViewGroup.getRelativeBoundsOfChild(child: View, outRect: Rect = Rect()): Rect {
    child.getDrawingRect(outRect)
    offsetDescendantRectToMyCoords(child, outRect)
    return outRect
}

fun View.boundsRelativeToParent(outRect: Rect = Rect()): Rect {
    val parent = parent as? ViewGroup
    if (parent == null) {
        Log.w(TAG, "boundsRelativeToParent failed: Parent could not be cast to ViewGroup")
        return outRect
    }

    getDrawingRect(outRect)
    parent.offsetDescendantRectToMyCoords(this, outRect)
    return outRect
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun showViews(vararg views: View) {
    views.forEach { it.visibility = View.VISIBLE }
}

fun hideViews(vararg views: View) {
    views.forEach { it.visibility = View.GONE }
}

fun showViews(showIf: Boolean, vararg views: View) {
    views.forEach { view ->
        view.visibility = when {
            showIf -> View.VISIBLE
            else -> View.GONE
        }
    }
}

fun setDependantVisibility(parent: View, vararg dependants: View) {
    val visibility = parent.visibility
    dependants.forEach { dependant ->
        dependant.visibility = visibility
    }
}

fun View.toggleVisibility() {
    visibility = when (visibility) {
        View.VISIBLE -> View.GONE
        else -> View.VISIBLE
    }
}


/**
 * Set visible only if this view has text
 */
fun TextView.hideIfEmpty() {
    visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
}

fun EditText.focus() {
    isFocusableInTouchMode = true
    requestFocus()
    setSelection(text.toString().length)
    val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun setBackgroundColor(color: Int, vararg views: View) {
    views.forEach { it.setBackgroundColor(color) }
}

fun setBackgroundColor(vararg colorViews: Pair<View, Int>) {
    colorViews.forEach { it.first.setBackgroundColor(it.second) }
}

fun setTextColor(color: Int, vararg textViews: TextView) {
    textViews.forEach { it.setTextColor(color) }
}

fun setLinkTextColor(color: Int, vararg textViews: TextView) {
    textViews.forEach { it.setLinkTextColor(color) }
}

fun setTextColor(textColor: Int, linkColor: Int, vararg textViews: TextView) {
    textViews.forEach { view ->
        view.setTextColor(textColor)
        view.setLinkTextColor(linkColor)
    }
}

fun bindText(vararg pairs: Pair<TextView, CharSequence?>, textColor: Int? = null, linkColor: Int? = null, hideIfEmpty: Boolean = true) {
    pairs.forEach { (view, text) ->
        view.text = text
        if (textColor != null) view.setTextColor(textColor)
        if (linkColor != null) view.setLinkTextColor(linkColor)
        if (hideIfEmpty) view.hideIfEmpty()
    }
}

/**
 * Bind text and show the view only if [ifNotNull] is not null.
 */
inline fun <T> bindText(view: TextView, ifNotNull: T?, binder: (T) -> String) {
    if (ifNotNull == null) {
        view.text = null
        view.hide()
        return
    }
    view.text = binder.invoke(ifNotNull)
    view.show()
}



fun bindContentDescription(vararg pairs: Pair<View, CharSequence?>, tooltip: Boolean = true) {
    pairs.forEach { (view, text) ->
        view.contentDescription = text
        if (tooltip) {
            view.tooltipText = text
        }
    }
}

fun applyColor(tint: Int, vararg views: View) {
    val tintList = tint.asStateList()
    views.forEach { view ->
        when (view) {
            is ImageView -> view.imageTintList = tintList
            is TextView -> view.setTextColor(tintList)
        }
    }
}

fun Menu.applyTint(color: Int) {
    iterator().forEach { item ->
        item.icon = item.icon?.mutate()?.apply { setTint(color) }
    }
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context)
        .inflate(layoutId, this, attachToRoot)


typealias MeasureMode = Int
typealias MeasureSize = Int
fun measureSpec(measureSpec: Int) = MSpec(measureSpec)
data class MSpec internal constructor(val mode: MeasureMode, val size: MeasureSize) {
    constructor(measureSpec: Int): this(
        View.MeasureSpec.getMode(measureSpec),
        View.MeasureSpec.getSize(measureSpec)
    )
}

inline fun View.withMeasureSpec(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int,
    crossinline block: (widthSize: MeasureSize, widthMode: MeasureMode, heightSize: MeasureSize, heightMode: MeasureMode) -> Unit,
) {
    val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
    val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
    val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
    val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

    block(widthSize, widthMode, heightSize, heightMode)
}


/**
 * https://developer.squareup.com/blog/showing-the-android-keyboard-reliably/
 */
fun View.focusAndShowKeyboard() {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.showKeyboardNow() {
//        addImeWindowInsetsAnimation()
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm = context.getSystemService(InputMethodManager::class.java) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        showKeyboardNow()
    } else {
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/**
 * Callbacks not being called? Try again with next release
 */
//fun View.addImeWindowInsetsAnimation() {
////    if (!Sdk.isR) {
////        Log.w(autotag, "IME animations not supported by sdk=${Sdk.version} (${Build.VERSION_CODES.R} required)")
////        return
////    }
//
//    Log.i(autotag, "Adding IME animation callback")
//    val bottomMargin = this.marginBottom
//
//    (context as? Activity)?.window?.setDecorFitsSystemWindows(false)
//    setWindowInsetsAnimationCallback(
//        @RequiresApi(Build.VERSION_CODES.R) object: WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
//            override fun onProgress(
//                insets: WindowInsets,
//                animations: MutableList<WindowInsetsAnimation>,
//            ): WindowInsets {
//                updateLayoutParams<ViewGroup.MarginLayoutParams> {
//                    val margin = bottomMargin + insets.getInsets(WindowInsets.Type.ime()).bottom
//                    updateMargins(bottom = margin.dump("Margin"))
//                }
//                return insets
//            }
//        }
//    )
//}

fun View.debugShowClick() {
    setOnClickListener {
        Log.d(autotag, "CLICK $this")
        Toast.makeText(context, "CLICK $this", Toast.LENGTH_SHORT).show()
    }
}
