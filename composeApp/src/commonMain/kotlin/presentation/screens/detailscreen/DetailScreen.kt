package presentation.screens.detailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.mp.KoinPlatform
import presentation.components.DeleteChatAlertDialogBox
import presentation.components.WelcomeScreen
import presentation.screens.chatscreen.ChatScreen
import presentation.screens.chatscreen.ChatViewModel
import presentation.screens.mainscreen.MainViewModel
import theme.*

@Composable
fun DetailScreen(mainViewModel: MainViewModel) {
    val chatViewModel: ChatViewModel = KoinPlatform.getKoin().get()

    val chatUiState by chatViewModel.chatUiState.collectAsState()
    val groupUiState by mainViewModel.uiState.collectAsState()
    DeleteChatAlertDialogBox(chatViewModel, mainViewModel)
    Column(
        Modifier.fillMaxHeight().background(lightBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (groupUiState.data.isNotEmpty() && mainViewModel.currentPos != -1) {
            ChatScreen(
                mainViewModel,
                chatViewModel,
                chatUiState,
                groupUiState,
                groupUiState.data[mainViewModel.currentPos]
            )
        } else {
            WelcomeScreen()
        }
    }
}