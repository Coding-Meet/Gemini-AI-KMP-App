import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import di.initKoin
import org.koin.mp.KoinPlatform
import presentation.screens.mainscreen.MainViewModel
import java.awt.Dimension

fun main() = application {
    initKoin {}
    Window(
        onCloseRequest = ::exitApplication, state = WindowState(
            placement = WindowPlacement.Maximized,
            position = WindowPosition(Alignment.Center)
        ), title = "Gemini AI KMP App",
        icon = painterResource("gemini_logo.svg")
    ) {
        window.minimumSize = Dimension(1280, 768)

        val mainViewModel: MainViewModel = KoinPlatform.getKoin().get()
        App(mainViewModel)
    }
}