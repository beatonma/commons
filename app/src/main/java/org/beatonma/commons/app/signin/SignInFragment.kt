package org.beatonma.commons.app.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.data.core.repository.UserAccount
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.databinding.FragmentSigninBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.load
import org.beatonma.commons.kotlin.extensions.snackbar

private const val RC_GOOGLE_SIGNIN = 9913

@AndroidEntryPoint
class SignInFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSigninBinding

    private val viewmodel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater)
        return binding.root
    }

    private fun observeAccount(account: UserAccount) {
        val token = viewmodel.getTokenForAccount(account)
        if (token != null) {
            token.observe(viewLifecycleOwner) { result ->
                updateUI(result.data)
            }
        }
        else {
            updateUI(null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gSignInButton.apply {
            setSize(SignInButton.SIZE_STANDARD)
            setOnClickListener { googleSignIn() }
        }
        binding.signoutButton.setOnClickListener { googleSignOut() }

        val activeToken = viewmodel.getTokenForCurrentSignedInAccount()
        if (activeToken != null) {
            activeToken.observe(viewLifecycleOwner) {
                updateUI(it.data)
            }
        }
        else {
            updateUI(null)
        }
    }

    private fun googleSignIn() {
        startActivityForResult(viewmodel.signInIntent, RC_GOOGLE_SIGNIN)
    }

    private fun googleSignOut() {
        viewmodel.signOut().addOnCompleteListener {
            updateUI(null)
            snackbar("Signed out!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_GOOGLE_SIGNIN -> handleSignInResult(data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleSignInResult(data: Intent?) {
        val account = viewmodel.getGoogleAccountFromSignInResult(
            GoogleSignIn.getSignedInAccountFromIntent(data)
        )

        if (account != null) {
            observeAccount(account)
        }
    }

    private fun updateUI(token: UserToken?) {
        val state = when(token) {
            null -> R.id.state_signed_out
            else -> R.id.state_signed_in
        }
        binding.root.transitionToState(state)

        if (token != null) {
            binding.apply {
                bindText(
                    accountName to token.name,
                    accountUsername to token.username,
                    accountId to token.snommocToken,
                )

                accountAvatar.load(token.photoUrl)
            }
        }
    }
}
