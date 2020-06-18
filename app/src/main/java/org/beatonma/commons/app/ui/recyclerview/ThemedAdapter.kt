package org.beatonma.commons.app.ui.recyclerview

import org.beatonma.commons.R
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.Themed

abstract class ThemedAdapter<T>(
    emptyLayoutID: Int = R.layout.vh_empty_results,
): ShowWhenEmptyAdapter<T>(emptyLayoutID = emptyLayoutID), Themed {
    override var theme: PartyColors? = null
}

abstract class ThemedCollapsibleAdapter<T>(
    emptyLayoutID: Int = R.layout.vh_empty_results,
): CollapsibleAdapter<T>(emptyLayoutID = emptyLayoutID), Themed {
    override var theme: PartyColors? = null
}
