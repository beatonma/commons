package org.beatonma.commons.app.ui.recyclerview.itemdecorator.sticky

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.adapter.StickyHeaderAdapter
import org.beatonma.commons.kotlin.extensions.colorCompat
import org.beatonma.commons.kotlin.extensions.dimenCompat

abstract class StickyHeaderDecoration(
    context: Context,
    protected val headerTextSize: Int = context.dimenCompat(R.dimen.list_sticky_header_text_size),

    // Additional padding - particularly useful with StickyHeader.backgroundFillsView
    protected val space: Space,

    typeface: Typeface = Typeface.DEFAULT_BOLD,
): RecyclerView.ItemDecoration() {
    protected val defaultTextColor = context.colorCompat(R.color.TextPrimary)

    protected val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    protected val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = defaultTextColor
        textSize = headerTextSize.toFloat()
        this.typeface = typeface
    }
    protected val rect = Rect()
    protected val rectF = RectF()



    /**
     * Space added by padding fields will be filled by background.
     * Space added by marginBetweenGroups will be empty.
     */
    class Space(
        val paddingVertical: Int = 0,
        val paddingHorizontal: Int = 0,
        val paddingStart: Int = paddingHorizontal,
        val paddingTop: Int = paddingVertical,
        val paddingEnd: Int = paddingHorizontal,
        val paddingBottom: Int = paddingVertical,
        val marginBetweenGroups: Int = paddingHorizontal,
    )
}

class StickyHeader(
    val text: String,
    val textColor: Int? = null,
    val backgroundColor: Int? = null,
    val backgroundCornerRadius: Int? = null,
    val backgroundDrawable: Drawable? = null,

    // If true, background color/drawable will appear behind the normal viewholder as well as the header.
    val backgroundFillsView: Boolean = false,
) {
    override fun toString() = text
}

internal val RecyclerView.stickyAdapter get() = adapter as StickyHeaderAdapter




/**
 * Return map of Position:HeaderText, each being the start point of a new header.
 */
internal fun getHeadersForVisibleItems(
    parent: RecyclerView,
): Map<Int, StickyHeader> {
    val map = mutableMapOf<Int, StickyHeader>()
    val adapter = parent.stickyAdapter

    var previousText: String? = null

    parent.forEachChild { adapterPosition, _ ->
        val header = adapter.getStickyHeaderForPosition(adapterPosition)
        val headerText = header.text
        if (headerText != previousText) {
            map[adapterPosition] = header
        }
        previousText = headerText
    }
    return map
}

/**
 * Returns header text for (previous, current) positions
 */
internal fun headerSameAsPrevious(
    parent: RecyclerView,
    position: Int,
): Boolean = parent.stickyAdapter.isHeaderSameForPositions(position - 1, position)

internal inline fun RecyclerView.forEachChild(reversed: Boolean = false, block: (adapterPosition: Int, child: View) -> Unit) {
    val childCount = this.childCount
    val range = if (reversed) (0..childCount).reversed() else 0..childCount
    for (position in range) {
        val child = getChildAt(position)
        val adapterPosition = getChildAdapterPosition(child)
        if (adapterPosition != RecyclerView.NO_POSITION) {
            block(adapterPosition, child)
        }
    }
}
