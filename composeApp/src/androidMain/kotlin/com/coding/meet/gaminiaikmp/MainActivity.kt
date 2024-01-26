package com.coding.meet.gaminiaikmp

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import utils.Screens
import presenation.screens.main.MainViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = remember { MainViewModel() }
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.screens == Screens.DETAIL) {
                        viewModel.screens = Screens.MAIN
                    } else {
                        finish()
                    }
                }
            })
            App(viewModel)
        }
    }
}