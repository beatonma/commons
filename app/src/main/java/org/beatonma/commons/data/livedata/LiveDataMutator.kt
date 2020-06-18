package org.beatonma.commons.data.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

abstract class LiveDataMutator<D>(
    private var mutable: D
) {
    val value: MutableLiveData<D> = MutableLiveData()

    fun update(
        updatePredicate: ((D) -> Boolean) = { true },
        block: D.() -> D,
    ): D {
        if (updatePredicate.invoke(mutable)) {
            mutable = block.invoke(mutable)
            value.value = mutable
        }
        else {
            Log.v("LiveDataMutator", "Update with no diff skipped")
        }
        return mutable
    }
}


inline fun <T> observeComplete(
    // An empty/base instance of the type that our mediator will update with values from sources added in block()
    empty: T,

    crossinline updatePredicate: ((T) -> Boolean) = { true },

    //Add child LiveData sources with addSource(liveData) here
    addSources: MediatorLiveData<T>.(LiveDataMutator<T>) -> Unit
): LiveData<T> {
    val complete = object: LiveDataMutator<T>(empty) {}
    return MediatorLiveData<T>().apply {
        addSources(complete)

        // Propagate changes to external observer, unless updatePredicate returns false
        addSource(complete.value) {
            if (updatePredicate.invoke(it)) {
                value = it
            }
        }
    }
}
