package domain.model

import kotlinx.serialization.Serializable


@Serializable
data class ChatMessage(
    val messageId:String,
    val groupId : String,
    var text: String,
    val images: List<ByteArray> = emptyList(),
    var participant: Role = Role.YOU,
    var isPending: Boolean = false
)
