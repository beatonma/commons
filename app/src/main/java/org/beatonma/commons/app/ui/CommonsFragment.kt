package org.beatonma.commons.app.ui

import androidx.fragment.app.Fragment
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.NetworkError
import org.beatonma.commons.kotlin.extensions.networkErrorSnackbar

abstract class CommonsFragment : Fragment() {
    fun <T> IoResult<T>.report() {
        if (this is NetworkError) networkErrorSnackbar(this)
    }
}
