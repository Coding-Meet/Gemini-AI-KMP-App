package domain.use_cases

import domain.model.Role
import domain.respository.GeminiRepository


interface IInsertMessageUseCase {
    suspend fun insertMessage(
        id: String,
        groupId: String,
        text: String,
        images: List<ByteArray>,
        participant: Role,
        isPending: Boolean
    )
}

class InsertMessageUseCase(private val geminiRepository: GeminiRepository) : IInsertMessageUseCase {
    override suspend fun insertMessage(
        id: String,
        groupId: String,
        text: String,
        images: List<ByteArray>,
        participant: Role,
        isPending: Boolean
    ) {
        geminiRepository.insertMessage(id, groupId, text, images, participant, isPending)
    }

}