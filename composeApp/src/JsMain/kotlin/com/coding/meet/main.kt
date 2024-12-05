package com.coding.meet

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.coding.meet.gaminiaikmp.App
import com.coding.meet.gaminiaikmp.di.initKoin
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    onWasmReady {
        CanvasBasedWindow(title = "Gemini AI KMP App", canvasElementId = "ComposeTarget") {
            App(koinInject())
        }
    }
}