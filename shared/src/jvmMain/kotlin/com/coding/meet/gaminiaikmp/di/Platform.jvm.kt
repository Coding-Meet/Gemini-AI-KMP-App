package com.coding.meet.gaminiaikmp.di

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.coding.meet.gaminiaikmp.utils.TYPE
import org.jetbrains.skia.Image
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

actual fun getPlatform(): TYPE = TYPE.DESKTOP

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

actual fun isNetworkAvailable(): Boolean {
    return try {
        val socket = Socket()
        socket.connect(InetSocketAddress("google.com", 80), 1500)
        socket.close()
        true
    } catch (e: IOException) {
        false
    }
}
