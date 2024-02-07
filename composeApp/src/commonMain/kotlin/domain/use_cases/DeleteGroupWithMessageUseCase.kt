package domain.use_cases

import domain.respository.GeminiRepository


interface IDeleteGroupWithMessageUseCase {
    suspend fun deleteGroupWithMessage(
        groupId: String
    )
}

class DeleteGroupWithMessageUseCase(private val geminiRepository: GeminiRepository) : IDeleteGroupWithMessageUseCase {
    override  suspend fun deleteGroupWithMessage(
        groupId: String
    ) {
        geminiRepository.deleteGroupWithMessage(groupId)
    }

}