package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: MainUiIntent) {
        when (intent) {
            is MainUiIntent.ToggleDesktopDrawer -> {
                _uiState.update { it.copy(isDesktopDrawerOpen = !it.isDesktopDrawerOpen) }
            }
            is MainUiIntent.SetDesktopDrawer -> {
                _uiState.update { it.copy(isDesktopDrawerOpen = intent.isOpen) }
            }
        }
    }
}
