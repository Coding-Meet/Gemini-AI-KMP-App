package com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen

import com.coding.meet.gaminiaikmp.theme.AppThemeMode

sealed interface SettingsUiIntent {
    data object LoadSettings : SettingsUiIntent
    data object RefreshModels : SettingsUiIntent
    data class SelectModel(val modelId: String) : SettingsUiIntent
    data class SelectTheme(val themeMode: AppThemeMode) : SettingsUiIntent
    data object OpenEditApiKeyDialog : SettingsUiIntent
    data object DismissEditApiKeyDialog : SettingsUiIntent
    data class UpdateTempApiKey(val text: String) : SettingsUiIntent
    data class SaveApiKey(val apiKey: String) : SettingsUiIntent
    data object ToggleApiKeyMask : SettingsUiIntent
    data object OpenDeleteAllDialog : SettingsUiIntent
    data object DismissDeleteAllDialog : SettingsUiIntent
    data object ConfirmDeleteAllConversations : SettingsUiIntent
    data object DismissSnackbar : SettingsUiIntent
}
