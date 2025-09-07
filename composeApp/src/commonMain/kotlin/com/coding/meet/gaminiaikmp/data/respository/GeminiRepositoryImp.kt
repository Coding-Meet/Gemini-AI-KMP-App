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
import com.coding.meet.gaminiaikmp.domain.model.Gemini
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
}