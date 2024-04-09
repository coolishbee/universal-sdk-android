package io.github.coolishbee.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import io.github.coolishbee.ui.CustomTabActivityHelper

class AppleLoginBrowserFallback : CustomTabActivityHelper.AppleLoginFallback{
    override fun openUri(activity: Activity?, uri: Uri?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, uri)
        activity?.startActivity(browserIntent)
    }
}