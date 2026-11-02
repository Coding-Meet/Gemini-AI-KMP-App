package com.coding.meet.gaminiaikmp

import androidx.compose.ui.window.ComposeUIViewController
import com.coding.meet.gaminiaikmp.di.initKoin
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel
import org.koin.compose.koinInject

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    val mainViewModel =  koinInject<MainViewModel>()
    App(mainViewModel)
}
