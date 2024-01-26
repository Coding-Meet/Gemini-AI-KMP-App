import androidx.compose.ui.platform.ClipboardManager
import utils.*

interface Platform {
    val type: TYPE
}

expect fun getPlatform(): Platform
expect suspend fun clipData(clipboardManager:ClipboardManager): String?


expect class AppCoroutineDispatchersImpl(): AppCoroutineDispatchers