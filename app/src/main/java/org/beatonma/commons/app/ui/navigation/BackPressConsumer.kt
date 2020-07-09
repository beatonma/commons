package org.beatonma.commons.app.ui.navigation

interface BackPressConsumer {
    /**
     * Return true if the action is consumed
     */
    fun onBackPressed(): Boolean
}
