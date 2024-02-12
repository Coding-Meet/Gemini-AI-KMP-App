package presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import di.clipData
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.screens.chatscreen.ChatViewModel
import presentation.screens.mainscreen.MainViewModel
import theme.*
import utils.Screens
import utils.currentDateTimeToString
import utils.generateRandomKey
import utils.isValidApiKey

@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ApiKeyAlertDialogBox(mainViewModel: MainViewModel) {
    val coroutine = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    if (mainViewModel.isApiShowDialog) {
        AlertDialog(properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            icon = {
                Icon(Icons.Default.Settings, contentDescription = "setting")
            },
            containerColor = lightBackgroundColor,
            textContentColor = whiteColor,
            iconContentColor = whiteColor,
            titleContentColor = whiteColor,
            title = {
                Text(text = "Add Api Key")
            },
            text = {
                TextField(
                    value = mainViewModel.apiKeyText,
                    onValueChange = { mainViewModel.apiKeyText = it },
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                        .background(borderColor),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    placeholder = {
                        Text("Enter the Api Key", color = textHintColor)
                    },
                    singleLine = true,
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
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = lightBorderColor
                            ),
                            onClick = {
                                coroutine.launch {
                                    clipData(clipboardManager)?.let {
                                        mainViewModel.apiKeyText = it
                                    }
                                }
                            }) {
                            Icon(
                                painter = painterResource("ic_paste.xml"),
                                contentDescription = "api key",
                                tint = whiteColor
                            )
                        }
                    }
                )
            },
            onDismissRequest = {
                mainViewModel.apiKeyText = ""
                mainViewModel.isApiShowDialog = false
                keyboardController?.hide()
            },

            confirmButton = {
                Button(
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        if (mainViewModel.apiKeyText.trim().isNotEmpty()) {
                            if (mainViewModel.apiKeyText.trim().isValidApiKey()) {
                                mainViewModel.setApikeyLocalStorage(mainViewModel.apiKeyText.trim())
                                mainViewModel.apiKeyText = ""
                                mainViewModel.isApiShowDialog = false
                                keyboardController?.hide()
                            } else {
                                mainViewModel.alertTitleText = "Invalid API Key"
                                mainViewModel.alertDescText =
                                    "The API Key you entered is not valid. Please enter a valid API Key."
                                mainViewModel.isAlertDialogShow = true
                            }
                        } else {
                            mainViewModel.alertTitleText = "Missing API Key"
                            mainViewModel.alertDescText =
                                "An API Key is required to proceed. Please enter your API Key."
                            mainViewModel.isAlertDialogShow = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        mainViewModel.apiKeyText = ""
                        mainViewModel.isApiShowDialog = false
                        keyboardController?.hide()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewChatAlertDialogBox(mainViewModel: MainViewModel) {
    if (mainViewModel.isNewChatShowDialog) {
        val keyboardController = LocalSoftwareKeyboardController.current
        AlertDialog(properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            icon = {
                Icon(Icons.Default.Person, contentDescription = "robot")
            },
            containerColor = lightBackgroundColor,
            textContentColor = whiteColor,
            iconContentColor = whiteColor,
            titleContentColor = whiteColor,
            title = {
                Text(text = "New Group")
            },
            text = {
                TextField(
                    value = mainViewModel.newGroupText,
                    onValueChange = { mainViewModel.newGroupText = it },
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                        .background(borderColor),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    placeholder = {
                        Text("Enter the Chat Name", color = textHintColor)
                    },
                    singleLine = true,
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
                    shape = RoundedCornerShape(10.dp),
                )
            },
            onDismissRequest = {
                mainViewModel.newGroupText = ""
                mainViewModel.isNewChatShowDialog = false
                keyboardController?.hide()
            },

            confirmButton = {
                Button(
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        if (mainViewModel.newGroupText.trim().isNotEmpty()) {
                            mainViewModel.addNewGroup(
                                generateRandomKey(),
                                mainViewModel.newGroupText.trim(),
                                currentDateTimeToString(),
                                "robot_${(1..8).random()}.png"
                            )
                            mainViewModel.newGroupText = ""
                            mainViewModel.isNewChatShowDialog = false
                            keyboardController?.hide()
                        } else {
                            mainViewModel.alertTitleText = "Group Name"
                            mainViewModel.alertDescText = "Group Name is Required"
                            mainViewModel.isAlertDialogShow = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        mainViewModel.newGroupText = ""
                        mainViewModel.isNewChatShowDialog = false
                        keyboardController?.hide()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun DeleteChatAlertDialogBox(chatViewModel: ChatViewModel, mainViewModel: MainViewModel) {
    if (chatViewModel.isDeleteShowDialog) {
        AlertDialog(
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            icon = {
                Icon(Icons.Default.Delete, contentDescription = "delete")
            },
            containerColor = lightBackgroundColor,
            textContentColor = whiteColor,
            iconContentColor = whiteColor,
            titleContentColor = whiteColor,
            title = {
                Text(text = "Delete Messages")
            },
            text = {
                Text(text = "Are you sure you want to delete all messages?")
            },
            onDismissRequest = {
                chatViewModel.isDeleteShowDialog = false
            },

            confirmButton = {
                Button(
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        chatViewModel.deleteAllMessage()
                        chatViewModel.isDeleteShowDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = redColor,
                        contentColor = whiteColor,
                    ),
                ) {
                    Text("Delete All Message")
                }

                Button(
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        chatViewModel.deleteGroupWithMessage {
                            chatViewModel.isDeleteShowDialog = false
                            mainViewModel.currentPos = -1
                            mainViewModel.screens = Screens.MAIN
                            mainViewModel.getGroupList()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = redColor,
                        contentColor = whiteColor,
                    ),
                ) {
                    Text("Delete Group with All Message")
                }

            },
            dismissButton = {
                Button(
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        chatViewModel.isDeleteShowDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AlertDialogLayout(mainViewModel: MainViewModel) {
    if (mainViewModel.isAlertDialogShow) {
        AlertDialog(
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            icon = {
                Icon(Icons.Default.Info, contentDescription = "info")
            },
            containerColor = lightBackgroundColor,
            textContentColor = whiteColor,
            iconContentColor = whiteColor,
            titleContentColor = whiteColor,
            title = {
                Text(text = mainViewModel.alertTitleText)
            },
            text = {
                Text(text = mainViewModel.alertDescText)
            },
            onDismissRequest = {
                mainViewModel.isAlertDialogShow = false
            },

            confirmButton = {
                Button(
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        mainViewModel.isAlertDialogShow = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Cancel")
                }
            },
        )
    }
}