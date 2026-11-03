package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen

sealed interface ChatUiIntent {
    data class LoadMessages(val groupId: String, val isClicked: Boolean = false) : ChatUiIntent
    data class UpdateInputMessage(val message: String) : ChatUiIntent
    data class AddImage(val imageBytes: ByteArray) : ChatUiIntent
    data class RemoveImage(val imageBytes: ByteArray) : ChatUiIntent
    data object ClearImages : ChatUiIntent
    data class SendMessage(
        val groupId: String,
        val content: String = "",
        val apiKey: String = ""
    ) : ChatUiIntent
    data object OpenDeleteDialog : ChatUiIntent
    data object DismissDeleteDialog : ChatUiIntent
    data class DeleteAllMessages(val groupId: String) : ChatUiIntent
    data class DeleteGroupWithMessages(val groupId: String) : ChatUiIntent
    data class ShowAlert(val title: String, val message: String) : ChatUiIntent
    data object DismissAlert : ChatUiIntent
    data class HandleError(val messageId: String, val errorMessage: String) : ChatUiIntent
    data object RetryFailedMessage : ChatUiIntent
}

