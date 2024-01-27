package domain.models

import kotlinx.serialization.Serializable


@Serializable
data class ChatMessage(
    val text: String,
    val images: List<ByteArray> = emptyList(),
    val role: String = Role.USER.roleName,
) {
    val isModel: Boolean
        get() = role == Role.MODEL.roleName
}

enum class Role(val roleName: String) {
    USER("You"),
    MODEL("NGemini")
}
