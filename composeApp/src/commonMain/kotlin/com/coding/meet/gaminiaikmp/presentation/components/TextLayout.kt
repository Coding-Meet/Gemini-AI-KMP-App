package com.coding.meet.gaminiaikmp.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.coding.meet.gaminiaikmp.theme.lightBackgroundColor
import com.coding.meet.gaminiaikmp.theme.whiteColor
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography

@Composable
fun CommonTextComposable(message: String, isGEMINIMessage: Boolean) {
    SelectionContainer {
//        Text(
//            text = message,
//            modifier = Modifier.padding(
//                start = 10.dp,
//                top = 10.dp,
//                end = 10.dp,
//                bottom = if (isGEMINIMessage) 3.dp else 10.dp,
//            ),
//            style = TextStyle(color = whiteColor)
//        )
        Markdown(
            content = message,
            modifier = Modifier.padding(
                start = 10.dp,
                top = 10.dp,
                end = 10.dp,
                bottom = if (isGEMINIMessage) 3.dp else 10.dp,
            ),

            colors = DefaultMarkdownColors(
                text = whiteColor,
                codeText = whiteColor,
                inlineCodeText = whiteColor,
                linkText = Color.Blue,
                codeBackground = lightBackgroundColor,
                inlineCodeBackground = lightBackgroundColor,
                dividerColor = Color.Gray
            ),
            typography = DefaultMarkdownTypography(
                MaterialTheme.typography.headlineLarge,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
                MaterialTheme.typography.bodyMedium,
            ),
        )
    }
}