package presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theme.whiteColor

@Composable
fun CommonTextComposable(message: String, isGEMINIMessage: Boolean) {

    SelectionContainer {
        Text(
            modifier = Modifier.padding(
                start = 10.dp,
                top = 10.dp,
                end = 10.dp,
                bottom = if (isGEMINIMessage) 3.dp else 10.dp,
            ),
            color = whiteColor,
            text = message,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}