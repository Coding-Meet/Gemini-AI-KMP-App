package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.meet.gaminiaikmp.domain.use_cases.GetAllGroupUserCase
import com.coding.meet.gaminiaikmp.domain.use_cases.GetApiKeyUseCase
import com.coding.meet.gaminiaikmp.domain.use_cases.InsertGroupUseCase
import com.coding.meet.gaminiaikmp.domain.use_cases.SaveApiKeyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAllGroupUserCase: GetAllGroupUserCase,
    private val insertGroupUseCase: InsertGroupUseCase,
    private val getApiKeyUseCase: GetApiKeyUseCase,
    private val saveApiKeyUseCase: SaveApiKeyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _effect = Channel<MainUiEffect>(Channel.BUFFERED)
    val effect: Flow<MainUiEffect> = _effect.receiveAsFlow()

    init {
        handleIntent(MainUiIntent.LoadGroups)
    }

    fun handleIntent(intent: MainUiIntent) {
        when (intent) {
            is MainUiIntent.LoadGroups -> getGroupList()
            is MainUiIntent.SelectGroup -> {
                _uiState.update {
                    it.copy(
                        currentPos = intent.index,
                        selectedGroupId = intent.groupId
                    )
                }
            }
            is MainUiIntent.ClearSelection -> {
                _uiState.update {
                    it.copy(
                        currentPos = -1,
                        selectedGroupId = null
                    )
                }
            }
            is MainUiIntent.OpenNewChatDialog -> {
                _uiState.update { it.copy(isNewChatShowDialog = true) }
            }
            is MainUiIntent.DismissNewChatDialog -> {
                _uiState.update { it.copy(isNewChatShowDialog = false, newGroupText = "") }
            }
            is MainUiIntent.UpdateNewGroupText -> {
                _uiState.update { it.copy(newGroupText = intent.text) }
            }
            is MainUiIntent.AddNewGroup -> {
                addNewGroup(intent.groupId, intent.groupName, intent.date, intent.icon)
            }
            is MainUiIntent.OpenApiKeyDialog -> {
                _uiState.update {
                    it.copy(
                        isApiShowDialog = true,
                        apiKeyText = getApikeyLocalStorage()
                    )
                }
            }
            is MainUiIntent.DismissApiKeyDialog -> {
                _uiState.update { it.copy(isApiShowDialog = false) }
            }
            is MainUiIntent.UpdateApiKeyText -> {
                _uiState.update { it.copy(apiKeyText = intent.text) }
            }
            is MainUiIntent.SaveApiKey -> {
                setApikeyLocalStorage(intent.apiKey)
                _uiState.update { it.copy(isApiShowDialog = false) }
            }
            is MainUiIntent.ShowAlert -> {
                _uiState.update {
                    it.copy(
                        isAlertDialogShow = true,
                        alertTitleText = intent.title,
                        alertDescText = intent.description
                    )
                }
            }
            is MainUiIntent.DismissAlert -> {
                _uiState.update { it.copy(isAlertDialogShow = false) }
            }
            is MainUiIntent.ToggleDesktopDrawer -> {
                _uiState.update { it.copy(isDesktopDrawerOpen = !it.isDesktopDrawerOpen) }
            }
        }
    }

    fun getGroupList() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isLoading = true) }
            val groups = getAllGroupUserCase.getGroupList()
            _uiState.update { it.copy(groupList = groups, isLoading = false) }
        }
    }

    fun addNewGroup(groupId: String, groupName: String, date: String, icon: String) {
        viewModelScope.launch(Dispatchers.Default) {
            insertGroupUseCase.insertGroup(
                groupId, groupName, date, icon
            )
            getGroupList()
        }
    }

    fun setApikeyLocalStorage(apiKeyText: String) {
        saveApiKeyUseCase(apiKeyText)
    }

    fun getApikeyLocalStorage(): String {
        return getApiKeyUseCase()
    }

    fun sendEffect(effect: MainUiEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
