package org.beatonma.commons.app.ui.recyclerview.itemdecorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.adapter.StickyHeaderAdapter
import org.beatonma.commons.kotlin.extensions.colorCompat
import org.beatonma.commons.kotlin.extensions.dimenCompat
import org.beatonma.commons.kotlin.extensions.getRelativeBoundsOfChild

class HorizontalStickyHeaderDecoration(
    context: Context,
    private val headerTextSize: Int = context.dimenCompat(R.dimen.list_sticky_header_text_size),
    private val headerMarginStart: Int = context.dimenCompat(R.dimen.list_sticky_header_text_margin_horizontal),
    private val headerMarginBetween: Int = context.dimenCompat(R.dimen.list_sticky_header_text_margin_between),
    private val headerPaddingVertical: Int = context.dimenCompat(R.dimen.list_sticky_header_text_margin_vertical),
    private val paddingVertical: Int = 0,
    private val paddingHorizontal: Int = 0,
    private val paddingStart: Int = paddingHorizontal,
    private val paddingTop: Int = paddingVertical,
    private val paddingEnd: Int = paddingHorizontal,
    private val paddingBottom: Int = paddingVertical,
    typeface: Typeface = Typeface.DEFAULT_BOLD,
): RecyclerView.ItemDecoration() {
    private val headerPositionMap: MutableMap<Int, StickyHeader> = mutableMapOf()

    private val headerHeight = headerTextSize + (2 * headerPaddingVertical)
    private val defaultTextColor = context.colorCompat(R.color.TextPrimary)

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = defaultTextColor
        textSize = headerTextSize.toFloat()
        this.typeface = typeface
    }
    private val rect = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        getHeadersForVisibleItems(parent, headerPositionMap)

        var previousLeft: Int = Int.MAX_VALUE
        val multipleTitlesVisible = headerPositionMap.keys.size > 1
        parent.forEachChild(reversed = true) { adapterPosition, child ->
            if (adapterPosition !in headerPositionMap) return@forEachChild

            parent.getRelativeBoundsOfChild(child, rect)
            val relativeLeftOfChild = rect.left
            val defaultLeft = relativeLeftOfChild.coerceAtLeast(0)

            var defaultRight: Int = rect.right
            val lastPositionInGroup = headerPositionMap.keys.sorted()
                .firstOrNull { it > adapterPosition }
                ?.minus(1)
                ?: Int.MAX_VALUE

            parent.forEachChild { _adapterPosition, _child ->
                if (_adapterPosition <= lastPositionInGroup) {
                    parent.getRelativeBoundsOfChild(_child, rect)
                    defaultRight = rect.right
                }
            }

            val header = headerPositionMap[adapterPosition]!!
            val headerText = header.text
            val textWidth = textPaint.measureText(headerText)

            val endX: Int
            val startX: Int
            when {
                multipleTitlesVisible -> {
                    val overlapping = (defaultLeft + textWidth + headerMarginBetween) > previousLeft
                    startX = when {
                        overlapping -> (previousLeft - textWidth - headerMarginBetween).toInt()
                        else -> defaultLeft
                    }
                    endX = previousLeft - headerMarginStart
                }

                adapterPosition == 0 -> {
                    startX = defaultLeft
                    endX = previousLeft - headerMarginStart
                }
                else -> {
                    startX = 0
                    endX = defaultRight
                }
            }
            val bottomY = if (header.backgroundFillsView) rect.bottom else headerHeight

            drawHeader(c, header, startX, endX, bottomY)
            previousLeft = relativeLeftOfChild
        }
    }

    private fun drawHeader(canvas: Canvas, stickyHeader: StickyHeader, startX: Int, endX: Int, bottomY: Int) {
        drawHeaderBackground(canvas, stickyHeader, startX, endX, bottomY)
        drawHeaderText(canvas, stickyHeader, startX)
    }

    private fun drawHeaderBackground(canvas: Canvas, stickyHeader: StickyHeader, startX: Int, endX: Int, bottomY: Int) {
        if (stickyHeader.backgroundColor != null) {
            canvas.drawRect(
                startX.toFloat() - paddingStart,
                0F - paddingTop,
                endX.toFloat() + paddingEnd,
                bottomY.toFloat() + paddingBottom,
                backgroundPaint.apply { color = stickyHeader.backgroundColor }
            )
        }

        stickyHeader.backgroundDrawable?.run {
            setBounds(startX, 0, endX, headerHeight)
            draw(canvas)
        }
    }

    private fun drawHeaderText(canvas: Canvas, stickyHeader: StickyHeader, startX: Int) {
        canvas.drawText(
            stickyHeader.text,
            startX.toFloat() + headerMarginStart,
            textPaint.textSize + headerPaddingVertical,
            textPaint.apply { color = stickyHeader.textColor ?: defaultTextColor }
        )
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.set(
            paddingStart,
            textPaint.textSize.toInt() + (2 * headerPaddingVertical) + paddingTop,
            paddingEnd,
            paddingBottom
        )
    }
}


/**
 * Return map of Position:HeaderText, each being the start point of a new header.
 */
private fun getHeadersForVisibleItems(
    parent: RecyclerView,
    outMap: MutableMap<Int, StickyHeader>
): Map<Int, StickyHeader> {
    outMap.clear()

    val adapter = parent.adapter as StickyHeaderAdapter

    var previousText: String? = null

    parent.forEachChild { adapterPosition, _ ->
        val header = adapter.getStickyHeaderForPositoin(adapterPosition)
        val headerText = header.text
        if (headerText != previousText) {
            outMap[adapterPosition] = header
        }
        previousText = headerText
    }
    return outMap
}

private inline fun RecyclerView.forEachChild(reversed: Boolean = false, block: (adapterPosition: Int, child: View) -> Unit) {
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


class StickyHeader(
    val text: String,
    val textColor: Int? = null,
    val backgroundColor: Int? = null,
    val backgroundDrawable: Drawable? = null,

    // If true, background color/drawable will appear behind the normal viewholder as well as the header.
    val backgroundFillsView: Boolean = false,
)
