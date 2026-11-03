package com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews
import com.coding.meet.gaminiaikmp.utils.isValidApiKey

@Composable
fun EditApiKeyDialog(
    show: Boolean,
    initialKey: String,
    onDismiss: () -> Unit,
    onSaveKey: (String) -> Unit
) {
    if (!show) return

    var currentText by remember(initialKey) { mutableStateOf(initialKey) }
    val clipboardManager = LocalClipboardManager.current
    val isValid = currentText.isBlank() || currentText.isValidApiKey()

    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        icon = {
            Icon(
                imageVector = Icons.Default.Key,
                contentDescription = "API Key",
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Enter your Google Gemini API key. You can get one for free at Google AI Studio.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = currentText,
                    onValueChange = { currentText = it },
                    label = { Text("API Key") },
                    placeholder = { Text("AIzaSy...") },
                    singleLine = false,
                    maxLines = 3,
                    isError = !isValid,
                    supportingText = {
                        if (!isValid) {
                            Text("Please enter a valid Gemini API key format (AIzaSy...)")
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                val clip = clipboardManager.getText()?.text
                                if (!clip.isNullOrBlank()) {
                                    currentText = clip.trim()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentPaste,
                                contentDescription = "Paste from clipboard",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(20.dp),
                enabled = currentText.isNotBlank() && isValid,
                onClick = { onSaveKey(currentText) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Save Key")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

@ComponentPreviews
@Composable
fun EditApiKeyDialogPreview() {
    EditApiKeyDialog(
        show = true,
        initialKey = "AIzaSyD-sample-gemini-key",
        onDismiss = {},
        onSaveKey = {}
    )
}

