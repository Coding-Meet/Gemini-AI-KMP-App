package com.coding.meet.gaminiaikmp.di

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.utils.TYPE
import io.github.xxfast.kstore.KStore
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import org.jetbrains.skia.Image
import platform.Foundation.*

actual fun getPlatform(): TYPE = TYPE.MOBILE

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun isNetworkAvailable(): Boolean {
    val url = NSURL.URLWithString("https://www.google.com")
    val request = url?.let { NSURLRequest.requestWithURL(it) }

    val responsePtr = nativeHeap.alloc<ObjCObjectVar<NSURLResponse?>>().ptr
    val errorPtr = nativeHeap.alloc<ObjCObjectVar<NSError?>>().ptr

    val data = request?.let { NSURLConnection.sendSynchronousRequest(it, responsePtr, errorPtr) }
    val response = responsePtr.pointed.value
    val error = errorPtr.pointed.value

    return when {
        data != null && response is NSHTTPURLResponse -> true
        error != null -> false
        else -> false
    }
}

actual suspend fun readGroupKStore(readFun: suspend (KStore<List<Group>>) -> Unit) {}
actual suspend fun readChatMessageKStore(readFun: suspend (KStore<List<ChatMessage>>) -> Unit) {}
