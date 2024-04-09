package io.github.coolishbee.ui

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import io.github.coolishbee.R

class CustomWebViewActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sdk_webview_activity)
        val url = intent.getStringExtra(EXTRA_URL)
        val webView = findViewById<WebView>(R.id.webview)
        webView.webViewClient = WebViewClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        title = url
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        webView.loadUrl(url!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Respond to the action bar's Up/Home button
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_URL = "extra.url"
    }
}