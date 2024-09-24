package presentation.screens.mainscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
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
    val chatViewModel : ChatViewModel = koinInject()

    ApiKeyAlertDialogBox(mainViewModel)
    NewChatAlertDialogBox(mainViewModel)
    AlertDialogLayout(mainViewModel)
    val sideScreen = if (mainViewModel.platformType == TYPE.MOBILE) {
        SideScreenMobile()
    } else {
        SideScreenDesktop(mainViewModel,chatViewModel)
    }
    when (mainViewModel.screens) {
        Screens.MAIN -> {
            sideScreen.ContentComposable {
                TopBarLayout(mainViewModel) { paddingValues ->
                    LazyColumn(
                        Modifier.fillMaxSize().padding(paddingValues).background(lightBackgroundColor),
                        verticalArrangement =  if (groupUiState.data.isEmpty()) Arrangement.Center else Arrangement.Top,
                    ) {

                        itemsIndexed(groupUiState.data, key = {  index, groupItem ->
                            groupItem.groupId
                        }) { index, groupItem ->
                            GroupLayout(groupItem, mainViewModel.currentPos == index) {
                                val apiKey = mainViewModel.getApikeyLocalStorage().trim()
                                if (apiKey.isNotEmpty()) {
                                    if (apiKey.isValidApiKey()) {
                                        if (mainViewModel.platformType == TYPE.MOBILE) {
                                            mainViewModel.screens = Screens.DETAIL
                                        }
                                        mainViewModel.currentPos = index
                                        chatViewModel.groupId = groupItem.groupId
                                        chatViewModel.getMessageList(true)
                                    } else {
                                        mainViewModel.apiKeyText = apiKey
                                        mainViewModel.isApiShowDialog = true
                                    }
                                } else {
                                    mainViewModel.apiKeyText = apiKey
                                    mainViewModel.isApiShowDialog = true
                                }
                            }
                        }
                        if (mainViewModel.platformType == TYPE.MOBILE) {
                            if (groupUiState.data.isEmpty()) {
                                item {
                                    WelcomeScreen()
                                }
                            }
                        }
                    }
                }

            }
        }

        Screens.DETAIL -> {
            DetailScreen(mainViewModel,chatViewModel)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            if (chatViewModel.failedMessageId.isNotEmpty()) {
                chatViewModel.handleError(
                    chatViewModel.failedMessageId,
                    "Failed to generate content. Please try again."
                )
            }
        }
    }
}