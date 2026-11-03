package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews
import com.coding.meet.gaminiaikmp.utils.isValidApiKey
import gemini_ai_kmp_app.shared.generated.resources.Res
import gemini_ai_kmp_app.shared.generated.resources.ic_paste
import org.jetbrains.compose.resources.painterResource

@Composable
fun ApiKeyAlertDialog(
    show: Boolean,
    initialApiKey: String = "",
    onDismiss: () -> Unit,
    onSave: (apiKey: String) -> Unit,
    onError: (title: String, message: String) -> Unit = { _, _ -> }
) {
    if (!show) return
    val clipboardManager = LocalClipboardManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember(show, initialApiKey) { mutableStateOf(initialApiKey) }

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
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "Gemini API Key",
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
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None),
                placeholder = {
                    Text("Paste your Gemini API Key here")
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            clipboardManager.getText()?.text?.let {
                                text = it.trim()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_paste),
                            contentDescription = "Paste API key",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    val trimmed = text.trim()
                    if (trimmed.isNotEmpty()) {
                        if (trimmed.isValidApiKey()) {
                            onSave(trimmed)
                            keyboardController?.hide()
                        } else {
                            onError(
                                "Invalid API Key",
                                "The API Key you entered is not valid. Please enter a valid API Key."
                            )
                        }
                    } else {
                        onError(
                            "Missing API Key",
                            "An API Key is required to proceed. Please enter your API Key."
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Save")
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
fun ApiKeyAlertDialogPreview() {
    ApiKeyAlertDialog(
        show = true,
        initialApiKey = "AIzaSyFakeKey12345",
        onDismiss = {},
        onSave = {}
    )
}
