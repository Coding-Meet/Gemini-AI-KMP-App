package data.entity

import kotlinx.serialization.Serializable

@Serializable
data class GeminiMessage(
    val text: String
)