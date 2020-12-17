package org.beatonma.commons.data

import androidx.lifecycle.SavedStateHandle

inline class SavedStateKey(val name: String)

operator fun <T> SavedStateHandle.set(key: SavedStateKey, value: T) {
    set(key.name, value)
}

operator fun <T> SavedStateHandle.get(key: SavedStateKey): T? = get(key.name)
