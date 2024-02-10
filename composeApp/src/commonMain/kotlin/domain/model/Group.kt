package domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val groupId: String,
    val groupName: String,
    val date: String,
    val icon: String
)