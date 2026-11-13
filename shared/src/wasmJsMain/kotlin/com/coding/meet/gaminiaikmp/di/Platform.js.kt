package com.coding.meet.gaminiaikmp.di

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.coding.meet.gaminiaikmp.utils.TYPE
import kotlinx.browser.window
import org.jetbrains.skia.Image

actual fun getPlatform(): TYPE = TYPE.WEB

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

actual fun isNetworkAvailable(): Boolean {
    return window.navigator.onLine
}
