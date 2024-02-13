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
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import platform.Foundation.NSURL
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.Foundation.sendSynchronousRequest
import platform.Foundation.NSError
import platform.Foundation.NSErrorDomain
import platform.Foundation.NSURLConnection
import presentation.components.CommonTextComposable
import utils.AppCoroutineDispatchers
import utils.TYPE


actual fun getPlatform(): TYPE = TYPE.IOS

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
actual fun ImagePicker(showFilePicker: Boolean,onDismissDialog : () -> Unit, onResult: (ByteArray?) -> Unit) {
    val scope = rememberCoroutineScope()
    val fileType = listOf("jpg", "jpeg","png")
    FilePicker(show = showFilePicker, fileExtensions = fileType) { file ->
        scope.launch {
            file?.getFileByteArray()?.let { onResult(it) }
        }
        onDismissDialog()

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
    val url = NSURL.URLWithString("https://www.google.com")
    val request = NSURLRequest.requestWithURL(url)

    val response: AutoreleasingUnsafeMutablePointer<NSURLResponse?> = null
    val error: AutoreleasingUnsafeMutablePointer<NSError?> = null

    val data = NSURLConnection.sendSynchronousRequest(request, response, error)

    if (data != null && (response != null && response.pointed !is NSHTTPURLResponse)) {
        return true
    } else {
        val nsError = error?.pointed
        if (nsError != null && nsError.domain == NSErrorDomain.NSURLErrorDomain) {
            return nsError.code != -1009
        }
    }

    return false
}
actual suspend fun readGroupKStore(readFun :suspend (KStore<List<Group>>) -> Unit) {
}
actual suspend fun readChatMessageKStore(readFun :suspend (KStore<List<ChatMessage>>) -> Unit) {
}