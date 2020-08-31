package org.beatonma.commons.data.core

private const val MILLIS_IN_DAY = 24L * 60L * 60L * 1000L
private const val DEFAULT_PERSISTENCE_PERIOD_DAYS = 5L

enum class PersistencePolicy {
    KEEP_FOREVER,  // Data persists until the user chooses to delete app data.
    SESSION,  // Data persists until the app is closed.
    USER,  // Data persists until user logs out.
    DEFAULT,  // Data persists for a period after it was accessed
    ;

    companion object {
        const val DEFAULT_PERSISTENCE_PERIOD: Long = DEFAULT_PERSISTENCE_PERIOD_DAYS * MILLIS_IN_DAY
    }
}
