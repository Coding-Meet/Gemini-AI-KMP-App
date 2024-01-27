import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import screens.main.MainScreen
import screens.main.MainViewModel

@Composable
fun App(viewModel: MainViewModel) {
    MaterialTheme {
        MainScreen(viewModel)
    }
}