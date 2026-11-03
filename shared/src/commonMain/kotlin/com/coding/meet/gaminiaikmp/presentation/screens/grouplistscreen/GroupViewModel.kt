package com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen

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

class GroupViewModel(
    private val getAllGroupUserCase: GetAllGroupUserCase,
    private val insertGroupUseCase: InsertGroupUseCase,
    private val getApiKeyUseCase: GetApiKeyUseCase,
    private val saveApiKeyUseCase: SaveApiKeyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()

    private val _effect = Channel<GroupUiEffect>(Channel.BUFFERED)
    val effect: Flow<GroupUiEffect> = _effect.receiveAsFlow()

    init {
        handleIntent(GroupUiIntent.LoadGroups)
    }

    fun handleIntent(intent: GroupUiIntent) {
        when (intent) {
            is GroupUiIntent.LoadGroups -> getGroupList()
            is GroupUiIntent.SelectGroup -> {
                _uiState.update {
                    it.copy(
                        currentPos = intent.index,
                        selectedGroupId = intent.groupId
                    )
                }
            }
            is GroupUiIntent.ClearSelection -> {
                _uiState.update {
                    it.copy(
                        currentPos = -1,
                        selectedGroupId = null
                    )
                }
            }
            is GroupUiIntent.OpenNewChatDialog -> {
                _uiState.update { it.copy(isNewChatShowDialog = true) }
            }
            is GroupUiIntent.DismissNewChatDialog -> {
                _uiState.update { it.copy(isNewChatShowDialog = false, newGroupText = "") }
            }
            is GroupUiIntent.UpdateNewGroupText -> {
                _uiState.update { it.copy(newGroupText = intent.text) }
            }
            is GroupUiIntent.AddNewGroup -> {
                _uiState.update { it.copy(isNewChatShowDialog = false, newGroupText = "") }
                addNewGroup(intent.groupId, intent.groupName, intent.date, intent.icon)
            }
            is GroupUiIntent.OpenApiKeyDialog -> {
                _uiState.update {
                    it.copy(
                        isApiShowDialog = true,
                        apiKeyText = getApikeyLocalStorage()
                    )
                }
            }
            is GroupUiIntent.DismissApiKeyDialog -> {
                _uiState.update { it.copy(isApiShowDialog = false) }
            }
            is GroupUiIntent.UpdateApiKeyText -> {
                _uiState.update { it.copy(apiKeyText = intent.text) }
            }
            is GroupUiIntent.SaveApiKey -> {
                setApikeyLocalStorage(intent.apiKey)
                _uiState.update { it.copy(isApiShowDialog = false) }
            }
            is GroupUiIntent.ShowAlert -> {
                _uiState.update {
                    it.copy(
                        isAlertDialogShow = true,
                        alertTitleText = intent.title,
                        alertDescText = intent.description
                    )
                }
            }
            is GroupUiIntent.DismissAlert -> {
                _uiState.update { it.copy(isAlertDialogShow = false) }
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
            val groups = getAllGroupUserCase.getGroupList()
            val newIndex = groups.indexOfFirst { it.groupId == groupId }
            _uiState.update {
                it.copy(
                    groupList = groups,
                    isLoading = false,
                    isNewChatShowDialog = false,
                    newGroupText = "",
                    currentPos = if (newIndex != -1) newIndex else 0,
                    selectedGroupId = groupId
                )
            }
            sendEffect(GroupUiEffect.NavigateToDetail(groupId))
        }
    }

    fun setApikeyLocalStorage(apiKeyText: String) {
        saveApiKeyUseCase(apiKeyText)
    }

    fun getApikeyLocalStorage(): String {
        return getApiKeyUseCase()
    }

    fun sendEffect(effect: GroupUiEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}

