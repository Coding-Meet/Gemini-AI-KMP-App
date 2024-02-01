package domain.respository

import domain.model.Gemini


interface GeminiRepository {
    suspend fun generateContent(content: String, apiKey: String): Gemini

    suspend fun generateContentWithImage(content: String, apiKey: String, images: List<ByteArray> = emptyList()): Gemini
}