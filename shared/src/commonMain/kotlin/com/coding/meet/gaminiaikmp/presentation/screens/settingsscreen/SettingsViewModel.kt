package com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.meet.gaminiaikmp.domain.use_cases.ClearAllConversationsUseCase
import com.coding.meet.gaminiaikmp.domain.use_cases.GetApiKeyUseCase
import com.coding.meet.gaminiaikmp.domain.use_cases.GetAvailableModelsUseCase
import com.coding.meet.gaminiaikmp.domain.use_cases.GetSelectedModelUseCase
import com.coding.meet.gaminiaikmp.domain.use_cases.GetThemeModeUseCase
import com.coding.meet.gaminiaikmp.domain.use_cases.SaveApiKeyUseCase
import com.coding.meet.gaminiaikmp.domain.use_cases.SaveSelectedModelUseCase
import com.coding.meet.gaminiaikmp.domain.use_cases.SaveThemeModeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val clearAllConversationsUseCase: ClearAllConversationsUseCase,
    private val getAvailableModelsUseCase: GetAvailableModelsUseCase,
    private val getApiKeyUseCase: GetApiKeyUseCase,
    private val saveApiKeyUseCase: SaveApiKeyUseCase,
    private val getSelectedModelUseCase: GetSelectedModelUseCase,
    private val saveSelectedModelUseCase: SaveSelectedModelUseCase,
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val saveThemeModeUseCase: SaveThemeModeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _effect = Channel<SettingsUiEffect>(Channel.BUFFERED)
    val effect: Flow<SettingsUiEffect> = _effect.receiveAsFlow()

    init {
        handleIntent(SettingsUiIntent.LoadSettings)
    }

    fun handleIntent(intent: SettingsUiIntent) {
        when (intent) {
            is SettingsUiIntent.LoadSettings -> {
                val currentApiKey = getApiKeyUseCase()
                val currentModel = getSelectedModelUseCase()
                val currentTheme = getThemeModeUseCase()
                _uiState.update {
                    it.copy(
                        apiKey = currentApiKey,
                        selectedModel = currentModel,
                        themeMode = currentTheme
                    )
                }
                loadModels(currentApiKey)
            }

            is SettingsUiIntent.RefreshModels -> {
                loadModels(_uiState.value.apiKey, userTriggered = true)
            }

            is SettingsUiIntent.SelectModel -> {
                saveSelectedModelUseCase(intent.modelId)
                _uiState.update { it.copy(selectedModel = intent.modelId) }
                sendEffect(SettingsUiEffect.ShowSnackbar("Gemini model set to ${intent.modelId}"))
            }

            is SettingsUiIntent.SelectTheme -> {
                saveThemeModeUseCase(intent.themeMode)
                _uiState.update { it.copy(themeMode = intent.themeMode) }
                sendEffect(SettingsUiEffect.ShowSnackbar("Theme set to ${intent.themeMode.title}"))
            }

            is SettingsUiIntent.OpenEditApiKeyDialog -> {
                _uiState.update {
                    it.copy(
                        isEditApiKeyDialogOpen = true,
                        tempApiKeyInput = it.apiKey
                    )
                }
            }

            is SettingsUiIntent.DismissEditApiKeyDialog -> {
                _uiState.update { it.copy(isEditApiKeyDialogOpen = false) }
            }

            is SettingsUiIntent.UpdateTempApiKey -> {
                _uiState.update { it.copy(tempApiKeyInput = intent.text) }
            }

            is SettingsUiIntent.SaveApiKey -> {
                val cleanedKey = intent.apiKey.trim()
                saveApiKeyUseCase(cleanedKey)
                _uiState.update {
                    it.copy(
                        apiKey = cleanedKey,
                        isEditApiKeyDialogOpen = false
                    )
                }
                sendEffect(SettingsUiEffect.ShowSnackbar("API Key updated successfully"))
                loadModels(cleanedKey, userTriggered = true)
            }

            is SettingsUiIntent.ToggleApiKeyMask -> {
                _uiState.update { it.copy(isApiKeyMasked = !it.isApiKeyMasked) }
            }

            is SettingsUiIntent.OpenDeleteAllDialog -> {
                _uiState.update { it.copy(isDeleteAllDialogOpen = true) }
            }

            is SettingsUiIntent.DismissDeleteAllDialog -> {
                _uiState.update { it.copy(isDeleteAllDialogOpen = false) }
            }

            is SettingsUiIntent.ConfirmDeleteAllConversations -> {
                deleteAllConversations()
            }

            is SettingsUiIntent.DismissSnackbar -> {
                _uiState.update { it.copy(messageSnackbar = null) }
            }
        }
    }

    private fun loadModels(apiKey: String, userTriggered: Boolean = false) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isLoadingModels = true) }
            try {
                val models = getAvailableModelsUseCase(apiKey)
                _uiState.update {
                    it.copy(
                        availableModels = models,
                        isLoadingModels = false
                    )
                }
                if (userTriggered) {
                    sendEffect(SettingsUiEffect.ShowSnackbar("Models refreshed successfully (${models.size} available)"))
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingModels = false) }
                if (userTriggered) {
                    sendEffect(SettingsUiEffect.ShowSnackbar("Failed to refresh models: ${e.message}"))
                }
            }
        }
    }

    private fun deleteAllConversations() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isLoading = true) }
            try {
                clearAllConversationsUseCase()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isDeleteAllDialogOpen = false
                    )
                }
                sendEffect(SettingsUiEffect.ShowSnackbar("All conversations permanently deleted"))
                sendEffect(SettingsUiEffect.AllConversationsDeleted)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                sendEffect(SettingsUiEffect.ShowSnackbar("Failed to delete conversations: ${e.message}"))
            }
        }
    }

    private fun sendEffect(effect: SettingsUiEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
