import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import presenation.screens.main.MainViewModel
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication, state = WindowState(
            placement = WindowPlacement.Maximized,
            position = WindowPosition(Alignment.Center)
        ), title = "Gemini-AI-KMP-App"
    ) {
        window.minimumSize = Dimension(1280, 768)
        val viewModel = remember { MainViewModel() }
        App(viewModel)
    }
}