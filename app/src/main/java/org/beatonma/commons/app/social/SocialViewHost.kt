package org.beatonma.commons.app.social

import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.annotation.CallSuper
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.annotations.SignInRequired
import org.beatonma.commons.app.ui.navigation.OnBackPressed
import org.beatonma.commons.data.NoBodySuccessResult
import org.beatonma.commons.data.core.social.SocialVoteType

private const val TAG = "SocialViewHost"

interface SocialViewHost: LifecycleOwner, OnBackPressed {
    val socialViewModel: SocialViewModel
    val socialViewController: SocialViewController

    fun setupViewController(
        layout: MotionLayout,
        defaultTransition: Int? = null,
        collapsedTheme: SocialViewTheme? = null
    ): SocialViewController {
        if (defaultTransition != null) {
            layout.setTransition(defaultTransition)
        }

        val expandedTheme = resolveColorAttributes(layout.context)
        return SocialViewController(
            this,
            layout,
            collapsedTheme ?: expandedTheme,
            expandedTheme,
            defaultTransition = defaultTransition,
        )
    }

    fun onVoteSubmissionSuccessful() {
        socialViewController.onVoteSubmissionSuccessful()
    }

    @CallSuper
    fun onCommentSubmissionSuccessful() {
        socialViewController.onCommentSubmissionSuccessful()
    }

    fun onVoteClicked(voteType: SocialVoteType) {
        if (socialViewModel.shouldRemoveVote(voteType)) {
            submitVote(SocialVoteType.NULL)
        }
        else {
            submitVote(voteType)
        }
    }

    @SignInRequired
    fun submitVote(voteType: SocialVoteType) {
        socialViewController.updateVoteUi(voteType)
        lifecycleScope.launch {
            val result = socialViewModel.postVote(voteType)
            when (result) {
                is NoBodySuccessResult -> onVoteSubmissionSuccessful()
                else -> {
                    Log.w(TAG, result.toString())
                }
            }
        }
    }

    @SignInRequired
    private fun submitComment(text: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = socialViewModel.postComment(text)
            when (result) {
                is NoBodySuccessResult -> onCommentSubmissionSuccessful()
                else -> {
                    Log.w(TAG, result.toString())
                }
            }
        }
    }

    fun validateComment(comment: String) {
        val result = socialViewModel.validateComment(comment)
        when (result) {
            CommentValidation.VALID -> submitComment(comment)
            else -> socialViewController.notifyInvalidComment(result)
        }
    }

    override fun onBackPressed(): Boolean {
        return socialViewController.onBackPressed()
    }

    fun resolveColorAttributes(context: Context): SocialViewTheme {
        val typedValue = TypedValue()
        val theme = context.theme

        theme.resolveAttribute(R.attr.colorControlNormal, typedValue, true)
        val colorControlNormal = typedValue.data

        theme.resolveAttribute(R.attr.colorControlActivated, typedValue, true)
        val colorControlActive = typedValue.data

        return SocialViewTheme(colorControlNormal, colorControlActive)
    }
}