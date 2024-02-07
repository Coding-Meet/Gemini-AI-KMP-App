package domain.use_cases

import domain.model.Gemini
import domain.respository.GeminiRepository


interface IGetContentUseCase {
    suspend fun getContentWithImage(content: String, apiKey: String, images: List<ByteArray>): Gemini
}

class GetContentUseCase(private val geminiRepository: GeminiRepository) : IGetContentUseCase {
    override suspend fun getContentWithImage(content: String, apiKey: String, images: List<ByteArray>): Gemini {
        return if (images.isNotEmpty()) {
            geminiRepository.generateContentWithImage(content, apiKey, images)
        } else {
            geminiRepository.generateContent(content, apiKey)
        }
    }

}