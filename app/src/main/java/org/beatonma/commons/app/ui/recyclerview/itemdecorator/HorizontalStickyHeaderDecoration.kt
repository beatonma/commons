package org.beatonma.commons.app.ui.recyclerview.itemdecorator

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.adapter.StickyHeaderAdapter
import org.beatonma.commons.data.ShouldNotHappen
import org.beatonma.commons.kotlin.extensions.colorCompat
import org.beatonma.commons.kotlin.extensions.dimenCompat
import org.beatonma.commons.kotlin.extensions.getRelativeBoundsOfChild

class HorizontalStickyHeaderDecoration(
    context: Context,
    private val headerTextSize: Int = context.dimenCompat(R.dimen.list_sticky_header_text_size),

    // Minimum space before the header text - filled by background, if provided.
    private val marginHorizontal: Int = context.dimenCompat(R.dimen.list_sticky_header_text_margin_horizontal),

    // Minimum gap between consecutive headers - they will be pushed offscreen if they try to get closer than this
    headerMarginBetween: Int = context.dimenCompat(R.dimen.list_sticky_header_text_margin_between),

    // Space above and below the header text - filled by background, if provided
    private val headerPaddingVertical: Int = context.dimenCompat(R.dimen.list_sticky_header_text_margin_vertical),

    // Additional padding - particularly useful with StickyHeader.backgroundFillsView
    private val space: Space = defaultSpace(context),

    typeface: Typeface = Typeface.DEFAULT_BOLD,
): RecyclerView.ItemDecoration() {
    private val headerMarginBetween: Int = headerMarginBetween + space.marginBetweenGroups

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
    private val rectF = RectF()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val headerMap = getHeadersForVisibleItems(parent)

        val multipleTitlesVisible = headerMap.keys.size > 1

        // Track the leftmost position of the view to the right of the current one - we are
        // traversing the child views from right to left
        var previousLeft: Int = Int.MAX_VALUE

        parent.forEachChild(reversed = true) { adapterPosition, child ->
            if (adapterPosition !in headerMap) return@forEachChild

            parent.getRelativeBoundsOfChild(child, rect)
            val relativeLeftOfChild = rect.left
            val defaultLeft = relativeLeftOfChild.coerceAtLeast(0)

            var defaultRight: Int = rect.right
            val lastPositionInGroup = headerMap.keys.sorted()
                .firstOrNull { it > adapterPosition }
                ?.minus(1)
                ?: Int.MAX_VALUE

            parent.forEachChild { _adapterPosition, _child ->
                if (_adapterPosition <= lastPositionInGroup) {
                    parent.getRelativeBoundsOfChild(_child, rect)
                    defaultRight = rect.right
                }
            }

            val header = headerMap[adapterPosition]
                ?: throw ShouldNotHappen("We already checked that the key exists in the map, and map values are not nullable")
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
                    endX = defaultRight
                }

                adapterPosition == 0 -> {
                    startX = defaultLeft
                    endX = previousLeft - marginHorizontal - headerMarginBetween
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
        rectF.set(
            startX.toFloat() - space.paddingStart,
            0F - space.paddingTop,
            endX.toFloat() + space.paddingEnd,
            bottomY.toFloat() + space.paddingBottom,
        )

        if (stickyHeader.backgroundColor != null) {
            if (stickyHeader.backgroundCornerRadius != null) {
                val radius = stickyHeader.backgroundCornerRadius.toFloat()
                canvas.drawRoundRect(
                    rectF,
                    radius,
                    radius,
                    backgroundPaint.apply { color = stickyHeader.backgroundColor }
                )
            }
            else {
                canvas.drawRect(
                    rectF,
                    backgroundPaint.apply { color = stickyHeader.backgroundColor }
                )
            }
        }

        stickyHeader.backgroundDrawable?.run {
            setBounds(rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt())
            draw(canvas)
        }
    }

    private fun drawHeaderText(canvas: Canvas, stickyHeader: StickyHeader, startX: Int) {
        canvas.drawText(
            stickyHeader.text,
            startX.toFloat() + marginHorizontal,
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
        val position = parent.getChildAdapterPosition(view)

        val extraStartMargin = when {
            position == 0 -> 0
            headerSameAsPrevious(parent, position) -> 0
            else -> space.marginBetweenGroups
        }

        outRect.set(
            space.paddingStart + extraStartMargin,
            textPaint.textSize.toInt() + (2 * headerPaddingVertical) + space.paddingTop,
            space.paddingEnd,
            space.paddingBottom
        )
    }

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


/**
 * Return map of Position:HeaderText, each being the start point of a new header.
 */
private fun getHeadersForVisibleItems(
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
private fun headerSameAsPrevious(
    parent: RecyclerView,
    position: Int,
): Boolean = parent.stickyAdapter.isHeaderSameForPositions(position - 1, position)

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

private fun defaultSpace(context: Context): HorizontalStickyHeaderDecoration.Space =
    HorizontalStickyHeaderDecoration.Space(
        marginBetweenGroups = context.dimenCompat(R.dimen.list_space_between_groups_horizontal)
    )


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

private val RecyclerView.stickyAdapter get() = adapter as StickyHeaderAdapter
