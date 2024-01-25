package data.entity

import kotlinx.serialization.Serializable

@Serializable
data class GeminiCandidate(
    val content: GeminiContent,
    val finishReason: String,
    val index: Int,
    val safetyRatings: List<SafetyRating>
)