package domain.respository

import domain.models.NGemini


interface NGeminiRepository {
    suspend fun generateContent(content: String): NGemini


    suspend fun generateContentWithImage(content: String, images: List<ByteArray> = emptyList()): NGemini
}