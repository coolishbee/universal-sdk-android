package io.github.coolishbee.api

import android.app.Activity

interface UniversalApiClient {

    fun setupSDK()

    fun logout(activity: Activity)

//    fun initBilling(productList: List<String>,
//                    callback: UniversalCallback<List<InAppProduct>>)
//
//    fun restorePurchases()
//
//    fun purchase(activity: Activity,
//                 productId: String,
//                 callback: UniversalCallback<PurchaseResult>)

    fun openCustomTabView(activity: Activity,
                          url: String)

//    fun openImageBanner(activity: Activity)

//    fun getPushToken() : String
}