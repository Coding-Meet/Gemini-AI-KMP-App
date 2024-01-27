package screens.chatscreen

import domain.models.ChatMessage
import domain.models.Role
import domain.use_cases.IGetContentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope


class ChatViewModel(
    private val getContentUseCase: IGetContentUseCase
) : ViewModel() {

    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()

    fun generateContentWithText(content: String, images: List<ByteArray>? = null) {
        viewModelScope.launch(Dispatchers.Unconfined) {
            _chatUiState.value =
                _chatUiState.value.copy(
                    isLoading = true,
                    isConnectionError = false
                )
            if (images != null) {
                addToMessages(content, Role.USER, images)
            }
            try {
                _chatUiState.value =
                    _chatUiState.value.copy(isLoading = true, isConnectionError = false)
                val nGemini = getContentUseCase.getContentWithImage(content, images)
                val generatedContent =
                    nGemini.candidates?.get(0)?.content?.parts?.get(0)?.text.toString()
                updateLastBotMessage(generatedContent)
                addToMessages(
                    generatedContent,
                    Role.MODEL,
                    emptyList()
                )
                _chatUiState.value =
                    _chatUiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                handleContentGenerationError()
            }
        }
    }


    private fun handleContentGenerationError() {
        _chatUiState.value = _chatUiState.value.copy(
            isLoading = false,
            isConnectionError = true,
            errorMessage = "Failed to generate content. Please try again."
        )
    }


    private fun updateLastBotMessage(text: String) {
        val messages = _chatUiState.value.message.toMutableList()
        if (messages.isNotEmpty() && messages.last()?.role == Role.MODEL.roleName) {
            val last = messages.last()
            val updatedMessage = last.copy(text = text)
            messages[messages.lastIndex] = updatedMessage
            _chatUiState.value = _chatUiState.value.copy(
                message = messages,
            )
        }
    }


    private fun addToMessages(
        text: String,
        sender: Role,
        images: List<ByteArray>
    ) {
        val newMessage = ChatMessage(text, images, sender.roleName)
        _chatUiState.value = _chatUiState.value.copy(
            message = _chatUiState.value.message + newMessage,
            isLoading = true,
            images = images
        )
    }


    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}