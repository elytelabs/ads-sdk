package com.elytelabs.ads.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AdsClient {
    private const val BASE_URL = "https://elytelabs.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: AdsApi by lazy {
        retrofit.create(AdsApi::class.java)
    }
}
