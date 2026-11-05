package com.coding.meet.gaminiaikmp.screenshot

import com.apadmi.cmpdevicepreview.core.DevicePreset
import java.io.File

private val desktopScreens = listOf(
    "01_two_pane_welcome.png" to TwoPaneScreenState.WELCOME,
    "02_two_pane_add_api_key_dialog.png" to TwoPaneScreenState.ADD_API_KEY_DIALOG,
    "03_two_pane_new_group_dialog.png" to TwoPaneScreenState.NEW_GROUP_DIALOG,
    "04_two_pane_chat_loading.png" to TwoPaneScreenState.CHAT_LOADING,
    "05_two_pane_chat_response.png" to TwoPaneScreenState.CHAT_RESPONSE,
    "06_two_pane_multimodal_vision.png" to TwoPaneScreenState.MULTIMODAL_VISION,
    "07_two_pane_delete_chat_dialog.png" to TwoPaneScreenState.DELETE_CHAT_DIALOG,
    "08_two_pane_settings_screen.png" to TwoPaneScreenState.SETTINGS,
    "09_two_pane_edit_api_key_dialog.png" to TwoPaneScreenState.EDIT_API_KEY_DIALOG,
    "10_two_pane_delete_all_chats_dialog.png" to TwoPaneScreenState.DELETE_ALL_CHATS_DIALOG
)

internal fun exportDesktopStates(
    preset: DevicePreset,
    outputDir: File,
    sampleImageBytes: ByteArray,
    isDark: Boolean = true
) {
    desktopScreens.forEach { (fileName, screenState) ->
        renderDesktopScreen(
            preset = preset,
            outputFiles = listOf(File(outputDir, fileName)),
            isDark = isDark
        ) {
            CommonTwoPaneScreen(
                state = screenState,
                sampleImageBytes = sampleImageBytes
            )
        }
    }
}
