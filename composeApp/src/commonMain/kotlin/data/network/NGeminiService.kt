package data.network

import data.models.NGeminiResponseDto


interface NGeminiService {
    suspend fun generateContent(content: String): NGeminiResponseDto
    suspend fun generateContentWithImage(content: String, images: List<ByteArray> = emptyList()): NGeminiResponseDto
}