package com.coding.meet.gaminiaikmp.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.utils.AppCoroutineDispatchers
import com.coding.meet.gaminiaikmp.utils.TYPE
import io.github.xxfast.kstore.KStore


expect fun getPlatform(): TYPE
expect suspend fun clipData(clipboardManager: ClipboardManager): String?
expect suspend fun setClipData(clipboardManager: ClipboardManager,message:String)

@Composable
expect fun ImagePicker(showFilePicker: Boolean,onDismissDialog : () -> Unit, onResult: (ByteArray?) -> Unit)

expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect class AppCoroutineDispatchersImpl() : AppCoroutineDispatchers

@Composable
expect fun TextComposable(message:String,isGEMINIMessage:Boolean)

expect  fun isNetworkAvailable(): Boolean

expect suspend fun readGroupKStore(readFun :suspend (KStore<List<Group>>) -> Unit)
expect suspend fun readChatMessageKStore(readFun : suspend (KStore<List<ChatMessage>>) -> Unit)