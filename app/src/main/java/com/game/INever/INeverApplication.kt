package com.game.INever

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.amplitude.api.Amplitude
import com.game.INever.core.premium.PremiumSynchronizationManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class INeverApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var premiumSynchronizationManager: PremiumSynchronizationManager

    private fun isMainProcess(): Boolean {
        val pid = Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfo = activityManager.runningAppProcesses?.firstOrNull { it.pid == pid }
        return processInfo?.processName == packageName
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        premiumSynchronizationManager.init()

        Amplitude.getInstance()
            .initialize(this, "a56454c3461ae020bf299e1007256f2de1a4f4d7")
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }
}