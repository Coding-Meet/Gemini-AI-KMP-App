import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import presenation.screens.main.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(appModule)
    }
    onWasmReady {
        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
            val viewModel = koinInject<MainViewModel>()
            App(viewModel)
        }
    }
}