import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import viewmodels.MainViewModel
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication, state = WindowState(
            placement = WindowPlacement.Maximized,
            position = WindowPosition(Alignment.Center)
        ),title = "Gemini-AI-KMP-App"
    ) {
        window.minimumSize = Dimension(1280, 768)
        val viewModel = remember { MainViewModel() }
        App(viewModel)
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    val viewModel = remember { MainViewModel() }
    App(viewModel)
}