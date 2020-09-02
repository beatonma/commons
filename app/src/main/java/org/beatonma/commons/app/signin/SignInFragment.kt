package org.beatonma.commons.app.signin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionManager
import com.google.android.gms.common.SignInButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.databinding.FragmentSigninBinding
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.isError
import org.beatonma.commons.repo.result.isSuccess

private const val RC_GOOGLE_SIGNIN = 9913

private enum class State {
    LOADING,
    SIGNED_OUT,
    SIGNED_IN,
    MANAGE_ACCOUNT,
    EDIT_USERNAME,
    ;
}


@AndroidEntryPoint
class SignInFragment : BottomSheetDialogFragment(), BackPressConsumer {
    private var _binding: FragmentSigninBinding? = null
    val binding: FragmentSigninBinding get() = _binding!!

    private val tokenObserver = Observer<IoResult<UserToken>> { result ->
        when {
            result.isSuccess -> updateUI(result.data)
            result.isError -> updateUI(null)
        }
    }

    private val viewmodel: SignInViewModel by viewModels()
    private val currentState: State get() = when(binding.root.currentState) {
        R.id.state_loading -> State.LOADING
        R.id.state_manage_account -> State.MANAGE_ACCOUNT
        R.id.state_edit_username -> State.EDIT_USERNAME
        R.id.state_signed_in -> State.SIGNED_IN
        else -> State.SIGNED_OUT
    }

    private val dialog: BottomSheetDialog? get() = super.getDialog() as? BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object: BottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                if (this@SignInFragment.onBackPressed()) {
                    return
                }
                super.onBackPressed()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSigninBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.behavior?.apply {
            isFitToContents = false
            skipCollapsed = true
            peekHeight = view.context?.deviceHeight ?: BottomSheetBehavior.PEEK_HEIGHT_AUTO
        }

        binding.gSignInButton.apply {
            setSize(SignInButton.SIZE_STANDARD)
            setOnClickListener { googleSignIn() }
        }

        setupClickListeners()

        viewmodel.getTokenForCurrentSignedInAccount()
        observeToken()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // We want the MotionLayout to take the full size so we don't have to resize it later
        // when changing between states - better performance this way.
        (binding.root.parent as? ViewGroup)?.updateLayoutParams {
            height = binding.root.context.deviceHeight
        }
    }

    private fun setupClickListeners() {
        binding.signoutButton.setOnClickListener { googleSignOut() }
        binding.deleteAccountButton.setOnClickListener { startDeleteAccount() }

        binding.openFullAccount.setOnClickListener {
            transitionToState(State.MANAGE_ACCOUNT)
        }

        val editUsernameClickListener = View.OnFocusChangeListener { view, focussed ->
            if (!focussed) {
                transitionToState(State.MANAGE_ACCOUNT)
                view.onFocusChangeListener = null
            }
        }
        binding.editUsernameIcon.onFocusChangeListener = editUsernameClickListener
        binding.accountUsernameEditable.onFocusChangeListener = editUsernameClickListener

        binding.editUsernameIcon.setOnClickListener {
            transitionToState(State.EDIT_USERNAME)
            binding.root.addTransitionListener(object: TransitionAdapter() {
                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    motionLayout?.removeTransitionListener(this)
                    binding.editUsernameIcon.focusAndShowKeyboard()
                }
            })
            binding.accountUsernameEditable.focusAndShowKeyboard()
        }
        binding.submitNewUsernameButton.setOnClickListener {
            val newName = binding.accountUsernameEditable.text.toString()

            requestNewUsername(newName)
        }

        binding.overlayDelegate.setOnClickListener { dismiss() }
    }

    private fun requestNewUsername(newName: String) {
        lifecycleScope.launch {
            val result = viewmodel.requestRename(newName)
            when (result) {
                RenameResult.OK -> {
                    context?.toast("Rename successful!")
                    refreshAccount()
                }
                else -> showRenameError(result)
            }
        }
    }

    private fun startDeleteAccount() {
        TODO()
    }

    private fun refreshAccount() {
        context?.toast("Refreshing account")
        removeTokenObserver()
        viewmodel.refreshTokenForCurrentAccount()
        observeToken()
    }

    private fun showRenameError(error: RenameResult) {
        context?.toast(error.name)
    }

    private fun googleSignIn() {
        startActivityForResult(viewmodel.signInIntent, RC_GOOGLE_SIGNIN)
    }

    private fun googleSignOut() {
        removeTokenObserver()
        viewmodel.signOut().addOnCompleteListener {
            updateUI(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_GOOGLE_SIGNIN -> handleSignInResult(data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResult(data: Intent?) {
        viewmodel.getTokenFromSignInResult(data)
        observeToken()
    }

    private fun observeToken() {
        removeTokenObserver()
        viewmodel.activeUserToken?.observe(viewLifecycleOwner, tokenObserver)
            ?: updateUI(null)
    }

    private fun removeTokenObserver() {
        viewmodel.activeUserToken?.removeObserver(tokenObserver)
    }

    private fun updateUI(token: UserToken?) {
        val state = when(token) {
            null -> State.SIGNED_OUT
            else -> {
                when (currentState) {
                    State.EDIT_USERNAME -> State.MANAGE_ACCOUNT
                    State.MANAGE_ACCOUNT -> State.MANAGE_ACCOUNT
                    else -> State.SIGNED_IN
                }
            }
        }
        transitionToState(state)

        if (token != null) {
            binding.apply {
                bindText(
                    accountUsername to token.username,
                    accountUsernameEditable to token.username,
                    accountNameAndEmail to context?.dotted(token.name, token.email),
                    accountId to token.snommocToken,
                )

                accountAvatar.load(token.photoUrl)
            }
        }
    }

    private fun transitionToState(state: State) {
        if (state == currentState) return
        org.beatonma.commons.core.extensions.withNotNull(binding.root.parent as? ViewGroup) {
            TransitionManager.beginDelayedTransition(it)
        }

        binding.root.transitionToState(
            when (state) {
                State.LOADING -> R.id.state_loading
                State.SIGNED_IN -> R.id.state_signed_in
                State.SIGNED_OUT -> R.id.state_signed_out
                State.MANAGE_ACCOUNT -> R.id.state_manage_account
                State.EDIT_USERNAME -> R.id.state_edit_username
            }
        )
    }

    override fun onBackPressed(): Boolean {
        if (currentState == State.EDIT_USERNAME) {
            transitionToState(State.MANAGE_ACCOUNT)
            return true
        }
        return false
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
