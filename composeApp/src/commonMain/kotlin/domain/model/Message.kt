package domain.model

data class Message(
  val messageId: String,
  val chatId: String,
  val content: String,
  val isAiResponse: Boolean,
  val timestamp: String,
)