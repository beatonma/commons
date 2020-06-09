package org.beatonma.commons.app.social

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.annotations.SignInRequired
import org.beatonma.commons.app.ui.BaseViewmodelFragment
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.NetworkError
import org.beatonma.commons.data.NoBodySuccessResult
import org.beatonma.commons.data.core.interfaces.Sociable
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType
import org.beatonma.commons.kotlin.extensions.networkErrorSnackbar
import org.beatonma.commons.kotlin.extensions.snackbar

private const val TAG = "BaseSocialFrag"

abstract class BaseSocialFragment: BaseViewmodelFragment() {
    protected val viewmodel: SocialViewModel by activityViewModels { viewmodelFactory }

    protected val observer = Observer<IoResult<SocialContent>> { result ->
        if (result is NetworkError) networkErrorSnackbar(result.error)
        if (result.data != null) {
            updateUi(result.data)
        }
    }

    fun <T: Sociable> forTarget(target: T) = forTarget(
        SocialTarget(target)
    )

    open fun forTarget(target: SocialTarget) {
        lifecycleScope.launch(Dispatchers.Main) {
            viewmodel.forTarget(target)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    protected abstract fun updateUi(content: SocialContent?)
    protected abstract fun updateVoteUi(voteType: SocialVoteType)

    protected abstract fun setupClickListeners()

    protected suspend fun refreshAndObserveSocialContent() {
        withContext(Dispatchers.Main) {
            viewmodel.livedata.removeObserver(observer)
            viewmodel.refresh()
            viewmodel.livedata.observe(viewLifecycleOwner, observer)
        }
    }

    @SignInRequired
    private fun submitVote(voteType: SocialVoteType) {
        updateVoteUi(voteType)
        lifecycleScope.launch(Dispatchers.IO) {
            val result = viewmodel.postVote(voteType)
            when (result) {
                is NetworkError -> networkErrorSnackbar(result.error)
                is NoBodySuccessResult -> onVoteSubmissionSuccessful()
                else -> {
                    Log.w(TAG, result.toString())
                    snackbar(result.message ?: result.toString())
                }
            }
        }
    }

    protected fun onVoteClicked(voteType: SocialVoteType) {
        if (viewmodel.shouldRemoveVote(voteType)) {
            submitVote(SocialVoteType.NULL)
        }
        else {
            submitVote(voteType)
        }
    }

    protected open suspend fun onVoteSubmissionSuccessful() {
        snackbar("Vote submitted!")
        refreshAndObserveSocialContent()
    }
}
