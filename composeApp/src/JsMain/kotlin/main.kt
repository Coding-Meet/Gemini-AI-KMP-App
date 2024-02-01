
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.mp.KoinPlatform
import screens.main.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        initKoin()
        CanvasBasedWindow   (
            title = "Gemini AI KMP App",
            canvasElementId = "ComposeTarget") {
            val mainViewModel: MainViewModel = KoinPlatform.getKoin().get()
            App(mainViewModel)
        }
    }
}