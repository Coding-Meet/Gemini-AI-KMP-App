package presentation.screens.mainscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.mp.KoinPlatform
import presentation.components.*
import presentation.desktopweb.SideScreenDesktop
import presentation.mobile.SideScreenMobile
import presentation.screens.chatscreen.*
import presentation.screens.detailscreen.*
import theme.*
import utils.*

@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val groupUiState by mainViewModel.uiState.collectAsState()
    val chatViewModel: ChatViewModel = KoinPlatform.getKoin().get()
    ApiKeyAlertDialogBox(mainViewModel)
    NewChatAlertDialogBox(mainViewModel)
    val sideScreen = if (mainViewModel.platformType == TYPE.MOBILE) {
        SideScreenMobile()
    } else {
        SideScreenDesktop(mainViewModel)
    }
    when (mainViewModel.screens) {
        Screens.MAIN -> {
            sideScreen.SideRow {
                TopBarLayout(mainViewModel) { paddingValues ->
                    LazyColumn(
                        Modifier.fillMaxSize().padding(paddingValues).background(lightBackgroundColor)
                    ) {
                        itemsIndexed(groupUiState.data) { index, groupItem ->
                            GroupLayout(groupItem, mainViewModel.currentPos == index) {
                                if (mainViewModel.platformType == TYPE.MOBILE) {
                                    mainViewModel.screens = Screens.DETAIL
                                }
                                mainViewModel.currentPos = index
                                chatViewModel.groupId = groupItem.groupId
                                chatViewModel.getMessageList(true)
                            }
                        }
                    }
                }

            }
        }

        Screens.DETAIL -> {
            DetailScreen(mainViewModel)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            if (chatViewModel.failedMessageId.isNotEmpty()){
                chatViewModel.handleError(chatViewModel.failedMessageId, null)
            }
        }
    }
}