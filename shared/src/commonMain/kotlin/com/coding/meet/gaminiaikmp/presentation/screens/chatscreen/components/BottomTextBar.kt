package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.coding.meet.gaminiaikmp.di.isNetworkAvailable
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.launch

@Composable
fun BottomTextBar(
    message: String,
    onMessageChange: (String) -> Unit,
    selectedImagesCount: Int = 0,
    onAddImage: (ByteArray) -> Unit = {},
    onSendMessage: () -> Unit = {},
    isApiLoading: Boolean = false,
    onShowAlert: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image
    ) { file ->
        if (file != null) {
            coroutineScope.launch {
                onAddImage(file.readBytes())
            }
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 3.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalIconButton(
                onClick = {
                    if (!isApiLoading) {
                        if (selectedImagesCount < 3) {
                            launcher.launch()
                        } else {
                            onShowAlert("Images Limit Reached", "You can only select up to 3 images.")
                        }
                    } else {
                        onShowAlert(
                            "API Call in Progress",
                            "Please wait while an API call is currently in progress."
                        )
                    }
                },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Attach image"
                )
            }

            TextField(
                value = message,
                onValueChange = onMessageChange,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                placeholder = {
                    Text(
                        "Ask Gemini anything...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            AnimatedContent(
                targetState = isApiLoading,
                label = "send_button"
            ) { loading ->
                if (loading) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.5.dp
                        )
                    }
                } else {
                    FilledIconButton(
                        onClick = {
                            val trimmedMessage = message.trim()
                            if (trimmedMessage.isNotEmpty()) {
                                if (!isNetworkAvailable()) {
                                    onShowAlert(
                                        "No Internet Connection",
                                        "Please check your network connection and try again."
                                    )
                                    return@FilledIconButton
                                }
                                onSendMessage()
                                keyboardController?.hide()
                            }
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send message"
                        )
                    }
                }
            }
        }
    }
}

@ComponentPreviews
@Composable
fun BottomTextBarPreview() {
    BottomTextBar(
        message = "Hello, can you help me with Kotlin Multiplatform?",
        onMessageChange = {}
    )
}
