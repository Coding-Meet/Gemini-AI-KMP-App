
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady
import screens.main.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        initKoin()
        CanvasBasedWindow   (
            title = "Gemini AI KMP App",
            canvasElementId = "ComposeTarget") {
            val viewModel = remember { MainViewModel() }
                App(viewModel)
        }
    }
}