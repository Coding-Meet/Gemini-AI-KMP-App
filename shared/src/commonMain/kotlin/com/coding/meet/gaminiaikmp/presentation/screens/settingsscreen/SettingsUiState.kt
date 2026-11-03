package com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen

import com.coding.meet.gaminiaikmp.domain.model.DEFAULT_GEMINI_MODELS
import com.coding.meet.gaminiaikmp.domain.model.GeminiModelInfo
import com.coding.meet.gaminiaikmp.theme.AppThemeMode
import com.coding.meet.gaminiaikmp.utils.GEMINI_PRO

data class SettingsUiState(
    val apiKey: String = "",
    val isApiKeyMasked: Boolean = true,
    val selectedModel: String = GEMINI_PRO,
    val availableModels: List<GeminiModelInfo> = DEFAULT_GEMINI_MODELS,
    val isLoadingModels: Boolean = false,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val isEditApiKeyDialogOpen: Boolean = false,
    val tempApiKeyInput: String = "",
    val isDeleteAllDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val messageSnackbar: String? = null
)
