import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import di.initKoin
import gemini_ai_kmp_app.composeapp.generated.resources.Res
import gemini_ai_kmp_app.composeapp.generated.resources.gemini_logo
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
        icon = org.jetbrains.compose.resources.painterResource(Res.drawable.gemini_logo)
    ) {
        window.minimumSize = Dimension(1280, 768)

        val mainViewModel: MainViewModel = KoinPlatform.getKoin().get()
        App(mainViewModel)
    }
}