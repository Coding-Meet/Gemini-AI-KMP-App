import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import org.jetbrains.skia.Image
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.Worker
import org.w3c.files.FileReader
import org.w3c.files.get
import utils.AppCoroutineDispatchers
import utils.TYPE
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Promise

actual fun getPlatform(): TYPE = TYPE.WEB


//actual fun sqlDriverFactory(driver: (SqlDriver) -> Unit) {
//    val mainDriver = WebWorkerDriver(
//        Worker(
//            js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
//        )
//    )
//    GlobalScope.promise {
//        println("start")
//            GeminiApiChatDB.Schema.awaitCreate(mainDriver)
//        println("completed"+mainDriver.toString())
//        GeminiApiChatDB(mainDriver)
//        driver(mainDriver)
//    }
//}
// Extension function to convert a Kotlin coroutine to a JavaScript Promise
fun CoroutineScope.promise(block: suspend () -> Unit): Promise<Unit> {
    return Promise { resolve, reject ->
        launch {
            async {
            block()

            }.invokeOnCompletion {
            resolve(Unit)
            }
        }
    }
}
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


@Composable
actual fun ImagePicker(showFilePicker: Boolean, onResult: (ByteArray?) -> Unit) {
    val scope = rememberCoroutineScope()
    if (showFilePicker) {
        scope.launch {
            onResult(importImage())
        }
    }
}

private suspend fun importImage(): ByteArray? = suspendCoroutine { cont ->
    try {
        val input = document.createElement("input").apply {
            setAttribute("type", "file")
            setAttribute("accept", "image/*")
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