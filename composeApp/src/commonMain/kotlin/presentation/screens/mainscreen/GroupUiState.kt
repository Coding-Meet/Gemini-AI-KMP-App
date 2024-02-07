package presentation.screens.mainscreen

import domain.model.Group

data class GroupUiState(
    val data: List<Group> = emptyList()
)