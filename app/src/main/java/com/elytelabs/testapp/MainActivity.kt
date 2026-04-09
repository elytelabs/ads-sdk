package com.elytelabs.testapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.elytelabs.ads.AdsManager
import com.elytelabs.ads.ui.InterstitialAdActivity
import com.elytelabs.testapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        AdsManager.init(this)

        val listener = object : AdsManager.AdLoadListener {
            override fun onAdsLoaded() {
                binding.bannerAdView.loadAd()
            }
        }
        AdsManager.addListener(listener)

        binding.btnShowInterstitial.setOnClickListener {
            if (AdsManager.isInterstitialLoaded()) {
                InterstitialAdActivity.show(this)
            } else {
                Toast.makeText(this, getString(R.string.interstitial_not_loaded), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
