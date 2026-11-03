package com.coding.meet.gaminiaikmp.data.respository


import app.cash.sqldelight.async.coroutines.awaitAsList
import com.coding.meet.gaminiaikmp.GroupChat
import com.coding.meet.gaminiaikmp.Message
import com.coding.meet.gaminiaikmp.data.database.SharedDatabase
import com.coding.meet.gaminiaikmp.data.mapper.toGemini
import com.coding.meet.gaminiaikmp.data.network.GeminiService
import com.coding.meet.gaminiaikmp.di.getPlatform
import com.coding.meet.gaminiaikmp.di.readChatMessageKStore
import com.coding.meet.gaminiaikmp.di.readGroupKStore
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.DEFAULT_GEMINI_MODELS
import com.coding.meet.gaminiaikmp.domain.model.Gemini
import com.coding.meet.gaminiaikmp.domain.model.GeminiModelInfo
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.domain.model.Role
import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import com.coding.meet.gaminiaikmp.utils.TYPE
import io.github.xxfast.kstore.extensions.getOrEmpty
import io.github.xxfast.kstore.extensions.minus
import io.github.xxfast.kstore.extensions.plus


class GeminiRepositoryImp(
    private val geminiService: GeminiService,
    private val sharedDatabase: SharedDatabase
) : GeminiRepository {
    private val platform = getPlatform()
    override suspend fun generateContent(content: String, apiKey: String): Gemini {
        return geminiService.generateContent(content, apiKey).toGemini()
    }


    override suspend fun generateContentWithImage(content: String, apiKey: String, images: List<ByteArray>): Gemini {
        return geminiService.generateContentWithImage(content, apiKey, images).toGemini()
    }

    override suspend fun insertGroup(
        groupId: String, groupName: String, date: String, icon: String
    ) {
        if (platform == TYPE.WEB) {
            readGroupKStore {
                it.plus(
                    Group(
                        groupId, groupName, date, icon
                    )
                )
            }
        }else{
            sharedDatabase { appDatabase ->
                appDatabase.appDatabaseQueries.insertGroup(
                    GroupChat = GroupChat(
                        groupId, groupName, date, icon
                    )
                )
            }
        }

    }

    override suspend fun insertMessage(
        messageId: String, groupId: String, text: String, images: List<ByteArray>, participant: Role, isPending: Boolean
    ) {
        if (platform == TYPE.WEB) {
            readChatMessageKStore {
                it.plus(
                    ChatMessage(
                        messageId, groupId, text, images, participant, isPending
                    )
                )
            }
        } else {
            sharedDatabase { appDatabase ->
                appDatabase.appDatabaseQueries.insertMessage(
                    Message(messageId, groupId, text, images, participant, isPending)
                )
            }
        }
    }

    override suspend fun updatePendingStatus(messageId: String, isPending: Boolean) {
        if (platform == TYPE.WEB) {
            readChatMessageKStore { kStore ->
                val chatList: List<ChatMessage> = kStore.getOrEmpty()
                if (chatList.isNotEmpty()) {
                    val index = chatList.indexOfFirst { it.messageId == messageId }
                    kStore.update {
                        it?.let {
                            it[index].isPending = isPending
                        }
                        it
                    }
                }
            }
        } else {
            sharedDatabase { appDatabase ->
                appDatabase.appDatabaseQueries.updateMessageByMessageId(isPending, messageId)
            }
        }
    }

    override suspend fun getGroupList(): List<Group> {
        val groupList = arrayListOf<Group>()
        if (platform == TYPE.WEB) {
            readGroupKStore {
                groupList.addAll(it.getOrEmpty())
            }
        } else {
            sharedDatabase { appDatabase ->
                groupList.addAll(appDatabase.appDatabaseQueries.getAllGroup().awaitAsList().map {
                    Group(
                        it.groupId,
                        it.title,
                        it.date,
                        it.image
                    )
                })
            }
        }
        return groupList
    }

    override suspend fun getMessageListByGroupId(groupId: String): List<ChatMessage> {
        val chatList = arrayListOf<ChatMessage>()
        if (platform == TYPE.WEB) {
            readChatMessageKStore {kStore->
                chatList.addAll(kStore.getOrEmpty().filter { it.groupId == groupId })
            }
        } else {
            sharedDatabase { appDatabase ->
                chatList.addAll(appDatabase.appDatabaseQueries.getChatByGroupId(groupId).awaitAsList().map {
                    ChatMessage(
                        it.messageId,
                        it.chatId,
                        it.content,
                        it.images,
                        it.participant,
                        it.isPending
                    )
                })
            }
        }
        return chatList
    }

    override suspend fun deleteAllMessage(groupId: String) {
        if (platform == TYPE.WEB) {
            readChatMessageKStore { kStore ->
                val chatList: List<ChatMessage> = kStore.getOrEmpty()
                if (chatList.isNotEmpty()) {
                    val chatMessageList = chatList.filter { it.groupId == groupId }
                    chatMessageList.map { chatMessage ->
                        kStore.minus(
                            chatMessage
                        )
                    }
                }
            }
        } else {
            sharedDatabase { appDatabase ->
                appDatabase.appDatabaseQueries.deleteAllMessage(groupId)
            }
        }
    }

    override suspend fun deleteGroupWithMessage(groupId: String) {
        sharedDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.deleteAllMessage(groupId)
            appDatabase.appDatabaseQueries.deleteGroupWithMessage(groupId)
        }
    }

    override suspend fun clearAllConversations() {
        if (platform == TYPE.WEB) {
            readGroupKStore { it.reset() }
            readChatMessageKStore { it.reset() }
        } else {
            sharedDatabase { appDatabase ->
                appDatabase.appDatabaseQueries.clearAllMessages()
                appDatabase.appDatabaseQueries.clearAllGroups()
            }
        }
    }

    override suspend fun getAvailableModels(apiKey: String): List<GeminiModelInfo> {
        if (apiKey.isBlank()) return DEFAULT_GEMINI_MODELS
        return try {
            val response = geminiService.listModels(apiKey)
            val filtered = response.models.filter { modelDto ->
                val id = modelDto.name.removePrefix("models/").lowercase()
                modelDto.supportedGenerationMethods.contains("generateContent") &&
                    // Filter out audio / voice / speech models
                    !id.contains("tts") &&
                    !id.contains("live") &&
                    !id.contains("transcribe") &&
                    !id.contains("translate") &&
                    // Filter out image, video and music generators
                    !id.contains("image") &&
                    !id.contains("imagen") &&
                    !id.contains("banana") &&
                    !id.contains("omni") &&
                    !id.contains("lyria") &&
                    // Filter out shut down models
                    !id.contains("gemini-2.0") &&
                    !id.contains("gemini-3-pro-preview") &&
                    !id.contains("gemini-3.1-flash-lite-preview") &&
                    // Filter out specialized agent and robotics models
                    !id.contains("deep-research") &&
                    !id.contains("antigravity") &&
                    !id.contains("robotics") &&
                    !id.contains("computer-use") &&
                    !id.contains("embedding") &&
                    !id.contains("aqa")
            }.map { modelDto ->
                val rawId = modelDto.name.removePrefix("models/")
                val id = rawId.lowercase()
                val displayName = modelDto.displayName ?: rawId
                val desc = modelDto.description ?: ""
                val isThinking = modelDto.thinking == true
                val badge = when {
                    id == "gemini-3.8-flash" -> "Recommended"
                    id == "gemini-4-argon" -> "Gemini 4"
                    id == "gemini-3.5-flash-lite" -> "Fast & Lite"
                    id == "gemini-3.6-flash" -> "Stable"
                    id == "gemini-3.1-flash-lite" -> "Lite"
                    id.contains("latest") -> "Latest"
                    id == "gemini-2.5-flash-lite" -> "2.5 Lite"
                    id == "gemini-2.5-flash" || id == "gemini-2.5-pro" -> "Legacy"
                    id.contains("gemma") -> "Gemma"
                    id.contains("preview") -> "Preview"
                    isThinking -> "Thinking"
                    else -> null
                }
                GeminiModelInfo(
                    id = rawId,
                    name = displayName,
                    description = desc,
                    badge = badge,
                    inputTokenLimit = modelDto.inputTokenLimit,
                    outputTokenLimit = modelDto.outputTokenLimit,
                    isThinking = isThinking
                )
            }
            if (filtered.isNotEmpty()) {
                filtered.sortedWith(
                    compareByDescending<GeminiModelInfo> { it.id.lowercase() == "gemini-3.8-flash" }
                        .thenByDescending { it.id.lowercase() == "gemini-4-argon" }
                        .thenByDescending { it.id.lowercase() == "gemini-3.5-flash-lite" }
                        .thenByDescending { it.id.lowercase() == "gemini-3.6-flash" }
                        .thenByDescending { it.id.lowercase() == "gemini-3.1-flash-lite" }
                        .thenByDescending { it.id.lowercase().contains("latest") }
                        .thenByDescending { it.id.lowercase().contains("3.") }
                        .thenByDescending { it.id.lowercase().contains("gemma") }
                        .thenByDescending { it.id.lowercase().contains("2.5") }
                )
            } else {
                DEFAULT_GEMINI_MODELS
            }
        } catch (e: Exception) {
            println("Failed to fetch models from API, falling back to defaults: ${e.message}")
            DEFAULT_GEMINI_MODELS
        }
    }
}