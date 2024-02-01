package domain.model

enum class Role {
    YOU, GEMINI, ERROR
}

data class ChatMessage(
    val id:String,
    val chatId : String,
    var text: String,
    val images: List<ByteArray> = emptyList(),
    var participant: Role = Role.YOU,
    var isPending: Boolean = false
)
