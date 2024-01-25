package data.entity

import kotlinx.serialization.Serializable

@Serializable
data class GeminiContent(
    val role: String,
    val parts: List<GeminiMessage>
)