package domain.use_cases

import domain.model.Role
import domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent


class InsertMessageUseCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun insertMessage(
        messageId: String,
        groupId: String,
        text: String,
        images: List<ByteArray>,
        participant: Role,
        isPending: Boolean
    ) {
        geminiRepository.insertMessage(messageId, groupId, text, images, participant, isPending)
    }

}