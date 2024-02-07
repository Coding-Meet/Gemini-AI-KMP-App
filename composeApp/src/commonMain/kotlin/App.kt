import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import presentation.screens.mainscreen.*

@Composable
fun App(mainViewModel: MainViewModel) {
    MaterialTheme {
        MainScreen(mainViewModel)
    }
}