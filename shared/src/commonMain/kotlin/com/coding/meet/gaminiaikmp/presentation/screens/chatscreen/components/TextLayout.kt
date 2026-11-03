package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews
import com.mikepenz.markdown.m3.Markdown

@Composable
fun CommonTextComposable(message: String, isGEMINIMessage: Boolean = true, modifier: Modifier = Modifier) {
    SelectionContainer {
        Markdown(
            content = message,
            modifier = modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
    }
}

@ComponentPreviews
@Composable
fun CommonTextComposablePreview() {
    CommonTextComposable(
        message = "## Hello World\nThis is a **native Material 3 markdown** preview.",
        isGEMINIMessage = true
    )
}
