package presenation.screens.chatscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.entity.ChatModel
import domain.GeminiResult
import domain.model.Message
import domain.use_cases.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import utils.currentDateTimeToString
import utils.generateRandomKey

class ChatViewModel(
    private val sendMessageUseCase: SendMessageUseCase,
) :ViewModel(){

    var chat: ChatModel by mutableStateOf(
        ChatModel(
            generateRandomKey(),
            currentDateTimeToString(),
            "New Message"
        )
    )

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    fun onSend(text: String) {
        viewModelScope.launch {
            sendMessageUseCase(
                SendMessageUseCase.Param(
                    chat = chat,
                    text = text,
//                    list = _uiState.value.messages
                    list = arrayListOf()
                )
            ).collect { result ->
                when (result) {
                    is GeminiResult.Loading -> _uiState.update { it.copy(onAnswering = result.isLoading) }
                    is GeminiResult.Success -> _uiState.update { it.copy(messages = result.data) }
                    is GeminiResult.Error -> {}
                }
            }
        }
    }
}

data class HomeUiState(
    val messages: List<Message> = emptyList(),
    val onAnswering: Boolean? = null,
    val size: Int = 0
)
