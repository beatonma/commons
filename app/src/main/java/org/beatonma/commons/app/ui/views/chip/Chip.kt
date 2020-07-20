package org.beatonma.commons.app.ui.views.chip


interface Chip {
    fun bind(chipData: ChipData)
}

interface CollapsibleChip: Chip {
    fun collapse()
    fun expand()

    companion object {
        const val AUTO_COLLAPSE_TIME_MILLIS: Long = 3000L
    }
}
