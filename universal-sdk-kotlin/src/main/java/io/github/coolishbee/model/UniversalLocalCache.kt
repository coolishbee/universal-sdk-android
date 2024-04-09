package io.github.coolishbee.model

import android.content.Context

class UniversalLocalCache(
    _context: Context,
    packageName: String
) {
    private val context = _context
    private val sharedPreferenceKey = SHARED_PREFERENCE_KEY_PREFIX + packageName

    fun clear() {
        context.getSharedPreferences(sharedPreferenceKey, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    fun savePushToken(pushToken: String) {
        context.getSharedPreferences(sharedPreferenceKey, Context.MODE_PRIVATE)
            .edit()
            .putString(DATA_KEY_PUSH_TOKEN, pushToken)
            .apply()
    }

    fun getPushToken() : String {
        val sharedPreferences =
            context.getSharedPreferences(sharedPreferenceKey, Context.MODE_PRIVATE)
        return sharedPreferences.getString(DATA_KEY_PUSH_TOKEN, "")!!
    }


    companion object {
        private const val SHARED_PREFERENCE_KEY_PREFIX = "com.coolishbee.sdk."

        private const val DATA_KEY_PUSH_TOKEN = "pushToken"
    }
}