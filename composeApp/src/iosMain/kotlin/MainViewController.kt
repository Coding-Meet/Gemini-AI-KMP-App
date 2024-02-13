import androidx.compose.ui.window.ComposeUIViewController
import org.koin.mp.KoinPlatform
import presentation.screens.mainscreen.MainViewModel

fun MainViewController() = ComposeUIViewController {
    val mainViewModel: MainViewModel = KoinPlatform.getKoin().get()
    App(mainViewModel)
}
