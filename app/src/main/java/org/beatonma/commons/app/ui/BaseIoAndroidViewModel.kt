package org.beatonma.commons.app.ui

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import org.beatonma.commons.app
import org.beatonma.commons.repo.LiveDataIoResult

abstract class BaseIoAndroidViewModel<D>(
    context: Context,
): AndroidViewModel(context.app) {
    lateinit var liveData: LiveDataIoResult<D>
}
