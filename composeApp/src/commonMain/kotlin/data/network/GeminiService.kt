package data.network

import data.models.GeminiResponseDto


interface GeminiService {
    suspend fun generateContent(content: String, apiKey: String): GeminiResponseDto
    suspend fun generateContentWithImage(content: String,  apiKey: String,images: List<ByteArray> = emptyList()): GeminiResponseDto
}