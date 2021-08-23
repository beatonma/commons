package org.beatonma.commons.app.data

import androidx.lifecycle.SavedStateHandle

@JvmInline
value class SavedStateKey(val name: String)

operator fun <T> SavedStateHandle.set(key: SavedStateKey, value: T) {
    set(key.name, value)
}

operator fun <T> SavedStateHandle.get(key: SavedStateKey): T? = get(key.name)
