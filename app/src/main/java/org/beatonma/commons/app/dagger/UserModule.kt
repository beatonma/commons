package org.beatonma.commons.app.dagger

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.BuildConfig.GOOGLE_SIGNIN_CLIENT_ID
import javax.inject.Singleton

@Module @InstallIn(ApplicationComponent::class)
class UserModule {
    @Singleton
    @Provides
    fun providesGoogleSignInOptions(): GoogleSignInOptions = GoogleSignInOptions.Builder(
        GoogleSignInOptions.DEFAULT_SIGN_IN
    )
        .requestEmail()
        .requestIdToken(GOOGLE_SIGNIN_CLIENT_ID)
        .build()

    @Singleton
    @Provides
    fun providesGoogleSignInClient(@ApplicationContext context: Context, googleSignInOptions: GoogleSignInOptions): GoogleSignInClient =
        GoogleSignIn.getClient(context, googleSignInOptions)
}
