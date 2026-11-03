package com.coding.meet.gaminiaikmp

import androidx.compose.ui.window.ComposeUIViewController
import com.coding.meet.gaminiaikmp.di.initKoin
fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
