package com.coding.meet.gaminiaikmp

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.mp.KoinPlatform
import utils.Screens
import presentation.screens.mainscreen.MainViewModel
import theme.borderColor

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = borderColor.toArgb()
        window.navigationBarColor = borderColor.toArgb()
        installSplashScreen()
        setContent {
            val mainViewModel: MainViewModel = KoinPlatform.getKoin().get()
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (mainViewModel.screens == Screens.DETAIL) {
                        mainViewModel.screens = Screens.MAIN
                        mainViewModel.currentPos = -1
                    } else {
                        finish()
                    }
                }
            })
            App(mainViewModel)
        }
    }
}