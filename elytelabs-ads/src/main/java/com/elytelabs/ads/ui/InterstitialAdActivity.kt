package com.elytelabs.ads.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation
import com.elytelabs.ads.AdsManager
import com.elytelabs.ads.databinding.InterstitialAdBinding
import androidx.core.net.toUri

class InterstitialAdActivity : AppCompatActivity() {

    private lateinit var binding: InterstitialAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InterstitialAdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val adModel = AdsManager.interstitialAdModel

        if (adModel == null) {
            finish()
            return
        }

        binding.tvAdTitle.text = adModel.title
        binding.tvAdDescription.text = adModel.description
        binding.btnInstall.text = getString(com.elytelabs.ads.R.string.install)

        if (!adModel.iconUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(adModel.iconUrl)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                .into(binding.ivAdImage)
                
            Glide.with(this).load(adModel.iconUrl).into(binding.ivAdIcon)
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        val clickListener = android.view.View.OnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, "market://details?id=${adModel.id}".toUri()))
            } catch (_: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=${adModel.id}".toUri()))
            }
            finish()
        }

        binding.btnInstall.setOnClickListener(clickListener)
        binding.ivAdImage.setOnClickListener(clickListener)
        binding.root.setOnClickListener(clickListener)
    }

    companion object {
        fun show(context: Context) {
            val intent = Intent(context, InterstitialAdActivity::class.java)
            context.startActivity(intent)
        }
    }
}
