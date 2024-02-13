package presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import di.ImagePicker
import di.isNetworkAvailable
import presentation.screens.chatscreen.ChatUiState
import presentation.screens.chatscreen.ChatViewModel
import presentation.screens.mainscreen.GroupUiState
import presentation.screens.mainscreen.MainViewModel
import theme.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomTextBar(
    mainViewModel: MainViewModel,
    chatViewModel: ChatViewModel,
    chatUiState: ChatUiState,
    groupUiState: GroupUiState
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showFilePicker by remember { mutableStateOf(false) }

    ImagePicker(showFilePicker,{
        showFilePicker = false
    }, {
        it?.let { chatViewModel.imageUris.add(it) }
    })
    Row(
        modifier = Modifier.background(borderColor).padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier.padding(horizontal = 8.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = lightBorderColor
            ),
            onClick = {
                if (!chatUiState.isApiLoading) {
                    if (chatViewModel.imageUris.size != 3) {
                        showFilePicker = true
                    } else {
                        mainViewModel.alertTitleText = "Images Limit Reached"
                        mainViewModel.alertDescText = "You can only select up to 3 images."
                        mainViewModel.isAlertDialogShow = true
                    }
                } else {
                    mainViewModel.alertTitleText = "API Call in Progress"
                    mainViewModel.alertDescText =
                        "Please wait while there is already an API call going on, so please be patient."
                    mainViewModel.isAlertDialogShow = true
                }
            }) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "upload", tint = whiteColor
            )
        }
        TextField(
            value = chatViewModel.message,
            onValueChange = { chatViewModel.message = it },
            modifier = Modifier.weight(1f).background(borderColor),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            maxLines = 3,
            placeholder = {
                Text(
                    "Type a message", color = textHintColor
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = lightBorderColor,
                focusedContainerColor = lightBorderColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = whiteColor,
                unfocusedTextColor = whiteColor,
                focusedPlaceholderColor = whiteColor,
                unfocusedPlaceholderColor = whiteColor,
                unfocusedLabelColor = whiteColor,
                focusedLabelColor = whiteColor,
                cursorColor = whiteColor,
                selectionColors = TextSelectionColors(selectionColor, selectionColor)
            ),
            shape = RoundedCornerShape(10.dp)
        )
        IconButton(
            modifier = Modifier.padding(horizontal = 8.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = lightBorderColor
            ),
            onClick = {
                if (!chatUiState.isApiLoading) {
                    if (chatViewModel.message.isNotEmpty()) {
                        if (isNetworkAvailable()) {
                            chatViewModel.generateContentWithText(
                                groupUiState.data[mainViewModel.currentPos].groupId,
                                chatViewModel.message,
                                mainViewModel.getApikeyLocalStorage(),
                            )
                            keyboardController?.hide()
                            chatViewModel.message = ""
                        } else {
                            mainViewModel.alertTitleText = "Network Issue"
                            mainViewModel.alertDescText = "Please check your internet connection and try again."
                            mainViewModel.isAlertDialogShow = true
                        }
                    } else {
                        mainViewModel.alertTitleText = "Message"
                        mainViewModel.alertDescText = "Please enter a message before sending."
                        mainViewModel.isAlertDialogShow = true
                    }
                } else {
                    mainViewModel.alertTitleText = "API Call in Progress"
                    mainViewModel.alertDescText =
                        "Please wait while there is already an API call going on, so please be patient."
                    mainViewModel.isAlertDialogShow = true
                }
            }) {
            AnimatedContent(chatUiState.isApiLoading) { generating ->
                if (generating) {
                    CircularProgressIndicator(
                        trackColor = whiteColor,
                        color = lightBorderColor,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "send",
                        tint = whiteColor
                    )
                }
            }
        }

    }
}
