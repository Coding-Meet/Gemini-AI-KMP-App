import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin
import org.koin.compose.koinInject
import presentation.screens.mainscreen.MainViewModel

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    val mainViewModel =  koinInject<MainViewModel>()
    App(mainViewModel)
}
