package screens.chatscreen

import domain.model.ChatMessage

data class ChatUiState(
    val message: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
)