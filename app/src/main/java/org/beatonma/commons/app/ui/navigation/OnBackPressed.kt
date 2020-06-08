package org.beatonma.commons.app.ui.navigation

interface OnBackPressed {
    /**
     * Return true if the action is consumed
     */
    fun onBackPressed(): Boolean
}
