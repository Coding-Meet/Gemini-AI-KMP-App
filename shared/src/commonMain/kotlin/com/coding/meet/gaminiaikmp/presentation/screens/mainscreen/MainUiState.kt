package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen

import com.coding.meet.gaminiaikmp.domain.model.Group

data class MainUiState(
    val groupList: List<Group> = emptyList(),
    val isLoading: Boolean = false,
    val currentPos: Int = -1,
    val selectedGroupId: String? = null,
    val isNewChatShowDialog: Boolean = false,
    val newGroupText: String = "",
    val isApiShowDialog: Boolean = false,
    val apiKeyText: String = "",
    val isAlertDialogShow: Boolean = false,
    val alertTitleText: String = "",
    val alertDescText: String = "",
    val isDesktopDrawerOpen: Boolean = true,
)

