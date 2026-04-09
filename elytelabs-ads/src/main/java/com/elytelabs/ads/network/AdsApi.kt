package com.elytelabs.ads.network

import com.elytelabs.ads.models.AdResponse
import retrofit2.Call
import retrofit2.http.GET

interface AdsApi {
    @GET("api/promote")
    fun getAds(): Call<AdResponse>
}
