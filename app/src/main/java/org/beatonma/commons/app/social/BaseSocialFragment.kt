package org.beatonma.commons.app.social

import android.os.Bundle
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

@Deprecated("Use [SocialViewHost] and [SocialViewController] instead for usable transition animations.")
abstract class BaseSocialFragment: BaseViewmodelFragment(), SocialUiComponent {
    protected val viewmodel: SocialViewModel by activityViewModels()

    protected val observer = Observer<IoResult<SocialContent>> { result ->
        if (result is NetworkError) networkErrorSnackbar(result.error)
        if (result.data != null) {
            updateUi(result.data)
        }
    }

    fun <T: Sociable> forTarget(target: T) = forTarget(
        SocialTarget(target)
    )

    override fun forTarget(target: SocialTarget) {
        lifecycleScope.launch(Dispatchers.Main) {
            viewmodel.forTarget(target)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    protected suspend fun refreshAndObserveSocialContent() {
        withContext(Dispatchers.Main) {
            viewmodel.livedata.removeObserver(observer)
            viewmodel.refresh()
            viewmodel.livedata.observe(viewLifecycleOwner, observer)
        }
    }

    @SignInRequired
    override fun submitVote(voteType: SocialVoteType) {
        updateVoteUi(voteType)
        lifecycleScope.launch(Dispatchers.IO) {
            val result = viewmodel.postVote(voteType)
            when (result) {
                is NetworkError -> networkErrorSnackbar(result.error)
                is NoBodySuccessResult -> onVoteSubmissionSuccessful()
                else -> {
                    snackbar(result.message ?: result.toString())
                }
            }
        }
    }

    override fun onVoteClicked(voteType: SocialVoteType) {
        if (viewmodel.shouldRemoveVote(voteType)) {
            submitVote(SocialVoteType.NULL)
        }
        else {
            submitVote(voteType)
        }
    }

    override suspend fun onVoteSubmissionSuccessful() {
        snackbar("Vote submitted!")
        refreshAndObserveSocialContent()
    }
}
