package com.coding.meet.gaminiaikmp.screenshot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apadmi.cmpdevicepreview.devices.DevicePresets
import com.coding.meet.gaminiaikmp.presentation.components.WelcomeScreen
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.DeleteChatAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.GroupUiState
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.ApiKeyAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.NewChatAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.DeleteAllChatsDialog
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.EditApiKeyDialog
import java.io.File

/**
 * All canonical screen states represented in the Gemini AI KMP App screenshot suite.
 */
enum class AdaptiveScreenState {
    WELCOME,
    ADD_API_KEY_DIALOG,
    NEW_GROUP_DIALOG,
    CONVERSATIONS_LIST,
    EMPTY_CHAT,
    CHAT_LOADING,
    CHAT_RESPONSE,
    MULTIMODAL_PROMPT,
    MULTIMODAL_VISION,
    DELETE_CHAT_DIALOG,
    SETTINGS,
    EDIT_API_KEY_DIALOG,
    DELETE_ALL_CHATS_DIALOG
}

/**
 * Target device platform platforms supported by the unified adaptive renderer.
 */
enum class TargetPlatform {
    ANDROID,
    IOS,
    MACOS,
    WEB
}

internal val mobileScreenStates = listOf(
    "01_welcome_screen.png" to AdaptiveScreenState.WELCOME,
    "02_add_api_key_dialog.png" to AdaptiveScreenState.ADD_API_KEY_DIALOG,
    "03_new_group_dialog.png" to AdaptiveScreenState.NEW_GROUP_DIALOG,
    "04_conversations_list.png" to AdaptiveScreenState.CONVERSATIONS_LIST,
    "05_empty_chat_screen.png" to AdaptiveScreenState.EMPTY_CHAT,
    "06_question_sending_loading.png" to AdaptiveScreenState.CHAT_LOADING,
    "07_gemini_markdown_response.png" to AdaptiveScreenState.CHAT_RESPONSE,
    "08_multimodal_image_prompt.png" to AdaptiveScreenState.MULTIMODAL_PROMPT,
    "09_gemini_vision_response.png" to AdaptiveScreenState.MULTIMODAL_VISION,
    "10_delete_chat_dialog.png" to AdaptiveScreenState.DELETE_CHAT_DIALOG,
    "11_settings_screen.png" to AdaptiveScreenState.SETTINGS,
    "12_edit_api_key_dialog.png" to AdaptiveScreenState.EDIT_API_KEY_DIALOG,
    "13_delete_all_chats_dialog.png" to AdaptiveScreenState.DELETE_ALL_CHATS_DIALOG
)

internal val desktopScreenStates = listOf(
    "01_two_pane_welcome.png" to AdaptiveScreenState.WELCOME,
    "02_two_pane_add_api_key_dialog.png" to AdaptiveScreenState.ADD_API_KEY_DIALOG,
    "03_two_pane_new_group_dialog.png" to AdaptiveScreenState.NEW_GROUP_DIALOG,
    "04_two_pane_chat_loading.png" to AdaptiveScreenState.CHAT_LOADING,
    "05_two_pane_chat_response.png" to AdaptiveScreenState.CHAT_RESPONSE,
    "06_two_pane_multimodal_vision.png" to AdaptiveScreenState.MULTIMODAL_VISION,
    "07_two_pane_delete_chat_dialog.png" to AdaptiveScreenState.DELETE_CHAT_DIALOG,
    "08_two_pane_settings_screen.png" to AdaptiveScreenState.SETTINGS,
    "09_two_pane_edit_api_key_dialog.png" to AdaptiveScreenState.EDIT_API_KEY_DIALOG,
    "10_two_pane_delete_all_chats_dialog.png" to AdaptiveScreenState.DELETE_ALL_CHATS_DIALOG
)

internal val webScreenStates = listOf(
    "01_web_welcome.png" to AdaptiveScreenState.WELCOME,
    "02_web_add_api_key_dialog.png" to AdaptiveScreenState.ADD_API_KEY_DIALOG,
    "03_web_new_group_dialog.png" to AdaptiveScreenState.NEW_GROUP_DIALOG,
    "04_web_chat_loading.png" to AdaptiveScreenState.CHAT_LOADING,
    "05_web_chat_response.png" to AdaptiveScreenState.CHAT_RESPONSE,
    "06_web_multimodal_vision.png" to AdaptiveScreenState.MULTIMODAL_VISION,
    "07_web_delete_chat_dialog.png" to AdaptiveScreenState.DELETE_CHAT_DIALOG,
    "08_web_settings_screen.png" to AdaptiveScreenState.SETTINGS,
    "09_web_edit_api_key_dialog.png" to AdaptiveScreenState.EDIT_API_KEY_DIALOG,
    "10_web_delete_all_chats_dialog.png" to AdaptiveScreenState.DELETE_ALL_CHATS_DIALOG
)

/**
 * Unified adaptive container that renders the given [state] automatically adapting to the
 * screen width:
 * - On compact devices (`maxWidth < 840.dp`, e.g. Pixel 9, iPhone 16 Pro): renders single-pane layout.
 * - On expanded screens (`maxWidth >= 840.dp`, e.g. Desktop window, Web browser): renders two-pane layout.
 */
