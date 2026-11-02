package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen

import com.coding.meet.gaminiaikmp.domain.model.ChatMessage

data class ChatUiState(
    val message: List<ChatMessage> = emptyList(),
    val isApiLoading: Boolean = false,
    val isLoading: Boolean = false,
)