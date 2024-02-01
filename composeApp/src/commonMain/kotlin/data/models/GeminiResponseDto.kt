package data.models

import  kotlinx.serialization.*

@Serializable
data class GeminiResponseDto(
    val candidates: List<CandidateDto>,
)

@Serializable
data class CandidateDto(
    val content: ContentDto,
)

@Serializable
data class ContentDto(
    val parts: List<PartDto>,
    val role: String
)

@Serializable
data class PartDto(
    val text: String
)