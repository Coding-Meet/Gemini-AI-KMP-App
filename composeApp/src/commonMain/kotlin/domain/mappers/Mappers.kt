package domain.mappers

import data.entity.generateContent.GeminiGenerateContentResponse
import domain.model.Message
import utils.generateRandomKey

fun GeminiGenerateContentResponse.toDomain(): Message {
    return Message(
        messageId = generateRandomKey(),
//        chatId = chat.chatId,
        chatId = generateRandomKey(),
        content = candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "",
        isAiResponse = true,
        timestamp = ""
    )
}
