import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.ClipboardManager
import utils.*

expect fun getPlatform(): TYPE
expect suspend fun clipData(clipboardManager: ClipboardManager): String?

@Composable
expect fun ImagePicker(showFilePicker: Boolean, onResult: (ByteArray?) -> Unit)

expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect class AppCoroutineDispatchersImpl() : AppCoroutineDispatchers