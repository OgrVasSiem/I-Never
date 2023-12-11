package com.foresko.gamenever

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.Room
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.amplitude.api.Amplitude
import com.foresko.gamenever.core.ads.Ads
import com.foresko.gamenever.core.premium.PremiumSynchronizationManager
import com.foresko.gamenever.dataBase.AppDatabase
import com.google.firebase.FirebaseApp
import com.yandex.mobile.ads.common.MobileAds
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class INeverApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var premiumSynchronizationManager: PremiumSynchronizationManager

    @Inject
    lateinit var ads: Ads

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "app_db").build()
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        Amplitude.getInstance()
            .initialize(this, "06b57e7f021b7247be2003d012907f73")

        MobileAds.initialize(this) {
            ads.initYandexAds()
            ads.initYandexRewardedAds()
        }

        Amplitude.getInstance().logEvent("app_opened")

        premiumSynchronizationManager.init()
    }


    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }
}