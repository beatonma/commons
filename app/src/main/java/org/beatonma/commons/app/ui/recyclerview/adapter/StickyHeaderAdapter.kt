package org.beatonma.commons.app.ui.recyclerview.adapter

import org.beatonma.commons.app.ui.recyclerview.itemdecorator.sticky.StickyHeader

interface StickyHeaderAdapter {
    /**
     * Return true if the items in the given positions can be grouped under the same header.
     */
    fun isHeaderSameForPositions(first: Int, second: Int): Boolean

    /**
     * Return a [StickyHeader] instance with data for rendering the header at this position.
     */
    fun getStickyHeaderForPosition(position: Int): StickyHeader
}