@Composable
fun AdaptiveAppContent(
    state: AdaptiveScreenState,
    sampleImageBytes: ByteArray = ByteArray(0)
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTwoPane = maxWidth >= 840.dp

        if (isTwoPane) {
            when (state) {
                AdaptiveScreenState.WELCOME -> {
                    TwoPaneLayout(
                        groupUiState = GroupUiState(groupList = sampleGroups),
                        selectedGroupId = null
                    ) {
                        WelcomeScreen()
                    }
                }
                AdaptiveScreenState.ADD_API_KEY_DIALOG -> {
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
                AdaptiveScreenState.NEW_GROUP_DIALOG -> {
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
                AdaptiveScreenState.CONVERSATIONS_LIST,
                AdaptiveScreenState.EMPTY_CHAT -> {
                    TwoPaneLayout(
                        groupUiState = GroupUiState(groupList = sampleGroups),
                        selectedGroupId = "1"
                    ) {
                        EmptyChatContent()
                    }
                }
                AdaptiveScreenState.CHAT_LOADING -> {
                    TwoPaneLayout(
                        groupUiState = GroupUiState(groupList = sampleGroups),
                        selectedGroupId = "1"
                    ) {
                        ChatLoadingContent()
                    }
                }
                AdaptiveScreenState.CHAT_RESPONSE -> {
                    TwoPaneLayout(
                        groupUiState = GroupUiState(groupList = sampleGroups),
                        selectedGroupId = "1"
                    ) {
                        ChatResponseContent()
                    }
                }
                AdaptiveScreenState.MULTIMODAL_PROMPT,
                AdaptiveScreenState.MULTIMODAL_VISION -> {
                    TwoPaneLayout(
                        groupUiState = GroupUiState(groupList = sampleGroups),
                        selectedGroupId = "1"
                    ) {
                        MultimodalResponseContent(sampleImageBytes)
                    }
                }
                AdaptiveScreenState.DELETE_CHAT_DIALOG -> {
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
                AdaptiveScreenState.SETTINGS -> {
                    TwoPaneLayout(
                        groupUiState = GroupUiState(groupList = sampleGroups),
                        selectedGroupId = "1"
                    ) {
                        SettingsScreenCommonContent()
                    }
                }
                AdaptiveScreenState.EDIT_API_KEY_DIALOG -> {
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
                AdaptiveScreenState.DELETE_ALL_CHATS_DIALOG -> {
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
        } else {
            when (state) {
                AdaptiveScreenState.WELCOME -> EmptyGroupListContent()
                AdaptiveScreenState.ADD_API_KEY_DIALOG -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        EmptyGroupListContent()
                        ApiKeyAlertDialog(
                            show = true,
                            onDismiss = {},
                            onSave = {}
                        )
                    }
                }
                AdaptiveScreenState.NEW_GROUP_DIALOG -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        EmptyGroupListContent()
                        NewChatAlertDialog(
                            show = true,
                            onDismiss = {},
                            onSave = {}
                        )
                    }
                }
                AdaptiveScreenState.CONVERSATIONS_LIST -> ConversationListContent()
                AdaptiveScreenState.EMPTY_CHAT -> EmptyChatContent()
                AdaptiveScreenState.CHAT_LOADING -> ChatLoadingContent()
                AdaptiveScreenState.CHAT_RESPONSE -> ChatResponseContent()
                AdaptiveScreenState.MULTIMODAL_PROMPT -> MultimodalPromptContent(sampleImageBytes)
                AdaptiveScreenState.MULTIMODAL_VISION -> MultimodalResponseContent(sampleImageBytes)
                AdaptiveScreenState.DELETE_CHAT_DIALOG -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ChatResponseContent()
                        DeleteChatAlertDialog(
                            show = true,
                            onDismiss = {},
                            onConfirmDelete = {}
                        )
                    }
                }
                AdaptiveScreenState.SETTINGS -> SettingsScreenCommonContent()
                AdaptiveScreenState.EDIT_API_KEY_DIALOG -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SettingsScreenCommonContent()
                        EditApiKeyDialog(
                            show = true,
                            initialKey = "AIzaSyD-sample-api-key-gemini",
                            onDismiss = {},
                            onSaveKey = {}
                        )
                    }
                }
                AdaptiveScreenState.DELETE_ALL_CHATS_DIALOG -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SettingsScreenCommonContent()
                        DeleteAllChatsDialog(
                            show = true,
                            onDismiss = {},
                            onConfirmDeleteAll = {}
                        )
                    }
                }
            }
        }
    }
}

/**
 * Universal adaptive renderer that dispatches to the correct hardware bezel or browser frame
 * for the requested [platform].
 */
fun renderAdaptiveScreen(
    platform: TargetPlatform,
    outputFile: File,
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    when (platform) {
        TargetPlatform.ANDROID -> renderPhoneScreen(
            preset = DevicePresets.pixel9,
            outputFile = outputFile,
            isDark = isDark,
            content = content
        )
        TargetPlatform.IOS -> renderPhoneScreen(
            preset = DevicePresets.iPhone16Pro,
            outputFile = outputFile,
            isDark = isDark,
            content = content
        )
        TargetPlatform.MACOS -> renderDesktopScreen(
            preset = DevicePresets.largeDesktopWindow,
            outputFiles = listOf(outputFile),
            isDark = isDark,
            content = content
        )
        TargetPlatform.WEB -> renderWebBrowserScreen(
            outputFile = outputFile,
            isDark = isDark,
            content = content
        )
    }
}

/**
 * Exports all canonical screens for the specified [platform] in the given theme.
 */
fun exportPlatformScreens(
    platform: TargetPlatform,
    outputDir: File,
    sampleImageBytes: ByteArray,
    isDark: Boolean = true
) {
    val screens = when (platform) {
        TargetPlatform.ANDROID, TargetPlatform.IOS -> mobileScreenStates
        TargetPlatform.MACOS -> desktopScreenStates
        TargetPlatform.WEB -> webScreenStates
    }
    screens.forEach { (fileName, screenState) ->
        renderAdaptiveScreen(
            platform = platform,
            outputFile = File(outputDir, fileName),
            isDark = isDark
        ) {
            AdaptiveAppContent(
                state = screenState,
                sampleImageBytes = sampleImageBytes
            )
        }
    }
}
