package screens.chatscreen

import domain.models.ChatMessage

data class ChatUiState(
    val message: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val images: List<ByteArray> = emptyList(),
    val isConnectionError: Boolean = false,
    val errorMessage: String? = null
)