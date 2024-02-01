import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import utils.AppCoroutineDispatchers
import utils.TYPE


actual fun getPlatform(): TYPE = TYPE.DESKTOP

//actual suspend fun provideDbDriver(
//    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
//): SqlDriver {
//    return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
//        .also { schema.create(it).await() }
//}
actual suspend fun clipData(clipboardManager: ClipboardManager): String? {
    return clipboardManager.getText()?.text.toString().trim()
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
actual fun ImagePicker(showFilePicker:Boolean,onResult: (ByteArray?) -> Unit) {
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