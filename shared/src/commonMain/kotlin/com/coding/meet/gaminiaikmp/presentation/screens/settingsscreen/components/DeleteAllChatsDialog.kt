package com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews

@Composable
fun DeleteAllChatsDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirmDeleteAll: () -> Unit
) {
    if (!show) return

    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        icon = {
            Icon(
                imageVector = Icons.Default.DeleteForever,
                contentDescription = "Delete All",
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Delete All Conversations?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = "This will permanently delete all your conversation history, chat messages, and images stored on this device. This action cannot be reversed.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(20.dp),
                onClick = onConfirmDeleteAll,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Delete Everything")
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
fun DeleteAllChatsDialogPreview() {
    DeleteAllChatsDialog(
        show = true,
        onDismiss = {},
        onConfirmDeleteAll = {}
    )
}

