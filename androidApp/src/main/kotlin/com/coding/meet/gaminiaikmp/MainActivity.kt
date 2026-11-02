package com.coding.meet.gaminiaikmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel
import com.coding.meet.gaminiaikmp.theme.borderColor
import com.coding.meet.gaminiaikmp.utils.Screens
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(
                    0
                ),
                navigationBarStyle = SystemBarStyle.light(
                    0,255
                )
            )
            val mainViewModel = koinInject<MainViewModel>()
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