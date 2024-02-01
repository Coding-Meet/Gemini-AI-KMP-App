package com.coding.meet.gaminiaikmp

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import org.koin.mp.KoinPlatform
import utils.Screens
import screens.main.MainViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = KoinPlatform.getKoin().get()
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (mainViewModel.screens == Screens.DETAIL) {
                        mainViewModel.screens = Screens.MAIN
                    } else {
                        finish()
                    }
                }
            })
            App(mainViewModel)
        }
    }
}