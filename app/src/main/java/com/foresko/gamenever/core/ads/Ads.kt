package com.foresko.gamenever.core.ads

import android.app.Activity
import android.content.Context
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader

class Ads(val context: Context) {
    private var interstitialAd: InterstitialAd? = null
    private var rewardAd: RewardedAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null
    private var rewardedAdLoader: RewardedAdLoader? = null

    fun initYandexAds() {
        interstitialAdLoader = InterstitialAdLoader(context).apply {
            setAdLoadListener(object : InterstitialAdLoadListener {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                }
            })
        }
        loadInterstitialAd()
    }

    fun initYandexRewardedAds() {
        rewardedAdLoader = RewardedAdLoader(context).apply {
            setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    rewardAd = rewardedAd
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                }
            })
        }
        loadRewerdedAd()
    }

    private fun loadInterstitialAd() {
        val adRequestConfiguration =
            AdRequestConfiguration.Builder("R-M-4122932-1").build()
        interstitialAdLoader?.loadAd(adRequestConfiguration)
    }

    private fun loadRewerdedAd() {
        val adRequestConfiguration =
            AdRequestConfiguration.Builder("R-M-4122932-2").build()
        rewardedAdLoader?.loadAd(adRequestConfiguration)
    }

    fun showInterstitialAd(activity: Activity, handle: () -> Unit) {
        interstitialAd?.apply {
            setAdEventListener(object : InterstitialAdEventListener {
                override fun onAdShown() {
                }

                override fun onAdFailedToShow(adError: AdError) {
                    cleanUpAd()
                    loadInterstitialAd()
                }

                override fun onAdDismissed() {
                    handle()
                    cleanUpAd()
                    loadInterstitialAd()
                }

                override fun onAdClicked() {
                }

                override fun onAdImpression(impressionData: ImpressionData?) {
                }
            })
            show(activity)
        }
    }

    fun showRewardAd(activity: Activity, handle: () -> Unit) {
        rewardAd?.apply {
            setAdEventListener(object : RewardedAdEventListener {
                override fun onAdShown() {
                }

                override fun onAdFailedToShow(adError: AdError) {
                    cleanRewardedUpAd()
                    loadRewerdedAd()
                }

                override fun onAdDismissed() {
                    cleanRewardedUpAd()
                    loadRewerdedAd()
                }

                override fun onAdClicked() {
                }

                override fun onAdImpression(impressionData: ImpressionData?) {
                }

                override fun onRewarded(reward: Reward) {
                    handle()
                }
            })
            show(activity)
        }
    }

    private fun cleanUpAd() {
        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }
    private fun cleanRewardedUpAd() {
        rewardAd?.setAdEventListener(null)
        rewardAd = null
    }
}
