package com.coding.meet.gaminiaikmp.di

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.utils.TYPE
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import kotlinx.browser.window
import org.jetbrains.skia.Image

actual fun getPlatform(): TYPE = TYPE.WEB

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

actual fun isNetworkAvailable(): Boolean {
    return window.navigator.onLine
}

actual suspend fun readGroupKStore(readFun: suspend (KStore<List<Group>>) -> Unit) {
    val store: KStore<List<Group>> = storeOf("group_list")
    readFun(store)
}

actual suspend fun readChatMessageKStore(readFun: suspend (KStore<List<ChatMessage>>) -> Unit) {
    val store: KStore<List<ChatMessage>> = storeOf("chat_message_list")
    readFun(store)
}
