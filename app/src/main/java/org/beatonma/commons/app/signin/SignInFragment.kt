package org.beatonma.commons.app.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import org.beatonma.commons.app.ui.BaseViewmodelFragment
import org.beatonma.commons.app.ui.ktx.load
import org.beatonma.commons.data.core.repository.UserAccount
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.databinding.FragmentSigninBinding
import org.beatonma.commons.kotlin.extensions.snackbar
import org.beatonma.lib.util.kotlin.extensions.hideViews
import org.beatonma.lib.util.kotlin.extensions.show
import org.beatonma.lib.util.kotlin.extensions.showViews
import javax.inject.Inject

private const val RC_GOOGLE_SIGNIN = 9913

class SignInFragment : BaseViewmodelFragment() {
    private lateinit var binding: FragmentSigninBinding

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    private val viewmodel: SignInViewModel by viewModels { viewmodelFactory }

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
        startActivityForResult(googleSignInClient.signInIntent, RC_GOOGLE_SIGNIN)
    }

    private fun googleSignOut() {
        googleSignInClient.signOut().addOnCompleteListener {
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
        if (token == null) {
            binding.apply {
                showViews(rationale, gSignInButton)
            }
        }
        else {
            binding.apply {
                hideViews(rationale, gSignInButton)

                accountName.text = token.name
                accountUsername.text = token.username
                accountId.text = token.snommocToken

                accountAvatar.load(token.photoUrl)

                signoutButton.show()
            }
        }
    }
}
