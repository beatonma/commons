package org.beatonma.commons.kotlin.extensions

import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.iterator
import org.beatonma.commons.kotlin.data.Dimensions
import org.beatonma.commons.kotlin.data.asStateList

private const val TAG = "Views.ext"


fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context)
        .inflate(layoutId, this, attachToRoot)

val View.ratio: Float get() = widthF / heightF
val View.widthF: Float get() = width.toFloat()
val View.heightF: Float get() = height.toFloat()

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

fun showViews(showIf: Boolean, vararg views: View) =
    views.forEach { view ->
        view.visibility = when {
            showIf -> View.VISIBLE
            else -> View.GONE
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

fun setBackgroundColor(color: Int, vararg views: View) {
    views.forEach { it.setBackgroundColor(color) }
}

fun setBackgroundColor(vararg colorViews: Pair<View, Int>) {
    colorViews.forEach { it.first.setBackgroundColor(it.second) }
}

fun setTextColor(color: Int, vararg textViews: TextView) {
    textViews.forEach { it.setTextColor(color) }
}

//fun setLinkTextColor(color: Int, vararg textViews: TextView) {
//    textViews.forEach { it.setLinkTextColor(color) }
//}
//
//fun setTextColor(textColor: Int, linkColor: Int, vararg textViews: TextView) {
//    textViews.forEach { view ->
//        view.setTextColor(textColor)
//        view.setLinkTextColor(linkColor)
//    }
//}

fun bindText(
    vararg pairs: Pair<TextView, CharSequence?>,
    textColor: Int? = null,
    linkColor: Int? = null,
    hideIfEmpty: Boolean = true,
) {
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

fun bindContentDescription(vararg pairs: Pair<View, CharSequence?>) =
    pairs.forEach { (view, text) ->
        view.contentDescription = text
        view.tooltipText = text
    }

fun bindTooltop(vararg pairs: Pair<View, CharSequence?>,) = bindContentDescription(*pairs)

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

fun View.debugShowClick() {
    setOnClickListener {
        Log.d(autotag, "CLICK $this")
        Toast.makeText(context, "CLICK $this", Toast.LENGTH_SHORT).show()
    }
}
