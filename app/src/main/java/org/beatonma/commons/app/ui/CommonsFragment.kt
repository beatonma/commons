package org.beatonma.commons.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.ui.recyclerview.adapter.AsyncDiffHost
import org.beatonma.commons.kotlin.extensions.networkErrorSnackbar
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.NetworkError
import org.beatonma.commons.repo.result.SuccessResult

abstract class CommonsFragment<B: ViewBinding>: Fragment(), AsyncDiffHost {
    override var diffJob: Job? = null

    protected var _binding: B? = null
    val binding: B get() = _binding!!

    abstract fun inflateBinding(inflater: LayoutInflater): B

    fun <T> IoResult<T>.report() {
        if (this is NetworkError) networkErrorSnackbar(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater)
        return binding.root
    }

    inline fun <reified T> IoResult<T>.handle(
        noData: ActionBlock = {},
        withData: (T) -> Unit
    ) {
        when (this) {
            is NetworkError -> networkErrorSnackbar(this)
            is SuccessResult -> {
                if (data != null) {
                    withData.invoke(data as T)
                }
                else {
                    noData.invoke()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
