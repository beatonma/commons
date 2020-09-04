package org.beatonma.commons.data.core.interfaces

interface Timestamped {
    var accessedAt: Long
    var createdAt: Long

    fun millisSinceLastAccess(): Long = System.currentTimeMillis() - accessedAt

    companion object {
        const val FIELD_ACCESSED_AT = "accessed_at"
        const val FIELD_CREATED_AT = "created_at"
    }
}

fun <T: Timestamped> T.markCreated(): T {
    val now = System.currentTimeMillis()
    createdAt = now
    accessedAt = now
    return this
}

fun <T: Timestamped> T.markAccessed(): T {
    accessedAt = System.currentTimeMillis()
    return this
}

fun <T: Timestamped> Collection<T>.markAllAccessed(): List<T>  = map { it.markAccessed() }
