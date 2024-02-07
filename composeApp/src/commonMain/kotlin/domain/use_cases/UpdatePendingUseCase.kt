package domain.use_cases

import domain.respository.GeminiRepository


interface IUpdatePendingUseCase {
    suspend fun updatePending(
        messageId: String,
        isPending: Boolean
    )
}

class UpdatePendingUseCase(private val geminiRepository: GeminiRepository) : IUpdatePendingUseCase {
    override suspend fun updatePending(
        messageId: String,
        isPending: Boolean
    ) {
        geminiRepository.updatePendingStatus(messageId, isPending)
    }

}