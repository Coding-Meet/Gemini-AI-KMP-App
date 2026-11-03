package com.coding.meet.gaminiaikmp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ListModelsResponseDto(
    val models: List<GeminiModelDto> = emptyList(),
    val nextPageToken: String? = null
)

@Serializable
data class GeminiModelDto(
    val name: String,
    val version: String? = null,
    val displayName: String? = null,
    val description: String? = null,
    val inputTokenLimit: Int? = null,
    val outputTokenLimit: Int? = null,
    val supportedGenerationMethods: List<String> = emptyList(),
    val thinking: Boolean? = null
)
