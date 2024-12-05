package com.coding.meet.gaminiaikmp.data.network

import com.coding.meet.gaminiaikmp.data.models.GeminiResponseDto


interface GeminiService {
    suspend fun generateContent(content: String, apiKey: String): GeminiResponseDto
    suspend fun generateContentWithImage(content: String,  apiKey: String,images: List<ByteArray> = emptyList()): GeminiResponseDto
}