package com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen

import com.coding.meet.gaminiaikmp.domain.model.Group

data class GroupUiState(
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
    val alertDescText: String = ""
)

