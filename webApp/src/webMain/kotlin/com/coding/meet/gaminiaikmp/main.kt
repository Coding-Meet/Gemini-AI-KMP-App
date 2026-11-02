package com.coding.meet.gaminiaikmp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.coding.meet.gaminiaikmp.di.initKoin
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport {
        val mainViewModel = koinInject<MainViewModel>()
        App(mainViewModel)
    }
}

