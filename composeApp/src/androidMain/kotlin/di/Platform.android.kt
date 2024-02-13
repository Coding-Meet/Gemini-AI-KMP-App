package di

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import domain.model.ChatMessage
import domain.model.Group
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.mp.KoinPlatform
import presentation.components.CommonTextComposable
import utils.AppCoroutineDispatchers
import utils.TYPE

actual fun getPlatform(): TYPE = TYPE.MOBILE

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
actual fun ImagePicker(showFilePicker: Boolean,onDismissDialog : () -> Unit, onResult: (ByteArray?) -> Unit) {
    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        if (imageUri != null) {
            onResult(context.contentResolver.openInputStream(imageUri)?.readBytes())
        }
        onDismissDialog()
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

@Composable
actual fun TextComposable(message: String, isGEMINIMessage: Boolean) {
    CommonTextComposable(message, isGEMINIMessage)
}

actual fun isNetworkAvailable(): Boolean {
    val context: Context = KoinPlatform.getKoin().get()
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val cap = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}
actual suspend fun readGroupKStore(readFun: suspend (KStore<List<Group>>) -> Unit) {}
actual suspend fun readChatMessageKStore(readFun: suspend (KStore<List<ChatMessage>>) -> Unit) {}