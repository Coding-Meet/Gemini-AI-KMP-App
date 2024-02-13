package di

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import domain.model.ChatMessage
import domain.model.Group
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import org.jetbrains.skia.Image
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import org.w3c.files.get
import theme.whiteColor
import utils.AppCoroutineDispatchers
import utils.TYPE
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual fun getPlatform(): TYPE = TYPE.WEB


actual class AppCoroutineDispatchersImpl actual constructor() : AppCoroutineDispatchers {
    override val io: CoroutineDispatcher
        get() = Dispatchers.Default
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}

actual suspend fun clipData(clipboardManager: ClipboardManager): String? {
    return window.navigator.clipboard.readText().await().toString().trim()
}
actual suspend fun setClipData(clipboardManager: ClipboardManager,message:String){
    window.navigator.clipboard.writeText(message).await()
}

@Composable
actual fun TextComposable(message:String,isGEMINIMessage:Boolean) {
    Text(
        modifier = Modifier.padding(
            start = 10.dp,
            top = 10.dp,
            end = 10.dp,
            bottom = if (isGEMINIMessage) 3.dp else 10.dp
        ),
        text = message,
        color = whiteColor,
        fontFamily = FontFamily.Cursive,
        style = MaterialTheme.typography.bodyMedium,
    )
}
@Composable
actual fun ImagePicker(showFilePicker: Boolean, onDismissDialog : () -> Unit, onResult: (ByteArray?) -> Unit) {
    val scope = rememberCoroutineScope()
    if (showFilePicker) {
        scope.launch {
            onResult(importImage())
        }
        onDismissDialog()
    }
}

private suspend fun importImage(): ByteArray? = suspendCoroutine { cont ->
    try {
        val input = document.createElement("input").apply {
            setAttribute("type", "file")
            setAttribute("accept",  "image/jpeg, image/png, image/jpg")
        } as HTMLInputElement

        input.onchange = {
            val file = input.files?.get(0)
            if (file != null) {
                val reader = FileReader()
                reader.onload = { event ->
                    val arrayBuffer = (event.target as FileReader).result as ArrayBuffer
                    val array = Uint8Array(arrayBuffer)

                    cont.resume(ByteArray(array.length) { array[it] })
                }
                reader.onerror = {
                    cont.resumeWithException(Exception(reader.error.toString()))
                }
                reader.readAsArrayBuffer(file)
            } else {
                cont.resumeWithException(Exception("No file was selected"))
            }
        }
        input.click()
    } catch (e: Exception) {
        cont.resumeWithException(e)
    }
}

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}

actual fun isNetworkAvailable(): Boolean {
    return window.navigator.onLine
}

actual suspend fun readGroupKStore(readFun : suspend(KStore<List<Group>>) -> Unit){
    val store: KStore<List<Group>> = storeOf(key = "group_db")
    readFun(store)
}
actual suspend fun readChatMessageKStore(readFun : suspend(KStore<List<ChatMessage>>) -> Unit) {
    val store: KStore<List<ChatMessage>> = storeOf(key = "chat_db")
    readFun(store)
}