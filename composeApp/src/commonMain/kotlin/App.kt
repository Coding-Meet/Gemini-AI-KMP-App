import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import screens.MainScreen
import viewmodels.MainViewModel

@Composable
fun App(viewModel: MainViewModel) {
    MaterialTheme {
        MainScreen(viewModel)
    }
}