package org.beatonma.commons.kotlin.extensions

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.iterator

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

fun bindText(vararg pairs: Pair<TextView, String?>, textColor: Int? = null, linkColor: Int? = null, hideIfEmpty: Boolean = true) {
    pairs.forEach { (view, text) ->
        view.text = text
        if (textColor != null) view.setTextColor(textColor)
        if (linkColor != null) view.setLinkTextColor(linkColor)
        if (hideIfEmpty) view.hideIfEmpty()
    }
}

fun Menu.applyTint(color: Int) {
    iterator().forEach { item ->
        item.icon = item.icon?.mutate()?.apply { setTint(color) }
    }
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View =
    LayoutInflater.from(context)
        .inflate(layoutId, this, false)
