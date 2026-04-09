package com.elytelabs.ads.models

import com.google.gson.annotations.SerializedName

/**
 * Top-level response from the Elyte Labs promotion API (`/api/promote`).
 *
 * @property success `true` if the API call was handled correctly.
 * @property items   the list of promotional [AdModel] entries, or `null` if none.
 */
data class AdResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("items") val items: List<AdModel>?
)

/**
 * Represents a single promotional ad item.
 *
 * @property id              the app's package name (e.g. `com.elytelabs.muslimnames`).
 * @property iconUrl         URL pointing to the app's icon on Google Play CDN.
 * @property featureGraphic  URL to the app's Play Store feature graphic (1024x500), if available.
 * @property title           the display name of the promoted app.
 * @property description     a short promotional tagline.
 * @property url             the full Play Store listing URL (optional).
 * @property category        the app's Play Store category (optional).
 * @property rating          the app's star rating (optional).
 * @property installs        install count string, e.g. "10,000+" (optional).
 */
data class AdModel(
    @SerializedName("id") val id: String,
    @SerializedName("icon") val iconUrl: String?,
    @SerializedName("featureGraphic") val featureGraphic: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("url") val url: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("installs") val installs: String?
)
