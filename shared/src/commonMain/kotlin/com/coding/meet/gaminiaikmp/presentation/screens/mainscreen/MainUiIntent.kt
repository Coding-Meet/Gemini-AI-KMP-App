package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen

sealed interface MainUiIntent {
    data object ToggleDesktopDrawer : MainUiIntent
    data class SetDesktopDrawer(val isOpen: Boolean) : MainUiIntent
}
