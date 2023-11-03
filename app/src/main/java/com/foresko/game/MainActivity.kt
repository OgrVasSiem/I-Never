package com.foresko.game

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.appodeal.ads.Appodeal
import com.foresko.game.ui.INeverApplication
import com.foresko.game.utils.LocalActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /*@Inject
    lateinit var languageManager: LanguageManager*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /* val systemRegion = languageManager.setLocale(this)*/

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        Appodeal.initialize(
            activity = this,
            appKey = "2a6f87e8960ab6840bba8b2f6144bec511f76d6bb1d0e211",
            adTypes = Appodeal.INTERSTITIAL,
            hasConsent = true
        )

        setContent {
            CompositionLocalProvider(
                LocalActivity provides this@MainActivity,
                /*LocalSystemRegion provides systemRegion*/
            ) {
                INeverApplication()
            }
        }
    }
}