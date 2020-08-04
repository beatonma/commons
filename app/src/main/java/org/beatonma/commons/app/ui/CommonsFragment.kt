package org.beatonma.commons.app.ui

import androidx.fragment.app.Fragment
import kotlinx.coroutines.Job
import org.beatonma.commons.app.ui.recyclerview.adapter.AsyncDiffHost
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.NetworkError
import org.beatonma.commons.kotlin.extensions.networkErrorSnackbar

abstract class CommonsFragment : Fragment(), AsyncDiffHost {
    override var diffJob: Job? = null

    fun <T> IoResult<T>.report() {
        if (this is NetworkError) networkErrorSnackbar(this)
    }
}
