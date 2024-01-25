import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import presenation.screens.main.MainScreen
import presenation.screens.main.MainViewModel

@Composable
fun App(viewModel: MainViewModel) {
    MaterialTheme {
        MainScreen(viewModel)
    }
}