package screens.chatscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.model.ChatMessage
import domain.model.Role
import domain.use_cases.IGetContentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import org.koin.mp.KoinPlatform
import utils.AppCoroutineDispatchers
import utils.generateRandomKey


class ChatViewModel(
    private val getContentUseCase: IGetContentUseCase
) : ViewModel() {

    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()
    private val appCoroutineDispatchers: AppCoroutineDispatchers = KoinPlatform.getKoin().get()

    var userText by mutableStateOf("")
    val imageUris = mutableStateListOf<ByteArray>()

    fun generateContentWithText(chatId: String, content: String, apiKey: String) {
        val images = imageUris.toList()
        imageUris.clear()
        viewModelScope.launch(appCoroutineDispatchers.io) {
            val userId = generateRandomKey()
            _chatUiState.value =
                _chatUiState.value.copy(
                    isLoading = true
                )
            addToMessages(chatId, userId, content, Role.YOU, isPending = true, isLoading = true, images)
            try {
                val gemini = getContentUseCase.getContentWithImage(content, apiKey, images)
                val generatedContent =
                    if (gemini.candidates != null) {
                        if (gemini.candidates.isNotEmpty()){
                            gemini.candidates[0].content.parts[0].text
                        }else{
                            "Failed to generate content. Please try again."
                        }
                    } else {
                        "Failed to generate content. Please try again."
                    }
                val botId = generateRandomKey()
                handleContent(userId, false)
                addToMessages(
                    chatId,
                    botId,
                    generatedContent,
                    Role.GEMINI,
                    isPending = false,
                    isLoading = true,
                    emptyList()
                )
                _chatUiState.value =
                    _chatUiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                val errorId = generateRandomKey()
                handleContent(userId, false)
                addToMessages(
                    chatId,
                    errorId,
                    e.message ?: "Failed to generate content. Please try again.",
                    Role.ERROR,
                    isPending = false,
                    isLoading = false,
                    emptyList()
                )
            }
        }
    }


    private fun handleContent(id: String, isPending: Boolean) {
        val pos = _chatUiState.value.message.indexOfFirst {
            it.id == id
        }
        _chatUiState.value.message[pos].isPending = isPending
        _chatUiState.value = _chatUiState.value.copy(
            message = _chatUiState.value.message
        )
    }

    private fun addToMessages(
        chatId: String,
        id: String,
        text: String,
        sender: Role,
        isPending: Boolean,
        isLoading: Boolean,
        images: List<ByteArray>
    ) {
        val newMessage = ChatMessage(
            id = id,
            chatId = chatId,
            text = text,
            images = images,
            participant = sender,
            isPending = isPending
        )
        _chatUiState.value = _chatUiState.value.copy(
            message = _chatUiState.value.message + newMessage,
            isLoading = isLoading,
        )
    }

}