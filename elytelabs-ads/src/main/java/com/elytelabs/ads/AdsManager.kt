package com.elytelabs.ads

import android.content.Context
import android.util.Log
import com.elytelabs.ads.models.AdModel
import com.elytelabs.ads.models.AdResponse
import com.elytelabs.ads.network.AdsClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AdsManager {
    private const val TAG = "AdsManager"

    var bannerAdModel: AdModel? = null
        private set
    
    var interstitialAdModel: AdModel? = null
        private set

    interface AdLoadListener {
        fun onAdsLoaded()
    }

    private val listeners = mutableListOf<AdLoadListener>()

    fun addListener(listener: AdLoadListener) {
        listeners.add(listener)
        if (bannerAdModel != null || interstitialAdModel != null) {
            listener.onAdsLoaded()
        }
    }

    fun removeListener(listener: AdLoadListener) {
        listeners.remove(listener)
    }

    fun init(context: Context) {
        fetchAds()
    }

    private fun fetchAds() {
        AdsClient.api.getAds().enqueue(object : Callback<AdResponse> {
            override fun onResponse(call: Call<AdResponse>, response: Response<AdResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val items = response.body()?.items
                    if (!items.isNullOrEmpty()) {
                        bannerAdModel = items.firstOrNull()
                        interstitialAdModel = if (items.size > 1) items[1] else bannerAdModel
                        Log.d(TAG, "Ads fetched successfully.")
                        ArrayList(listeners).forEach { it.onAdsLoaded() }
                    } else {
                        Log.e(TAG, "Ads JSON items list is empty.")
                    }
                } else {
                    Log.e(TAG, "Failed to fetch ads. Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<AdResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch ads due to network error", t)
            }
        })
    }

    fun isInterstitialLoaded(): Boolean {
        return interstitialAdModel != null
    }
}
