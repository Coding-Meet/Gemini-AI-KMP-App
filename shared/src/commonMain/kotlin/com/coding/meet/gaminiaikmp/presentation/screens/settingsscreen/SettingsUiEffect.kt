package com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen

sealed interface SettingsUiEffect {
    data object NavigateBack : SettingsUiEffect
    data class ShowSnackbar(val message: String) : SettingsUiEffect
    data object AllConversationsDeleted : SettingsUiEffect
}

