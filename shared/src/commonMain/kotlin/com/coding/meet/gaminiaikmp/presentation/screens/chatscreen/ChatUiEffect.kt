package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen

sealed interface ChatUiEffect {
    data object NavigateBack : ChatUiEffect
    data class ShowAlert(val title: String, val message: String) : ChatUiEffect
    data class ShowToast(val message: String) : ChatUiEffect
    data object ScrollToBottom : ChatUiEffect
}

