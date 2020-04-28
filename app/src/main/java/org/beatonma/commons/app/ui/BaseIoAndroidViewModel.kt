package org.beatonma.commons.app.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.beatonma.commons.data.IoResult

abstract class BaseIoAndroidViewModel<D>(application: Application): AndroidViewModel(application) {
    val context: Context
        get() = getApplication()

    lateinit var liveData: LiveData<IoResult<D>>
}
