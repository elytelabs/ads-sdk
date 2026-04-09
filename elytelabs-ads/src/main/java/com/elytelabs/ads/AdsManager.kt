package com.elytelabs.ads

import android.content.Context
import android.util.Log
import com.elytelabs.ads.models.AdModel
import com.elytelabs.ads.models.AdResponse
import com.elytelabs.ads.network.AdsClient
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

object AdsManager {
    private const val TAG = "AdsManager"

    private const val PREFS_NAME = "ElyteAdsCache"
    private const val KEY_CACHED_ADS = "cached_ads_json"
    private const val KEY_LAST_FETCH = "last_fetch_time"
    private val CACHE_DURATION_MS = TimeUnit.HOURS.toMillis(24)

    private var cachedAdsList: List<AdModel> = emptyList()

    val bannerAdModel: AdModel?
        get() = getRandomAd()
    
    val interstitialAdModel: AdModel?
        get() = getRandomAd()

    interface AdLoadListener {
        fun onAdsLoaded()
    }

    private val listeners = mutableListOf<AdLoadListener>()

    fun addListener(listener: AdLoadListener) {
        listeners.add(listener)
        if (cachedAdsList.isNotEmpty()) {
            listener.onAdsLoaded()
        }
    }

    fun removeListener(listener: AdLoadListener) {
        listeners.remove(listener)
    }

    private fun getRandomAd(): AdModel? {
        return cachedAdsList.randomOrNull()
    }

    fun init(context: Context) {
        if (cachedAdsList.isNotEmpty()) return

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastFetchTime = prefs.getLong(KEY_LAST_FETCH, 0L)
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastFetchTime < CACHE_DURATION_MS) {
            val cachedJson = prefs.getString(KEY_CACHED_ADS, null)
            if (!cachedJson.isNullOrEmpty()) {
                try {
                    val type = object : TypeToken<List<AdModel>>() {}.type
                    val items: List<AdModel> = Gson().fromJson(cachedJson, type)
                    if (items.isNotEmpty()) {
                        cachedAdsList = items
                        preloadIcons(context, items)
                        Log.d(TAG, "Ads loaded successfully from 24-hour cache.")
                        ArrayList(listeners).forEach { it.onAdsLoaded() }
                        return
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to parse cached ads JSON.", e)
                }
            }
        }

        fetchAds(context)
    }

    private fun fetchAds(context: Context) {
        val packageName = context.packageName
        
        AdsClient.api.getAds(limit = 50, type = "apps", exclude = packageName).enqueue(object : Callback<AdResponse> {
            override fun onResponse(call: Call<AdResponse>, response: Response<AdResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val items = response.body()?.items
                    if (!items.isNullOrEmpty()) {
                        cachedAdsList = items
                        preloadIcons(context, items)
                        
                        try {
                            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                            val jsonString = Gson().toJson(items)
                            prefs.edit()
                                .putString(KEY_CACHED_ADS, jsonString)
                                .putLong(KEY_LAST_FETCH, System.currentTimeMillis())
                                .apply()
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to cache ads JSON.", e)
                        }

                        Log.d(TAG, "Ads fetched successfully from network and cached.")
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

    private fun preloadIcons(context: Context, items: List<AdModel>) {
        val appContext = context.applicationContext
        items.take(20).forEach { ad ->
            if (!ad.iconUrl.isNullOrEmpty()) {
                Glide.with(appContext).load(ad.iconUrl).preload()
            }
        }
    }

    fun isInterstitialLoaded(): Boolean {
        return cachedAdsList.isNotEmpty()
    }
}
