package com.foresko.gamenever

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
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

    private fun isMainProcess(): Boolean {
        val pid = Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfo = activityManager.runningAppProcesses?.firstOrNull { it.pid == pid }
        return processInfo?.processName == packageName
    }

    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "app_db").build()
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        MobileAds.initialize(this) {
            ads.initYandexAds()
            ads.initYandexRewardedAds()
        }

        Amplitude.getInstance().logEvent("AppOpened")

        premiumSynchronizationManager.init()
    }


    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }
}