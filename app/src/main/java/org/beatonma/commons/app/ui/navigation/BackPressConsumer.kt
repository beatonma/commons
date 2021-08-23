package org.beatonma.commons.app.ui.navigation

@Deprecated("Use BackHandler")
interface BackPressConsumer {
    /**
     * Return true if the action is consumed
     */
    @Deprecated("Use BackHandler")
    fun onBackPressed(): Boolean
}
