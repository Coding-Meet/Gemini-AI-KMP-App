package di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import domain.model.ChatMessage
import domain.model.Group
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import presentation.components.CommonTextComposable
import utils.AppCoroutineDispatchers
import utils.TYPE
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


actual fun getPlatform(): TYPE = TYPE.DESKTOP

actual suspend fun clipData(clipboardManager: ClipboardManager): String? {
    return clipboardManager.getText()?.text.toString().trim()
}
actual suspend fun setClipData(clipboardManager: ClipboardManager,message:String) {
    return clipboardManager.setText(AnnotatedString(message))
}
actual class AppCoroutineDispatchersImpl actual constructor() : AppCoroutineDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}

@Composable
actual fun ImagePicker(showFilePicker: Boolean, onResult: (ByteArray?) -> Unit) {
    val scope = rememberCoroutineScope()
    val fileType = listOf("jpg", "png")
    FilePicker(show = showFilePicker, fileExtensions = fileType) { file ->
        scope.launch {
            file?.getFileByteArray()?.let { onResult(it) }
        }

    }
}

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}
@Composable
actual fun TextComposable(message:String,isGEMINIMessage:Boolean) {
    CommonTextComposable(message,isGEMINIMessage)
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
actual suspend fun readGroupKStore(readFun :suspend (KStore<List<Group>>) -> Unit) {
}
actual suspend fun readChatMessageKStore(readFun :suspend (KStore<List<ChatMessage>>) -> Unit) {
}