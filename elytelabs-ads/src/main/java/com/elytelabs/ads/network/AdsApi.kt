package com.elytelabs.ads.network

import com.elytelabs.ads.models.AdResponse
import retrofit2.Call
import retrofit2.http.GET

import retrofit2.http.Query

interface AdsApi {
    @GET("api/promote")
    fun getAds(
        @Query("limit") limit: Int,
        @Query("type") type: String,
        @Query("exclude") exclude: String
    ): Call<AdResponse>
}
