package com.foresko.gamenever.core.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.amplitude.api.Amplitude
import com.appodeal.ads.Appodeal
import com.appodeal.ads.InterstitialCallbacks
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class Ads(
    val context: Context
) {
    var interstitialAd: InterstitialAd? = null
        private set

    fun initAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            "ca-app-pub-5693620155650275~9805635036",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d("ads", "error = $it") }
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {

                    interstitialAd = ad
                }
            })
    }

    fun showAd(activity: Activity, handle: () -> Unit) {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    handle()
                    interstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    interstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                }
            }

            Amplitude.getInstance().logEvent("advertisement")

            ad.show(activity)
        } ?: run {
            showAppodealInterstitialAds(activity = activity, handle = handle)
        }
    }

    private fun showAppodealInterstitialAds(activity: Activity, handle: () -> Unit) {
        Appodeal.setInterstitialCallbacks(object : InterstitialCallbacks {
            override fun onInterstitialExpired() {}
            override fun onInterstitialFailedToLoad() {}
            override fun onInterstitialLoaded(isPrecache: Boolean) {}
            override fun onInterstitialShowFailed() {}

            override fun onInterstitialClicked() {}

            override fun onInterstitialClosed() {
                handle()
            }

            override fun onInterstitialShown() {}

        })

        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
            Amplitude.getInstance().logEvent("advertisement")

            Appodeal.show(activity, Appodeal.INTERSTITIAL)
        } else {
            handle()
        }
    }
}