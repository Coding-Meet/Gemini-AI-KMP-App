import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import utils.AppCoroutineDispatchers
import utils.TYPE


actual fun getPlatform(): TYPE = TYPE.MOBILE

//actual suspend fun provideDbDriver(
//    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
//): SqlDriver {
//    val mainSchema = GeminiApiChatDB.Schema
//
//    return AndroidSqliteDriver(mainSchema, KoinPlatform.getKoin().get(), "gemini_api_chat",
//        callback = object : AndroidSqliteDriver.Callback(mainSchema) {
//            override fun onOpen(db: SupportSQLiteDatabase) {
//                db.setForeignKeyConstraintsEnabled(true)
//            }
//        })
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
actual fun ImagePicker(showFilePicker: Boolean, onResult: (ByteArray?) -> Unit) {
   val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        if (imageUri != null) {
            onResult(context.contentResolver.openInputStream(imageUri)?.readBytes())
        }
    }
    if (showFilePicker) {
        pickMedia.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

}

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}