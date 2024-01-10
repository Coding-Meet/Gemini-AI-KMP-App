import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady
import viewmodels.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
            val viewModel = remember { MainViewModel() }
            App(viewModel)
        }
    }
}