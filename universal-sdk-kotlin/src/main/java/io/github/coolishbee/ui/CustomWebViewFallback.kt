package io.github.coolishbee.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri

class CustomWebViewFallback : CustomTabActivityHelper.CustomTabFallback {
    override fun openUri(activity: Activity?, uri: Uri?) {
        val intent = Intent(activity, CustomWebViewActivity::class.java)
        intent.putExtra(CustomWebViewActivity.EXTRA_URL, uri.toString())
        activity?.startActivity(intent)
    }
}