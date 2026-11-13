package com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen

sealed interface GroupUiEffect {
    data class NavigateToDetail(val groupId: String) : GroupUiEffect
    data class ShowAlert(val title: String, val message: String) : GroupUiEffect
    data class ShowSnackbar(val message: String) : GroupUiEffect
}

