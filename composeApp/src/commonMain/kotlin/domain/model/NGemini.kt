package domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NGemini(
    @SerialName("candidates")
    val candidates: List<Candidate>?,
    @SerialName("promptFeedback")
    val promptFeedback: PromptFeedback?
) {
    @Serializable
    data class Candidate(
        @SerialName("content")
        val content: Content?,
        @SerialName("finishReason")
        val finishReason: String?,
        @SerialName("index")
        val index: Int?,
        @SerialName("safetyRatings")
        val safetyRatings: List<SafetyRating>?
    )

    @Serializable
    data class Content(
        @SerialName("parts")
        val parts: List<Part>?,
        @SerialName("role")
        val role: String?
    )

    @Serializable
    data class Part(
        @SerialName("text")
        val text: String?
    )

    @Serializable
    data class PromptFeedback(
        @SerialName("safetyRatings")
        val safetyRatings: List<SafetyRating>?
    )

    @Serializable
    data class SafetyRating(
        @SerialName("category")
        val category: String?,
        @SerialName("probability")
        val probability: String?
    )
}