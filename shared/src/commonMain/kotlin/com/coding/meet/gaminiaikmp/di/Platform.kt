package com.coding.meet.gaminiaikmp.di

import androidx.compose.ui.graphics.ImageBitmap
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.utils.TYPE
import io.github.xxfast.kstore.KStore

expect fun getPlatform(): TYPE

expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect fun isNetworkAvailable(): Boolean

expect suspend fun readGroupKStore(readFun: suspend (KStore<List<Group>>) -> Unit)
expect suspend fun readChatMessageKStore(readFun: suspend (KStore<List<ChatMessage>>) -> Unit)
