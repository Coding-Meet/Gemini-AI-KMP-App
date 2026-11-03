package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen

sealed interface MainUiEffect {
    data class NavigateToDetail(val groupId: String) : MainUiEffect
    data class ShowToast(val message: String) : MainUiEffect
    data class ShowAlert(val title: String, val message: String) : MainUiEffect
}

