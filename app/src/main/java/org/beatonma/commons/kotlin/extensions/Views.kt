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
import org.beatonma.commons.kotlin.data.asStateList

val View.ratio: Float
    get() = width.toFloat() / height.toFloat()
val View.widthF: Float
    get() = width.toFloat()
val View.heightF: Float
    get() = height.toFloat()

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
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
