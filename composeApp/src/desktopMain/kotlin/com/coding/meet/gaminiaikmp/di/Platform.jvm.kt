package com.coding.meet.gaminiaikmp.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.window.AwtWindow
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.presentation.components.CommonTextComposable
import com.coding.meet.gaminiaikmp.utils.AppCoroutineDispatchers
import com.coding.meet.gaminiaikmp.utils.TYPE
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import java.awt.Dialog
import java.awt.FileDialog
import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


actual fun getPlatform(): TYPE = TYPE.DESKTOP

actual suspend fun clipData(clipboardManager: ClipboardManager): String? {
    return clipboardManager.getText()?.text.toString().trim()
}

actual suspend fun setClipData(clipboardManager: ClipboardManager, message: String) {
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
actual fun ImagePicker(showFilePicker: Boolean, onDismissDialog: () -> Unit, onResult: (ByteArray?) -> Unit) {
    val scope = rememberCoroutineScope()
    val parent: Dialog? = null
    AwtWindow(showFilePicker, create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    file?.let { fileName ->
                        scope.launch {
                            val imageFile = File(directory, fileName)
                            if (imageFile.extension in listOf("jpg", "jpeg", "png")) {
                                try {
                                    onResult(imageFile.readBytes())
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        onDismissDialog()
                    }
                } else {
                    onDismissDialog()
                }
            }
        }
    }, dispose = {
        onDismissDialog()
    })
}

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}
@Composable
actual fun TextComposable(message: String, isGEMINIMessage: Boolean) {
    CommonTextComposable(message, isGEMINIMessage)
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