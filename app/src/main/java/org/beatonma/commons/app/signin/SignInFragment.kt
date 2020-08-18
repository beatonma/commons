package org.beatonma.commons.app.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.common.SignInButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.databinding.FragmentSigninBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.dotted
import org.beatonma.commons.kotlin.extensions.load

private const val RC_GOOGLE_SIGNIN = 9913

@AndroidEntryPoint
class SignInFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSigninBinding

    private val tokenObserver = Observer<IoResult<UserToken>> { result ->
        updateUI(result.data)
    }

    private val viewmodel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gSignInButton.apply {
            setSize(SignInButton.SIZE_STANDARD)
            setOnClickListener { googleSignIn() }
        }
        binding.signoutButton.setOnClickListener { googleSignOut() }

        viewmodel.getTokenForCurrentSignedInAccount()
        observeToken()
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
            null -> R.id.state_signed_out
            else -> R.id.state_signed_in
        }
        binding.root.transitionToState(state)

        if (token != null) {
            binding.apply {
                bindText(
                    accountUsername to token.username,
                    accountNameAndEmail to context?.dotted(token.name, token.email),
                    accountId to token.snommocToken,
                )

                accountAvatar.load(token.photoUrl)
            }
        }
    }
}
