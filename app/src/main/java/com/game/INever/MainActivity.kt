package com.game.INever

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.game.INever.ui.INeverApplication
import com.game.INever.utils.LocalActivity
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