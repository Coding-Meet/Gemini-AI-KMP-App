package data.respository


import data.mapper.toGemini
import data.network.GeminiService
import domain.model.Gemini
import domain.respository.GeminiRepository


class GeminiRepositoryImp(
    private val geminiService: GeminiService
) : GeminiRepository {

    override suspend fun generateContent(content: String, apiKey: String): Gemini {
        return geminiService.generateContent(content,apiKey).toGemini()
    }


    override suspend fun generateContentWithImage(content: String, apiKey: String, images: List<ByteArray>): Gemini {
        return geminiService.generateContentWithImage(content,apiKey ,images).toGemini()
    }
}