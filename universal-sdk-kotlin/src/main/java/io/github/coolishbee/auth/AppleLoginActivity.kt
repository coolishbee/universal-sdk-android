package io.github.coolishbee.auth

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import com.google.gson.Gson
import io.github.coolishbee.LoginType
import io.github.coolishbee.R
import io.github.coolishbee.ui.CustomTabActivityHelper
import io.github.coolishbee.ui.CustomWebViewFallback
import io.github.coolishbee.utils.UniversalUtils
import java.io.UnsupportedEncodingException
import java.util.*

class AppleLoginActivity : Activity(){
    private val authUrl = "https://appleid.apple.com/auth/authorize"
    private val responseType = "code%20id_token"
    private val responseMode = "form_post"
    private val scope = "name%20email"
    private val state = UUID.randomUUID().toString()
    private val rawNonce = UniversalUtils.sha256(UniversalUtils.generateNonce(32))

    private var activityIsHidden = false
    private var customTabIsOpened = false
    private var loginPending = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = Uri.parse(
            authUrl
                    + "?response_type=$responseType"
                    + "&response_mode=$responseMode"
                    + "&client_id=${getString(R.string.apple_client_id)}"
                    + "&scope=$scope"
                    + "&state=$state"
                    + "&nonce=$rawNonce"
                    + "&redirect_uri=${getString(R.string.redirect_url)}")

        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        CustomTabActivityHelper.openCustomTabAppleLogin(
            this,
            customTabsIntent,
            url,
            AppleLoginBrowserFallback())

        customTabIsOpened = true
    }

    override fun onPause() {
        super.onPause()
        activityIsHidden = true
    }

    // finish this activity when the user returned to this activity before login process ends
    override fun onResume() {
        super.onResume()

        if (activityIsHidden){
            activityIsHidden = false
            if (customTabIsOpened && !loginPending) {
                onAuthenticationFinished(UniversalLoginResult.canceledError())
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        loginPending = true

        if (intent?.data != null) {
            parseUri(intent.data!!)
        } else {
            onAuthenticationFinished(
                UniversalLoginResult.authenticationAgentError("intent is null"))
        }
    }

    private fun parseUri(url: Uri){
        val codeParam = url.getQueryParameter("code")
        val stateParam = url.getQueryParameter("state")
        val error = url.getQueryParameter("error")
        val idTokenParam = url.getQueryParameter("id_token")
        val userParam = url.getQueryParameter("user")

        when {
            error == "user_cancelled_authorize" -> {
                //Log.d(TAG, "user_cancelled_authorize")
                onAuthenticationFinished(UniversalLoginResult.canceledError())
            }
            codeParam == null -> {
                //Log.d(TAG, "code not returned")
                onAuthenticationFinished(
                    UniversalLoginResult.authenticationAgentError("code not returned"))
            }
            stateParam != state -> {
                //Log.d(TAG, "state code unmatched")
                onAuthenticationFinished(
                    UniversalLoginResult.authenticationAgentError("state code unmatched"))
            }
            idTokenParam == null -> {
                //Log.d(TAG, "idTokenParam not returned")
                onAuthenticationFinished(
                    UniversalLoginResult.authenticationAgentError("idTokenParam not returned"))
            }
            else -> {
                val gson = Gson()
                if(userParam != null)
                    Log.d(TAG, userParam)
                //Log.d(TAG, codeParam)
                val nameInfo = gson.fromJson(userParam, AppleUser::class.java)
                val decodedIdToken = decodeJWT(idTokenParam)
                val loginInfo = gson.fromJson(decodedIdToken, AppleLoginInfo::class.java)

                if (loginInfo.nonce_supported) {
                    if (loginInfo.nonce != rawNonce){
                        //Log.d(TAG, "Nonce unmatched")
                        onAuthenticationFinished(
                            UniversalLoginResult.authenticationAgentError("Nonce unmatched"))
                    }else{
                        var displayName = "Unknown"
                        if(nameInfo != null){
                            val firstName = nameInfo.name.firstName
                            val lastName = nameInfo.name.lastName
                            displayName = "$firstName$lastName"
                        }

                        val profile = UniversalProfile(
                            loginInfo.sub,
                            idTokenParam,
                            displayName,
                            null,
                            loginInfo.email,
                            codeParam)
                        loginWithApple(profile)
                    }
                }
            }
        }
    }

    private fun loginWithApple(profile: UniversalProfile) {
        val loginResult = UniversalLoginResult.Builder()
            .socialProfile(profile)
            .build()

        onAuthenticationFinished(loginResult)
    }

    private fun onAuthenticationFinished(loginResult: UniversalLoginResult) {
        val resultData = Intent()
        resultData.putExtra(RESPONSE_DATA_KEY_APPLE_AUTH_RESULT, loginResult)
        setResult(LoginType.APPLE.ordinal, resultData)
        finish()
    }

    private fun decodeJWT(JWT: String): String {
        var decodedJson = ""
        try {
            val split = JWT.split("\\.".toRegex()).toTypedArray()

            val decodedHeader = Base64.decode(split[0], Base64.URL_SAFE)
            //Log.d(TAG, "header: ${String(decodedHeader, charset("UTF-8"))}")

            val decodedBody = Base64.decode(split[1], Base64.URL_SAFE)
            decodedJson = String(decodedBody, charset("UTF-8"))
            //Log.d(TAG, "body: $decodedJson")

        }catch (e: UnsupportedEncodingException){
            e.printStackTrace()
        }
        return decodedJson
    }

    companion object {
        private const val TAG = "AppleLoginActivity"
        private const val RESPONSE_DATA_KEY_APPLE_AUTH_RESULT = "apple_auth_result"

        fun getLoginIntent(
            context: Context
        ): Intent {
            return Intent(context, AppleLoginActivity::class.java)
        }

        fun getResultFromIntent(intent: Intent): UniversalLoginResult {
            val loginResult: UniversalLoginResult? =
                intent.getParcelableExtra(RESPONSE_DATA_KEY_APPLE_AUTH_RESULT)
            return loginResult ?: UniversalLoginResult.authenticationAgentError(
                "Authentication result is not found.")
        }
    }
}