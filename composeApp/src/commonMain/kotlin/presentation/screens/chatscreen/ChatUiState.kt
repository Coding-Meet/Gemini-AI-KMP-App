package presentation.screens.chatscreen

import domain.model.ChatMessage

data class ChatUiState(
    val message: List<ChatMessage> = emptyList(),
    val isApiLoading: Boolean = false,
    val isLoading: Boolean = false,
)