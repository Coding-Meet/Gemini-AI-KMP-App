package com.coding.meet.gaminiaikmp.screenshot

import java.io.File

private val webScreens = listOf(
    "01_web_welcome.png" to TwoPaneScreenState.WELCOME,
    "02_web_add_api_key_dialog.png" to TwoPaneScreenState.ADD_API_KEY_DIALOG,
    "03_web_new_group_dialog.png" to TwoPaneScreenState.NEW_GROUP_DIALOG,
    "04_web_chat_loading.png" to TwoPaneScreenState.CHAT_LOADING,
    "05_web_chat_response.png" to TwoPaneScreenState.CHAT_RESPONSE,
    "06_web_multimodal_vision.png" to TwoPaneScreenState.MULTIMODAL_VISION,
    "07_web_delete_chat_dialog.png" to TwoPaneScreenState.DELETE_CHAT_DIALOG,
    "08_web_settings_screen.png" to TwoPaneScreenState.SETTINGS,
    "09_web_edit_api_key_dialog.png" to TwoPaneScreenState.EDIT_API_KEY_DIALOG,
    "10_web_delete_all_chats_dialog.png" to TwoPaneScreenState.DELETE_ALL_CHATS_DIALOG
)

internal fun exportWebStates(
    outputDir: File,
    sampleImageBytes: ByteArray,
    isDark: Boolean = true
) {
    webScreens.forEach { (fileName, screenState) ->
        renderWebBrowserScreen(
            outputFile = File(outputDir, fileName),
            isDark = isDark
        ) {
            CommonTwoPaneScreen(
                state = screenState,
                sampleImageBytes = sampleImageBytes
            )
        }
    }
}
