package di

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import domain.model.ChatMessage
import domain.model.Group
import io.github.xxfast.kstore.KStore
import utils.*

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