package com.coding.meet.gaminiaikmp.screenshot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.apadmi.cmpdevicepreview.core.DevicePreset
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.DeleteChatAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.ApiKeyAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.NewChatAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.DeleteAllChatsDialog
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.EditApiKeyDialog
import java.io.File

internal fun exportMobileStates(
    preset: DevicePreset,
    outputDir: File,
    sampleImageBytes: ByteArray,
    isDark: Boolean = true
) {
    // 01. Welcome Screen
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "01_welcome_screen.png"),
        isDark = isDark
    ) {
        EmptyGroupListContent()
    }

    // 02. Add API Key Dialog
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "02_add_api_key_dialog.png"),
        isDark = isDark
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            EmptyGroupListContent()
            ApiKeyAlertDialog(
                show = true,
                onDismiss = {},
                onSave = {}
            )
        }
    }

    // 03. New Group Dialog
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "03_new_group_dialog.png"),
        isDark = isDark
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            EmptyGroupListContent()
            NewChatAlertDialog(
                show = true,
                onDismiss = {},
                onSave = {}
            )
        }
    }

    // 04. Conversations List
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "04_conversations_list.png"),
        isDark = isDark
    ) {
        ConversationListContent()
    }

    // 05. Empty Chat Screen
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "05_empty_chat_screen.png"),
        isDark = isDark
    ) {
        EmptyChatContent()
    }

    // 06. Question Sending / Loading State
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "06_question_sending_loading.png"),
        isDark = isDark
    ) {
        ChatLoadingContent()
    }

    // 07. Gemini Markdown Response (Both Question & Answer visible on screen)
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "07_gemini_markdown_response.png"),
        isDark = isDark
    ) {
        ChatResponseContent()
    }

    // 08. Multimodal Image Prompt
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "08_multimodal_image_prompt.png"),
        isDark = isDark
    ) {
        MultimodalPromptContent(sampleImageBytes = sampleImageBytes)
    }

    // 09. Gemini Vision Response (Both Prompt & Vision Answer visible on screen)
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "09_gemini_vision_response.png"),
        isDark = isDark
    ) {
        MultimodalResponseContent(sampleImageBytes = sampleImageBytes)
    }

    // 10. Delete Chat Dialog
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "10_delete_chat_dialog.png"),
        isDark = isDark
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ChatResponseContent()
            DeleteChatAlertDialog(
                show = true,
                onDismiss = {},
                onConfirmDelete = {}
            )
        }
    }

    // 11. Settings Screen
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "11_settings_screen.png"),
        isDark = isDark
    ) {
        SettingsScreenCommonContent()
    }

    // 12. Edit API Key Dialog
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "12_edit_api_key_dialog.png"),
        isDark = isDark
    ) {
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

    // 13. Delete All Chats Dialog
    renderPhoneScreen(
        preset = preset,
        outputFile = File(outputDir, "13_delete_all_chats_dialog.png"),
        isDark = isDark
    ) {
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
