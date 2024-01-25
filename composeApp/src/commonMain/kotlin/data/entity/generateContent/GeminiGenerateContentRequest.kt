package data.entity.generateContent

import data.entity.GeminiContent
import kotlinx.serialization.Serializable

@Serializable
data class GeminiGenerateContentRequest(
    val contents: List<GeminiContent>
)
