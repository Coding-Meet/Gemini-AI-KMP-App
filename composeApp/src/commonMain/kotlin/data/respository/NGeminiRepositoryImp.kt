package data.respository


import data.mapper.toNGemini
import data.network.NGeminiService
import domain.models.NGemini
import domain.respository.NGeminiRepository


class NGeminiRepositoryImp(
    private val nGeminiService: NGeminiService
) : NGeminiRepository {

    override suspend fun generateContent(content: String): NGemini {
        return nGeminiService.generateContent(content).toNGemini()
    }


    override suspend fun generateContentWithImage(content: String, images: List<ByteArray>): NGemini {
        return nGeminiService.generateContentWithImage(content, images).toNGemini()
    }
}