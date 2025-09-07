package com.coding.meet.gaminiaikmp.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.presentation.components.CommonTextComposable
import com.coding.meet.gaminiaikmp.utils.AppCoroutineDispatchers
import com.coding.meet.gaminiaikmp.utils.TYPE
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import io.github.xxfast.kstore.KStore
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import platform.Foundation.*


actual fun getPlatform(): TYPE = TYPE.MOBILE

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
actual suspend fun readGroupKStore(readFun :suspend (KStore<List<Group>>) -> Unit) {
}
actual suspend fun readChatMessageKStore(readFun :suspend (KStore<List<ChatMessage>>) -> Unit) {}