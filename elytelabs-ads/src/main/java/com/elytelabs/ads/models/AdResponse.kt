package com.elytelabs.ads.models

import com.google.gson.annotations.SerializedName

data class AdResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("items") val items: List<AdModel>?
)

data class AdModel(
    @SerializedName("id") val id: String,
    @SerializedName("icon") val iconUrl: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String?
)
