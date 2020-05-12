package org.beatonma.commons.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.beatonma.commons.data.LiveDataIoResult

abstract class BaseIoAndroidViewModel<D>(application: Application): AndroidViewModel(application) {
    lateinit var liveData: LiveDataIoResult<D>
}
