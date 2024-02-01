import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import org.koin.mp.KoinPlatform
import screens.main.MainViewModel
import java.awt.Dimension

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication, state = WindowState(
            placement = WindowPlacement.Maximized,
            position = WindowPosition(Alignment.Center)
        ), title = "Gemini-AI-KMP-App"
    ) {
        window.minimumSize = Dimension(1280, 768)

        val mainViewModel: MainViewModel = KoinPlatform.getKoin().get()
        App(mainViewModel)
    }
}