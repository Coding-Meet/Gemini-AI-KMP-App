package data.respository


import com.coding.meet.gaminiaikmp.GroupChat
import com.coding.meet.gaminiaikmp.Message
import data.mapper.toGemini
import data.network.GeminiService
import database.SharedDatabase
import domain.model.ChatMessage
import domain.model.Gemini
import domain.model.Group
import domain.model.Role
import domain.respository.GeminiRepository


class GeminiRepositoryImp(
    private val geminiService: GeminiService,
    private val sharedDatabase: SharedDatabase,
) : GeminiRepository {

    override suspend fun generateContent(content: String, apiKey: String): Gemini {
        return geminiService.generateContent(content, apiKey).toGemini()
    }


    override suspend fun generateContentWithImage(content: String, apiKey: String, images: List<ByteArray>): Gemini {
        return geminiService.generateContentWithImage(content, apiKey, images).toGemini()
    }

    override suspend fun insertGroup(
        groupId: String, groupName: String, date: String, icon: String
    ) {
        sharedDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.insertGroup(
                GroupChat = GroupChat(
                    groupId, groupName, date, icon
                )
            )
        }
    }

    override suspend fun insertMessage(
        id: String, groupId: String, text: String, images: List<ByteArray>, participant: Role, isPending: Boolean
    ) {
        sharedDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.insertMessage(
                Message(id, groupId, text, images, participant, isPending)
            )
        }
    }

    override suspend fun updatePendingStatus(messageId: String, isPending: Boolean) {
        sharedDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.updateMessageByMessageId(isPending, messageId)
        }
    }

    override suspend fun getGroupList(): List<Group> =
        sharedDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.getAllGroup().executeAsList().map {
                Group(
                    it.groupId,
                    it.title,
                    it.date,
                    it.image
                )
            }
        }

    override suspend fun getMessageListByGroupId(groupId: String): List<ChatMessage> =
        sharedDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.getChatByGroupId(groupId).executeAsList().map {
                ChatMessage(
                    it.messageId,
                    it.chatId,
                    it.content,
                    it.images,
                    it.participant,
                    it.isPending
                )
            }
        }

    override suspend fun deleteAllMessage(groupId: String) {
        sharedDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.deleteAllMessage(groupId)
        }
    }

    override suspend fun deleteGroupWithMessage(groupId: String) {
        sharedDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.deleteGroupWithMessage(groupId)
        }
    }
}