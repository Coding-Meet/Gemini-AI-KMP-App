package data.entity

import kotlinx.serialization.Serializable

@Serializable
data class ChatModel(
    val chatId: String,
    val createdAt: String,
    var title: String
)