package com.coding.meet.gaminiaikmp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponseDto(
    val candidates: List<CandidateDto> = emptyList(),
    val error: GeminiErrorDto? = null
)

@Serializable
data class GeminiErrorDto(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null
)

@Serializable
data class CandidateDto(
    val content: ContentDto? = null,
    val finishReason: String? = null
)

@Serializable
data class ContentDto(
    val parts: List<PartDto> = emptyList(),
    val role: String? = null
)

@Serializable
data class PartDto(
    val text: String? = null,
    val thought: Boolean? = null
)
