package models


enum class Role {
    YOU, GEMINI, ERROR
}

data class ChatMessage(
    val id: String,
    var text: String,
    val participant: Role = Role.YOU,
    var isPending: Boolean = false
)
