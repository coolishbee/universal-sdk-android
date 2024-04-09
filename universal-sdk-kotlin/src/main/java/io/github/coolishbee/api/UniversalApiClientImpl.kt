package io.github.coolishbee.api

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import io.github.coolishbee.auth.UniversalLoginApi
import io.github.coolishbee.model.UniversalLocalCache
import io.github.coolishbee.ui.CustomTabActivityHelper
import io.github.coolishbee.ui.CustomWebViewFallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UniversalApiClientImpl(
    activity: Activity,
    localCache: UniversalLocalCache
) : UniversalApiClient {

//    private val billingManager = GoogleBillingImpl(activity)
    private val localCacheManager = localCache

    override fun setupSDK() {
    }

    override fun logout(activity: Activity)
    {
        UniversalLoginApi.googleLogout(activity)

    }

//    override fun initBilling(productList: List<String>,
//                             callback: UniversalCallback<List<InAppProduct>>)
//    {
//        val strList = listOf("boxer_1000", "boxer_2000")
//        billingManager.init(strList, callback)
//    }
//
//    override fun restorePurchases() {
//        TODO("Not yet implemented")
//    }
//
//    override fun purchase(activity: Activity,
//                          productId: String,
//                          callback: UniversalCallback<PurchaseResult>)
//    {
//        billingManager.purchase(activity, productId, callback)
//    }

    override fun openCustomTabView(activity: Activity, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        CustomTabActivityHelper.openCustomTab(
            activity,
            customTabsIntent,
            Uri.parse(url),
            CustomWebViewFallback()
        )
    }

//    override fun openImageBanner(activity: Activity) {
//
//        CoroutineScope(Dispatchers.Main).launch {
//            val bannerDialog = BannerDialog(activity, 1.8f, 1.8f)
//            bannerDialog.setOnDismissListener { dialog ->
//                val noShowIsChecked = (dialog as BannerDialog).mNoShowIsChecked
//                Log.d("openImageBanner", noShowIsChecked.toString())
//            }
//            bannerDialog.show()
//        }
//    }

//    override fun getPushToken(): String {
//        return localCacheManager.getPushToken()
//    }

    init {
    }
}