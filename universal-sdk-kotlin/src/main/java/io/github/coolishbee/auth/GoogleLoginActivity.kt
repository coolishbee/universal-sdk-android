package io.github.coolishbee.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.github.coolishbee.LoginType
import io.github.coolishbee.R
import java.util.*

class GoogleLoginActivity : Activity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (TextUtils.isEmpty(this.resources.getString(R.string.google_web_client_id))) {
            onAuthenticationFinished(
                UniversalLoginResult.internalError("The google_web_client_id is empty.")
            )
            return
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(this.resources.getString(R.string.google_web_client_id))
            .requestServerAuthCode(this.resources.getString(R.string.google_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signIn()
    }

//    override fun onStart() {
//        super.onStart()
//    }

    public override fun onActivityResult(requestCode: Int,
                                         resultCode: Int,
                                         data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account?.id)
                loginWithGoogle(account!!)

            } catch (e: ApiException) {
                Log.d(TAG, "Google sign in failed", e)

                if(e.statusCode == 12501){
                    googleSignInClient.signOut().addOnCompleteListener(){
                    }
                    onAuthenticationFinished(UniversalLoginResult.canceledError())
                }else{
                    googleSignInClient.signOut().addOnCompleteListener(){
                    }
                    val exLog = String.format(
                        Locale.getDefault(),
                        "Login Failed.(%d)",
                        e.statusCode)
                    onAuthenticationFinished(UniversalLoginResult.authenticationAgentError(exLog))
                }
            }
        }
    }

    private fun loginWithGoogle(acct: GoogleSignInAccount) {

        val profile = UniversalProfile(
            acct.id,
            acct.idToken,
            acct.displayName,
            acct.photoUrl.toString(),
            acct.email,
            acct.serverAuthCode)

        //Log.d(TAG, "loginWithGoogle ${acct.serverAuthCode}")

        val loginResult = UniversalLoginResult.Builder()
            .socialProfile(profile)
            .build()

        onAuthenticationFinished(loginResult)
    }

    private fun onAuthenticationFinished(loginResult: UniversalLoginResult) {
        val resultData = Intent()
        resultData.putExtra(RESPONSE_DATA_KEY_GOOGLE_AUTH_RESULT, loginResult)
        setResult(LoginType.GOOGLE.ordinal, resultData)
        finish()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object{
        private const val TAG = "GoogleLoginActivity"
        private const val RESPONSE_DATA_KEY_GOOGLE_AUTH_RESULT = "google_auth_result"
        private const val PARAM_KEY_AUTHENTICATION_CONFIG = "authentication_config"

        private const val RC_SIGN_IN = 9001

        fun getLoginIntent(
            context: Context
        ): Intent {
            return Intent(context, GoogleLoginActivity::class.java)
        }

        fun getResultFromIntent(intent: Intent): UniversalLoginResult {
            val loginResult: UniversalLoginResult? =
                intent.getParcelableExtra(RESPONSE_DATA_KEY_GOOGLE_AUTH_RESULT)
            return loginResult ?: UniversalLoginResult.authenticationAgentError(
                "Authentication result is not found.")
        }
    }

}