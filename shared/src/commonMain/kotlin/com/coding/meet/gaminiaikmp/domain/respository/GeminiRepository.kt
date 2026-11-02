package com.coding.meet.gaminiaikmp.domain.respository

import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Gemini
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.domain.model.Role


interface GeminiRepository {
    suspend fun generateContent(content: String, apiKey: String): Gemini

    suspend fun generateContentWithImage(content: String, apiKey: String, images: List<ByteArray> = emptyList()): Gemini


    suspend fun insertGroup(
        groupId: String,
        groupName: String,
        date: String,
        icon: String
    )

    suspend fun insertMessage(
        messageId: String,
        groupId: String,
        text: String,
        images: List<ByteArray>,
        participant: Role,
        isPending: Boolean
    )

    suspend fun updatePendingStatus(
        messageId: String,
        isPending: Boolean
    )


    suspend fun getGroupList(): List<Group>
    suspend fun getMessageListByGroupId(groupId: String): List<ChatMessage>

    suspend fun deleteAllMessage(groupId: String)
    suspend fun deleteGroupWithMessage(groupId: String)

}