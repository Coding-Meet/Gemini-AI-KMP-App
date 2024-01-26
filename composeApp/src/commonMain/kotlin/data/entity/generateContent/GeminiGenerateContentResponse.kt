package data.entity.generateContent


import data.entity.GeminiCandidate
import kotlinx.serialization.Serializable

@Serializable
data class GeminiGenerateContentResponse(
    val candidates: List<GeminiCandidate>
)

