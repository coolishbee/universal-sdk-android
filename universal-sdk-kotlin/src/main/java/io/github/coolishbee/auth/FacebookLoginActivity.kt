package io.github.coolishbee.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import io.github.coolishbee.LoginType
import io.github.coolishbee.R
import org.json.JSONException

class FacebookLoginActivity : Activity() {

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (TextUtils.isEmpty(this.resources.getString(R.string.facebook_app_id))) {
            onAuthenticationFinished(
                UniversalLoginResult.internalError("The facebook_app_id is empty.")
            )
            return
        }

        facebookAuth()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun facebookAuth() {
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("public_profile", "email")
        )

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult>
        {
            override fun onSuccess(result: LoginResult) {
                //Log.d(TAG, "facebook:onSuccess:$result")
                loginWithFacebook(result)
            }

            override fun onCancel() {
                //Log.d(TAG, "facebook:onCancel")
                onAuthenticationFinished(UniversalLoginResult.canceledError())
            }

            override fun onError(error: FacebookException) {
                //Log.d(TAG, "facebook:onError", error)
                onAuthenticationFinished(UniversalLoginResult.authenticationAgentError(error.toString()))
            }
        })
    }

    private fun loginWithFacebook(fbLoginResult: LoginResult) {
        val request = GraphRequest.newMeRequest(
            fbLoginResult.accessToken
        ) { obj, _ ->
            try {
                val name = obj!!.getString("name")
                val email = obj.getString("email")
                val photoURL = obj.getJSONObject("picture")
                    .getJSONObject("data")
                    .getString("url")

                val profile = UniversalProfile(
                    fbLoginResult.accessToken.userId,
                    fbLoginResult.accessToken.token,
                    name,
                    photoURL,
                    email,
                    "")

                val loginResult = UniversalLoginResult.Builder()
                    .socialProfile(profile)
                    .build()

                onAuthenticationFinished(loginResult)

            }catch (e: JSONException){
                Toast.makeText(
                    this,
                    "Facebook Authentication Failed.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id, name, email, picture.type(normal)")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun onAuthenticationFinished(loginResult: UniversalLoginResult) {
        val resultData = Intent()
        resultData.putExtra(RESPONSE_DATA_KEY_FACEBOOK_AUTH_RESULT, loginResult)
        setResult(LoginType.FACEBOOK.ordinal, resultData)
        finish()
    }

    companion object{
        private const val TAG = "FacebookLoginActivity"
        private const val RESPONSE_DATA_KEY_FACEBOOK_AUTH_RESULT = "facebook_auth_result"
        private const val PARAM_KEY_AUTHENTICATION_CONFIG = "authentication_config"

        fun getLoginIntent(
            context: Context
        ): Intent {
            return Intent(context, FacebookLoginActivity::class.java)
        }

        fun getResultFromIntent(intent: Intent): UniversalLoginResult {
            val loginResult: UniversalLoginResult? =
                intent.getParcelableExtra(RESPONSE_DATA_KEY_FACEBOOK_AUTH_RESULT)
            return loginResult ?: UniversalLoginResult.authenticationAgentError(
                "Authentication result is not found.")
        }
    }
}