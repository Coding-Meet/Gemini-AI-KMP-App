package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen

sealed interface MainUiIntent {
    data object LoadGroups : MainUiIntent
    data class SelectGroup(val index: Int, val groupId: String) : MainUiIntent
    data object ClearSelection : MainUiIntent
    data object OpenNewChatDialog : MainUiIntent
    data object DismissNewChatDialog : MainUiIntent
    data class UpdateNewGroupText(val text: String) : MainUiIntent
    data class AddNewGroup(
        val groupId: String,
        val groupName: String,
        val date: String,
        val icon: String
    ) : MainUiIntent
    data object OpenApiKeyDialog : MainUiIntent
    data object DismissApiKeyDialog : MainUiIntent
    data class UpdateApiKeyText(val text: String) : MainUiIntent
    data class SaveApiKey(val apiKey: String) : MainUiIntent
    data class ShowAlert(val title: String, val description: String) : MainUiIntent
    data object DismissAlert : MainUiIntent
    data object ToggleDesktopDrawer : MainUiIntent
}

