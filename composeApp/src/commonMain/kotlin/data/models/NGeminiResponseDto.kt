package data.models

import  kotlinx.serialization.*

@Serializable
data class NGeminiResponseDto(
    val candidates: List<CandidateDto>? = null,
    val promptFeedback: PromptFeedbackDto? = null
)

@Serializable
data class CandidateDto(
    val content: ContentDto?,
    val finishReason: String?,
    val index: Int?,
    val safetyRatings: List<SafetyRatingDto>?
)

@Serializable
data class ContentDto(
    val parts: List<PartDto>?,
    val role: String?
)

@Serializable
data class PartDto(
    val text: String?
)

@Serializable
data class PromptFeedbackDto(
    val safetyRatings: List<SafetyRatingDto>?
)

@Serializable
data class SafetyRatingDto(
    val category: String?,
    val probability: String?
)