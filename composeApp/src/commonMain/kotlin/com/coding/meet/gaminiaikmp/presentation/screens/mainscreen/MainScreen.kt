package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.coding.meet.gaminiaikmp.presentation.components.*
import com.coding.meet.gaminiaikmp.presentation.desktopweb.SideScreenDesktop
import com.coding.meet.gaminiaikmp.presentation.mobile.SideScreenMobile
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatViewModel
import com.coding.meet.gaminiaikmp.presentation.screens.detailscreen.DetailScreen
import com.coding.meet.gaminiaikmp.theme.lightBackgroundColor
import com.coding.meet.gaminiaikmp.utils.Screens
import com.coding.meet.gaminiaikmp.utils.TYPE
import com.coding.meet.gaminiaikmp.utils.isValidApiKey
import org.koin.compose.koinInject

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
                        Modifier.fillMaxSize().padding(paddingValues).background(
                            lightBackgroundColor
                        ),
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