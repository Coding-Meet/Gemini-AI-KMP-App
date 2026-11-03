package com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen

sealed interface GroupUiIntent {
    data object LoadGroups : GroupUiIntent
    data class SelectGroup(val index: Int, val groupId: String) : GroupUiIntent
    data object ClearSelection : GroupUiIntent
    data object OpenNewChatDialog : GroupUiIntent
    data object DismissNewChatDialog : GroupUiIntent
    data class UpdateNewGroupText(val text: String) : GroupUiIntent
    data class AddNewGroup(
        val groupId: String,
        val groupName: String,
        val date: String,
        val icon: String
    ) : GroupUiIntent
    data object OpenApiKeyDialog : GroupUiIntent
    data object DismissApiKeyDialog : GroupUiIntent
    data class UpdateApiKeyText(val text: String) : GroupUiIntent
    data class SaveApiKey(val apiKey: String) : GroupUiIntent
    data class ShowAlert(val title: String, val description: String) : GroupUiIntent
    data object DismissAlert : GroupUiIntent
}

