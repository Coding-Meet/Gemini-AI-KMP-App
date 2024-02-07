package domain.use_cases

import domain.respository.GeminiRepository


interface IDeleteMessageUseCase {
    suspend fun deleteAllMessage(
        groupId: String
    )
}

class DeleteMessageUseCase(private val geminiRepository: GeminiRepository) : IDeleteMessageUseCase {
    override  suspend fun deleteAllMessage(
        groupId: String
    ) {
        geminiRepository.deleteAllMessage(groupId)
    }

}