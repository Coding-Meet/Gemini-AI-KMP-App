package presentation.screens.chatscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.model.Role
import domain.use_cases.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import utils.AppCoroutineDispatchers
import utils.generateRandomKey


class ChatViewModel(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val getContentUseCase: GetContentUseCase,
    private val getAllMessageByGroupIdUserCase: GetAllMessageByGroupIdUserCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val updatePendingUseCase: UpdatePendingUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val deleteGroupWithMessageUseCase: DeleteGroupWithMessageUseCase,
) : ViewModel() {

    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()

    var message by mutableStateOf("")
    val imageUris = mutableStateListOf<ByteArray>()

    var isDeleteShowDialog by mutableStateOf(false)

    var groupId by mutableStateOf("")

    var failedMessageId by mutableStateOf("")


    fun getMessageList(isClicked: Boolean = false) {

        viewModelScope.launch(appCoroutineDispatchers.io) {
            if (isClicked) {
                _chatUiState.update {
                    _chatUiState.value.copy(
                        isLoading = true
                    )
                }
            }

            delay(500)
            _chatUiState.update {
                _chatUiState.value.copy(
                    message = getAllMessageByGroupIdUserCase.getMessageListByGroupId(groupId).reversed()
                )
            }
            _chatUiState.update {
                _chatUiState.value.copy(
                    isLoading = false
                )
            }
        }

    }

    fun deleteAllMessage() {
        viewModelScope.launch(appCoroutineDispatchers.io) {
            deleteMessageUseCase.deleteAllMessage(groupId)
            getMessageList(true)
        }
    }

    fun deleteGroupWithMessage(
        deleteGroup: () -> Unit
    ) {
        viewModelScope.launch(appCoroutineDispatchers.io) {
            deleteGroupWithMessageUseCase.deleteGroupWithMessage(groupId)
            deleteGroup()
        }
    }

    fun generateContentWithText(groupId: String, content: String, apiKey: String) {
        val images = imageUris.toList()
        imageUris.clear()
        viewModelScope.launch(appCoroutineDispatchers.io) {
            val messageId = generateRandomKey()
            failedMessageId = messageId
            _chatUiState.update {
                _chatUiState.value.copy(
                    isApiLoading = true
                )
            }
            addToMessages(groupId, messageId, content, Role.YOU, isPending = true, images)
            try {
                val gemini = getContentUseCase.getContentWithImage(content, apiKey, images)
                val generatedContent = gemini.candidates[0].content.parts[0].text
                val botId = generateRandomKey()
                handleContent(messageId, false)
                failedMessageId = ""
                addToMessages(
                    groupId,
                    botId,
                    generatedContent,
                    Role.GEMINI,
                    isPending = false,
                    emptyList()
                )
                _chatUiState.update {
                    _chatUiState.value.copy(isApiLoading = false)
                }
            } catch (e: Exception) {
                val errorMessage = if (e.message != null) {
                    if (e.message.toString().contains("Illegal input: Field")) {
                        "Failed to generate content. Please try again."
                    } else {
                        e.message.toString()
                    }
                } else {
                    "Failed to generate content. Please try again."
                }
                handleError(messageId, errorMessage)
            }
        }
    }

    fun handleError(messageId: String, errorMessage: String) {
        viewModelScope.launch(appCoroutineDispatchers.io) {
            updatePendingUseCase.updatePending(messageId, false)
            val errorId = generateRandomKey()
            addToMessages(
                groupId,
                errorId,
                errorMessage,
                Role.ERROR,
                isPending = false,
                emptyList()
            )
            failedMessageId = ""
            _chatUiState.update {
                _chatUiState.value.copy(isApiLoading = false)
            }
        }
    }


    private fun handleContent(messageId: String, isPending: Boolean) {
        viewModelScope.launch(appCoroutineDispatchers.io) {
            updatePendingUseCase.updatePending(messageId, isPending)
            getMessageList()
        }
    }

    private fun addToMessages(
        groupId: String,
        messageId: String,
        text: String,
        sender: Role,
        isPending: Boolean,
        images: List<ByteArray>
    ) {
        viewModelScope.launch(appCoroutineDispatchers.io) {
            insertMessageUseCase.insertMessage(
                messageId, groupId, text, images, sender, isPending
            )
            getMessageList()
        }
    }

}