package com.coding.meet.gaminiaikmp.screenshot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Role
import com.coding.meet.gaminiaikmp.presentation.components.WelcomeScreen
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatScreenContent
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatUiState
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.DeleteChatAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.GroupListScreenContent
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.GroupUiState
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.ApiKeyAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.NewChatAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.SettingsScreenContent
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.SettingsUiState
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.DeleteAllChatsDialog
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.EditApiKeyDialog
import com.coding.meet.gaminiaikmp.theme.AppThemeMode
import com.coding.meet.gaminiaikmp.theme.LocalThemeMode

// =============================================================================
// Common Single-Pane Screen Content (Shared across Mobile, Desktop & Web)
// =============================================================================

@Composable
internal fun EmptyGroupListContent() {
    GroupListScreenContent(groupUiState = GroupUiState(groupList = emptyList()))
}

@Composable
internal fun ConversationListContent() {
    GroupListScreenContent(groupUiState = GroupUiState(groupList = sampleGroups))
}

@Composable
internal fun EmptyChatContent() {
    ChatScreenContent(
        chatUiState = ChatUiState(
            groupId = "1",
            group = sampleGroups[0],
            message = emptyList()
        ),
        group = sampleGroups[0],
        groupId = "1"
    )
}

@Composable
internal fun ChatLoadingContent() {
    ChatScreenContent(
        chatUiState = ChatUiState(
            groupId = "1",
            group = sampleGroups[0],
            isApiLoading = true,
            message = listOf(
                ChatMessage(
                    messageId = "1",
                    groupId = "1",
                    text = "what is kmp in jetpack compose",
                    participant = Role.YOU,
                    isPending = true
                )
            )
        ),
        group = sampleGroups[0],
        groupId = "1"
    )
}

@Composable
internal fun ChatResponseContent() {
    ChatScreenContent(
        chatUiState = ChatUiState(
            groupId = "1",
            group = sampleGroups[0],
            message = listOf(
                ChatMessage(
                    messageId = "2",
                    groupId = "1",
                    text = geminiResponseText,
                    participant = Role.GEMINI
                ),
                ChatMessage(
                    messageId = "1",
                    groupId = "1",
                    text = "what is kmp in jetpack compose",
                    participant = Role.YOU
                )
            )
        ),
        group = sampleGroups[0],
        groupId = "1"
    )
}

@Composable
internal fun MultimodalPromptContent(sampleImageBytes: ByteArray) {
    ChatScreenContent(
        chatUiState = ChatUiState(
            groupId = "1",
            group = sampleGroups[0],
            message = listOf(
                ChatMessage(
                    messageId = "3",
                    groupId = "1",
                    text = "what is written in this image",
                    images = if (sampleImageBytes.isNotEmpty()) listOf(sampleImageBytes) else emptyList(),
                    participant = Role.YOU
                )
            )
        ),
        group = sampleGroups[0],
        groupId = "1"
    )
}

@Composable
internal fun MultimodalResponseContent(sampleImageBytes: ByteArray) {
    ChatScreenContent(
        chatUiState = ChatUiState(
            groupId = "1",
            group = sampleGroups[0],
            message = listOf(
                ChatMessage(
                    messageId = "4",
                    groupId = "1",
                    text = geminiVisionText,
                    participant = Role.GEMINI
                ),
                ChatMessage(
                    messageId = "3",
                    groupId = "1",
                    text = "what is written in this image",
                    images = if (sampleImageBytes.isNotEmpty()) listOf(sampleImageBytes) else emptyList(),
                    participant = Role.YOU
                )
            )
        ),
        group = sampleGroups[0],
        groupId = "1"
    )
}

@Composable
internal fun SettingsScreenCommonContent() {
    val currentTheme = LocalThemeMode.current
    SettingsScreenContent(
        uiState = SettingsUiState(
            apiKey = "AIzaSy••••••••••••••••••••••••",
            isApiKeyMasked = true,
            themeMode = if (currentTheme == AppThemeMode.LIGHT) AppThemeMode.LIGHT else AppThemeMode.DARK
        )
    )
}

// =============================================================================
// Two-Pane Composite Screen States (Shared between Desktop and Web)
// =============================================================================

