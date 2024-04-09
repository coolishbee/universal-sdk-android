package io.github.coolishbee.api

import android.app.Activity
import io.github.coolishbee.model.UniversalLocalCache

class UniversalApiClientBuilder(
    private val activity: Activity
) {
    private val context = activity.applicationContext

    fun build(): UniversalApiClient {
        return UniversalApiClientImpl(
            activity,
            UniversalLocalCache(context, context.packageName)
        )
    }


}