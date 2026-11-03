package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen

import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Group

data class ChatUiState(
    val groupId: String = "",
    val group: Group? = null,
    val message: List<ChatMessage> = emptyList(),
    val inputMessage: String = "",
    val selectedImages: List<ByteArray> = emptyList(),
    val isApiLoading: Boolean = false,
    val isLoading: Boolean = false,
    val isDeleteShowDialog: Boolean = false,
    val isAlertDialogShow: Boolean = false,
    val alertTitleText: String = "",
    val alertDescText: String = "",
    val failedMessageId: String = ""
)
