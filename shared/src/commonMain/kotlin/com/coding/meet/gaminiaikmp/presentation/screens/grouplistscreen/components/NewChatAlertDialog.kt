package com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews

@Composable
fun NewChatAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onSave: (groupName: String) -> Unit,
    onError: (title: String, message: String) -> Unit = { _, _ -> }
) {
    if (!show) return
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember(show) { mutableStateOf("") }

    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
        onDismissRequest = {
            onDismiss()
            keyboardController?.hide()
        },
        shape = RoundedCornerShape(28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        icon = {
            Icon(
                imageVector = Icons.Default.AddComment,
                contentDescription = "New Chat",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "New Conversation",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                placeholder = {
                    Text("e.g. Travel Guide, Python Helper")
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    val trimmed = text.trim()
                    if (trimmed.isNotEmpty()) {
                        text = ""
                        onSave(trimmed)
                        keyboardController?.hide()
                    } else {
                        onError("Conversation Name", "Please enter a name for the conversation.")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    keyboardController?.hide()
                }
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

@ComponentPreviews
@Composable
fun NewChatAlertDialogPreview() {
    NewChatAlertDialog(
        show = true,
        onDismiss = {},
        onSave = {}
    )
}

