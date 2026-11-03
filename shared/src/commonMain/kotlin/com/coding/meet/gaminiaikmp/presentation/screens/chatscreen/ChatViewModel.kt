package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.meet.gaminiaikmp.BuildKonfig
import com.coding.meet.gaminiaikmp.domain.model.Role
import com.coding.meet.gaminiaikmp.domain.use_cases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatViewModel(
    private val getContentUseCase: GetContentUseCase,
    private val getAllGroupUserCase: GetAllGroupUserCase,
    private val getAllMessageByGroupIdUserCase: GetAllMessageByGroupIdUserCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val updatePendingUseCase: UpdatePendingUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase,
    private val deleteGroupWithMessageUseCase: DeleteGroupWithMessageUseCase,
    private val getApiKeyUseCase: GetApiKeyUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    // Backward compatibility alias
    val chatUiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ChatUiEffect>(Channel.BUFFERED)
    val effect: Flow<ChatUiEffect> = _effect.receiveAsFlow()

    val lazyListState = LazyListState()

    // Backward compatibility properties synced with uiState
    var message: String
        get() = _uiState.value.inputMessage
        set(value) = _uiState.update { it.copy(inputMessage = value) }

    val imageUris = mutableStateListOf<ByteArray>()

    var isDeleteShowDialog: Boolean
        get() = _uiState.value.isDeleteShowDialog
        set(value) = _uiState.update { it.copy(isDeleteShowDialog = value) }

    var groupId: String
        get() = _uiState.value.groupId
        set(value) = _uiState.update { it.copy(groupId = value) }

    var failedMessageId: String
        get() = _uiState.value.failedMessageId
        set(value) = _uiState.update { it.copy(failedMessageId = value) }

    fun getApiKey(): String {
        return getApiKeyUseCase()
    }

    fun handleIntent(intent: ChatUiIntent) {
        when (intent) {
            is ChatUiIntent.LoadMessages -> {
                groupId = intent.groupId
                getMessageList(intent.isClicked)
            }
            is ChatUiIntent.UpdateInputMessage -> {
                _uiState.update { it.copy(inputMessage = intent.message) }
            }
            is ChatUiIntent.AddImage -> {
                if (imageUris.size < 3) {
                    imageUris.add(intent.imageBytes)
                    _uiState.update { it.copy(selectedImages = imageUris.toList()) }
                } else {
                    sendEffect(ChatUiEffect.ShowAlert("Images Limit Reached", "You can only select up to 3 images."))
                }
            }
            is ChatUiIntent.RemoveImage -> {
                imageUris.remove(intent.imageBytes)
                _uiState.update { it.copy(selectedImages = imageUris.toList()) }
            }
            is ChatUiIntent.ClearImages -> {
                imageUris.clear()
                _uiState.update { it.copy(selectedImages = emptyList()) }
            }
            is ChatUiIntent.SendMessage -> {
                val apiKey = if (intent.apiKey.isNotEmpty()) intent.apiKey else getApiKey()
                generateContentWithText(intent.groupId, intent.content, apiKey)
            }
            is ChatUiIntent.OpenDeleteDialog -> {
                _uiState.update { it.copy(isDeleteShowDialog = true) }
            }
            is ChatUiIntent.DismissDeleteDialog -> {
                _uiState.update { it.copy(isDeleteShowDialog = false) }
            }
            is ChatUiIntent.DeleteAllMessages -> {
                deleteAllMessage(intent.groupId)
            }
            is ChatUiIntent.DeleteGroupWithMessages -> {
                deleteGroupWithMessage(intent.groupId) {
                    sendEffect(ChatUiEffect.NavigateBack)
                }
            }
            is ChatUiIntent.ShowAlert -> {
                _uiState.update {
                    it.copy(
                        isAlertDialogShow = true,
                        alertTitleText = intent.title,
                        alertDescText = intent.message
                    )
                }
            }
            is ChatUiIntent.DismissAlert -> {
                _uiState.update { it.copy(isAlertDialogShow = false) }
            }
            is ChatUiIntent.HandleError -> {
                handleError(intent.messageId, intent.errorMessage)
            }
            is ChatUiIntent.RetryFailedMessage -> {
                if (failedMessageId.isNotEmpty()) {
                    handleError(failedMessageId, "Failed to generate content. Please try again.")
                }
            }
        }
    }

    fun getMessageList(isClicked: Boolean = false) {
        val currentGroupId = groupId
        if (currentGroupId.isEmpty()) return

        viewModelScope.launch(Dispatchers.Default) {
            if (isClicked) {
                _uiState.update { it.copy(isLoading = true) }
            }
            val group = getAllGroupUserCase.getGroupList().find { it.groupId == currentGroupId }
            delay(500)
            val messages = getAllMessageByGroupIdUserCase.getMessageListByGroupId(currentGroupId).reversed()
            _uiState.update {
                it.copy(
                    groupId = currentGroupId,
                    group = group,
                    message = messages,
                    isLoading = false
                )
            }
        }
    }

    fun deleteAllMessage(targetGroupId: String = groupId) {
        viewModelScope.launch(Dispatchers.Default) {
            deleteMessageUseCase.deleteAllMessage(targetGroupId)
            getMessageList(true)
        }
    }

    fun deleteGroupWithMessage(targetGroupId: String = groupId, deleteGroup: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Default) {
            deleteGroupWithMessageUseCase.deleteGroupWithMessage(targetGroupId)
            _uiState.update {
                it.copy(
                    group = null,
                    groupId = "",
                    message = emptyList(),
                    isDeleteShowDialog = false
                )
            }
            deleteGroup()
            sendEffect(ChatUiEffect.NavigateBack)
        }
    }

    fun generateContentWithText(targetGroupId: String, content: String, apiKey: String) {
        val images = imageUris.toList()
        imageUris.clear()
        _uiState.update { it.copy(selectedImages = emptyList(), inputMessage = "") }

        viewModelScope.launch(Dispatchers.Default) {
            val messageId = Uuid.random().toString()
            failedMessageId = messageId
            _uiState.update { it.copy(isApiLoading = true) }
            addToMessages(targetGroupId, messageId, content, Role.YOU, isPending = true, images)

            if (apiKey.isBlank()) {
                handleError(messageId, "Gemini API Key is missing. Please configure your API key in Settings.")
                return@launch
            }

            try {
                val gemini = getContentUseCase.getContentWithImage(content, apiKey, images)
                val generatedContent = gemini.candidates.firstOrNull()?.let { candidate ->
                    val textParts = candidate.content.parts.filter { it.text.isNotBlank() }
                    textParts.joinToString("\n") { it.text }
                } ?: ""

                if (generatedContent.isBlank()) {
                    throw Exception("No text was generated by the model. Please try again.")
                }

                val botId = Uuid.random().toString()
                handleContent(messageId, false)
                failedMessageId = ""
                addToMessages(
                    targetGroupId,
                    botId,
                    generatedContent,
                    Role.GEMINI,
                    isPending = false,
                    emptyList()
                )
                _uiState.update { it.copy(isApiLoading = false) }
            } catch (e: Exception) {
                val errorMessage = e.message?.takeIf { it.isNotBlank() } ?: "Failed to generate content. Please try again."
                handleError(messageId, errorMessage)
            }
        }
    }

    fun handleError(messageId: String, errorMessage: String) {
        viewModelScope.launch(Dispatchers.Default) {
            updatePendingUseCase.updatePending(messageId, false)
            val errorId = Uuid.random().toString()
            addToMessages(
                groupId,
                errorId,
                errorMessage,
                Role.ERROR,
                isPending = false,
                emptyList()
            )
            failedMessageId = ""
            _uiState.update { it.copy(isApiLoading = false) }
        }
    }

    private fun handleContent(messageId: String, isPending: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            updatePendingUseCase.updatePending(messageId, isPending)
            getMessageList()
        }
    }

    private fun addToMessages(
        targetGroupId: String,
        messageId: String,
        text: String,
        sender: Role,
        isPending: Boolean,
        images: List<ByteArray>
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            insertMessageUseCase.insertMessage(
                messageId, targetGroupId, text, images, sender, isPending
            )
            getMessageList()
        }
    }

    fun sendEffect(effect: ChatUiEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
