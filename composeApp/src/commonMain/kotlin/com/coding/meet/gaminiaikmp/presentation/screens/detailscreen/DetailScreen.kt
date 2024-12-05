package com.coding.meet.gaminiaikmp.presentation.screens.detailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.coding.meet.gaminiaikmp.presentation.components.DeleteChatAlertDialogBox
import com.coding.meet.gaminiaikmp.presentation.components.WelcomeScreen
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatScreen
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatViewModel
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel
import com.coding.meet.gaminiaikmp.theme.lightBackgroundColor


@Composable
fun DetailScreen(mainViewModel: MainViewModel, chatViewModel: ChatViewModel) {

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