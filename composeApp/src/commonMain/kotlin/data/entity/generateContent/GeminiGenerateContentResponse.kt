package data.entity.generateContent


import data.entity.GeminiCandidate
import data.entity.PromptFeedback
import kotlinx.serialization.Serializable

@Serializable
data class GeminiGenerateContentResponse(
    val candidates: List<GeminiCandidate>,
    val promptFeedback: PromptFeedback
)

