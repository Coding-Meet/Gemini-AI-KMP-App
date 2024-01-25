import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import org.koin.compose.koinInject
import org.koin.core.context.startKoin
import presenation.screens.main.MainViewModel
import java.awt.Dimension

fun main() = application {
    var isInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startKoin {
            modules(appModule)
        }
        isInitialized = true
    }
    Window(
        onCloseRequest = ::exitApplication, state = WindowState(
            placement = WindowPlacement.Maximized,
            position = WindowPosition(Alignment.Center)
        ), title = "Gemini-AI-KMP-App"
    ) {
        window.minimumSize = Dimension(1280, 768)
        if (isInitialized) {
            val viewModel = koinInject<MainViewModel>()
            App(viewModel)
        }
    }
}