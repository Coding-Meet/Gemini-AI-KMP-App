package com.coding.meet.gaminiaikmp.data.network

import com.coding.meet.gaminiaikmp.data.models.GeminiResponseDto
import com.coding.meet.gaminiaikmp.data.models.ListModelsResponseDto

interface GeminiService {
    suspend fun generateContent(content: String, apiKey: String): GeminiResponseDto
    suspend fun generateContentWithImage(content: String, apiKey: String, images: List<ByteArray> = emptyList()): GeminiResponseDto
    suspend fun listModels(apiKey: String): ListModelsResponseDto
}