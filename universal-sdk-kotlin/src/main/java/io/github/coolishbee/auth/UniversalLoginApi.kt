package io.github.coolishbee.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import io.github.coolishbee.R

class UniversalLoginApi {
    companion object {
        /**
         * Google Login API
         */
        fun getGoogleLoginIntent(
            context: Context
        ): Intent {
            return GoogleLoginActivity.getLoginIntent(
                context
            )
        }

        fun googleLogout(
            activity: Activity
        ) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.resources.getString(R.string.google_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(activity, gso)
            googleSignInClient.signOut().addOnCompleteListener(activity) {
            }
        }

        fun getGoogleLoginResultFromIntent(
            intent: Intent?
        ): UniversalLoginResult {
            return intent?.let {
                GoogleLoginActivity.getResultFromIntent(it)
            } ?: run {
                UniversalLoginResult.authenticationAgentError("Callback intent is null")
            }
        }

        /**
         * Facebook Login API
         */
        fun getFacebookLoginIntent(
            context: Context
        ): Intent {
            return FacebookLoginActivity.getLoginIntent(
                context
            )
        }

        fun getFacebookLoginResultFromIntent(
            intent: Intent?
        ): UniversalLoginResult {
            return intent?.let {
                FacebookLoginActivity.getResultFromIntent(it)
            } ?: run {
                UniversalLoginResult.authenticationAgentError("Callback intent is null")
            }
        }

        /**
         * Apple Login with Custom Tabs API
         */
        fun getAppleLoginIntent(
            context: Context
        ): Intent {
            return AppleLoginActivity.getLoginIntent(
                context
            )
        }

        fun getAppleLoginResultFromIntent(
            intent: Intent?
        ): UniversalLoginResult {
            return intent?.let {
                AppleLoginActivity.getResultFromIntent(it)
            } ?: run {
                UniversalLoginResult.authenticationAgentError("Callback intent is null")
            }
        }
    }
}