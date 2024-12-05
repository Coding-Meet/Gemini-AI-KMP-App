package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen

import com.coding.meet.gaminiaikmp.domain.model.Group

data class GroupUiState(
    val data: List<Group> = emptyList()
)