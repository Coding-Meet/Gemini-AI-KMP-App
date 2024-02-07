package domain.use_cases

import domain.model.Gemini
import domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent


class GetContentUseCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun getContentWithImage(content: String, apiKey: String, images: List<ByteArray>): Gemini {
        return if (images.isNotEmpty()) {
            geminiRepository.generateContentWithImage(content, apiKey, images)
        } else {
            geminiRepository.generateContent(content, apiKey)
        }
    }

}