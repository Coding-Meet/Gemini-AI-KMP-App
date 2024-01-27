package domain.use_cases

import domain.models.NGemini
import domain.respository.NGeminiRepository


interface IGetContentUseCase {
    suspend fun getContentWithImage(content: String, images: List<ByteArray>? = null): NGemini
}

class GetContentUseCase(private val nGeminiRepository: NGeminiRepository) : IGetContentUseCase {
    override suspend fun getContentWithImage(content: String, images: List<ByteArray>?): NGemini{
        println("image size: $images")
        return if (images.isNullOrEmpty()){
            nGeminiRepository.generateContent(content)
        } else{
            nGeminiRepository.generateContentWithImage(content, images)
        }
    }

}