package com.elytelabs.ads.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.elytelabs.ads.AdsManager
import com.elytelabs.ads.databinding.BannerAdBinding

class BannerAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: BannerAdBinding = BannerAdBinding.inflate(LayoutInflater.from(context), this, true)

    private val refreshHandler = Handler(Looper.getMainLooper())
    private val refreshRunnable = object : Runnable {
        override fun run() {
            loadAd()
            refreshHandler.postDelayed(this, 30_000L) // Refresh every 30 seconds
        }
    }

    init {
        visibility = View.GONE
    }

    fun loadAd() {
        val adModel = AdsManager.bannerAdModel
        if (adModel != null) {
            binding.tvAdTitle.text = adModel.title
            binding.tvAdDescription.text = adModel.description
            binding.btnInstall.text = context.getString(com.elytelabs.ads.R.string.install)

            if (!adModel.iconUrl.isNullOrEmpty()) {
                Glide.with(this).load(adModel.iconUrl).into(binding.ivAdIcon)
            }

            val clickListener = OnClickListener {
                openPlayStore(adModel.id)
            }
            binding.root.setOnClickListener(clickListener)
            binding.btnInstall.setOnClickListener(clickListener)
            visibility = View.VISIBLE
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshHandler.postDelayed(refreshRunnable, 30_000L)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        refreshHandler.removeCallbacks(refreshRunnable)
    }

    private fun openPlayStore(packageName: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }
}