internal enum class TwoPaneScreenState {
    WELCOME,
    ADD_API_KEY_DIALOG,
    NEW_GROUP_DIALOG,
    CHAT_LOADING,
    CHAT_RESPONSE,
    MULTIMODAL_VISION,
    DELETE_CHAT_DIALOG,
    SETTINGS,
    EDIT_API_KEY_DIALOG,
    DELETE_ALL_CHATS_DIALOG
}

@Composable
internal fun CommonTwoPaneScreen(
    state: TwoPaneScreenState,
    sampleImageBytes: ByteArray = byteArrayOf()
) {
    when (state) {
        TwoPaneScreenState.WELCOME -> {
            TwoPaneLayout(
                groupUiState = GroupUiState(groupList = sampleGroups),
                selectedGroupId = null
            ) {
                WelcomeScreen()
            }
        }
        TwoPaneScreenState.ADD_API_KEY_DIALOG -> {
            Box(modifier = Modifier.fillMaxSize()) {
                TwoPaneLayout(
                    groupUiState = GroupUiState(groupList = sampleGroups),
                    selectedGroupId = null
                ) {
                    WelcomeScreen()
                }
                ApiKeyAlertDialog(
                    show = true,
                    onDismiss = {},
                    onSave = {}
                )
            }
        }
        TwoPaneScreenState.NEW_GROUP_DIALOG -> {
            Box(modifier = Modifier.fillMaxSize()) {
                TwoPaneLayout(
                    groupUiState = GroupUiState(groupList = sampleGroups),
                    selectedGroupId = null
                ) {
                    WelcomeScreen()
                }
                NewChatAlertDialog(
                    show = true,
                    onDismiss = {},
                    onSave = {}
                )
            }
        }
        TwoPaneScreenState.CHAT_LOADING -> {
            TwoPaneLayout(
                groupUiState = GroupUiState(groupList = sampleGroups),
                selectedGroupId = "1"
            ) {
                ChatLoadingContent()
            }
        }
        TwoPaneScreenState.CHAT_RESPONSE -> {
            TwoPaneLayout(
                groupUiState = GroupUiState(groupList = sampleGroups),
                selectedGroupId = "1"
            ) {
                ChatResponseContent()
            }
        }
        TwoPaneScreenState.MULTIMODAL_VISION -> {
            TwoPaneLayout(
                groupUiState = GroupUiState(groupList = sampleGroups),
                selectedGroupId = "1"
            ) {
                MultimodalResponseContent(sampleImageBytes)
            }
        }
        TwoPaneScreenState.DELETE_CHAT_DIALOG -> {
            Box(modifier = Modifier.fillMaxSize()) {
                TwoPaneLayout(
                    groupUiState = GroupUiState(groupList = sampleGroups),
                    selectedGroupId = "1"
                ) {
                    ChatResponseContent()
                }
                DeleteChatAlertDialog(
                    show = true,
                    onDismiss = {},
                    onConfirmDelete = {}
                )
            }
        }
        TwoPaneScreenState.SETTINGS -> {
            TwoPaneLayout(
                groupUiState = GroupUiState(groupList = sampleGroups),
                selectedGroupId = "1"
            ) {
                SettingsScreenCommonContent()
            }
        }
        TwoPaneScreenState.EDIT_API_KEY_DIALOG -> {
            Box(modifier = Modifier.fillMaxSize()) {
                TwoPaneLayout(
                    groupUiState = GroupUiState(groupList = sampleGroups),
                    selectedGroupId = "1"
                ) {
                    SettingsScreenCommonContent()
                }
                EditApiKeyDialog(
                    show = true,
                    initialKey = "AIzaSyD-sample-api-key-gemini",
                    onDismiss = {},
                    onSaveKey = {}
                )
            }
        }
        TwoPaneScreenState.DELETE_ALL_CHATS_DIALOG -> {
            Box(modifier = Modifier.fillMaxSize()) {
                TwoPaneLayout(
                    groupUiState = GroupUiState(groupList = sampleGroups),
                    selectedGroupId = "1"
                ) {
                    SettingsScreenCommonContent()
                }
                DeleteAllChatsDialog(
                    show = true,
                    onDismiss = {},
                    onConfirmDeleteAll = {}
                )
            }
        }
    }
}

