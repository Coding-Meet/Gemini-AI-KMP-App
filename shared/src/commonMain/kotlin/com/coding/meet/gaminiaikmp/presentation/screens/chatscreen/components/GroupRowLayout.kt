package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews
import com.coding.meet.gaminiaikmp.utils.capitalizeFirstLetter
import gemini_ai_kmp_app.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun GroupRowLayout(group: Group, modifier: Modifier = Modifier) {
    val groupIcon = when (group.icon) {
        "robot_1.png" -> Res.drawable.robot_1
        "robot_2.png" -> Res.drawable.robot_2
        "robot_3.png" -> Res.drawable.robot_3
        "robot_4.png" -> Res.drawable.robot_4
        "robot_5.png" -> Res.drawable.robot_5
        "robot_6.png" -> Res.drawable.robot_6
        "robot_7.png" -> Res.drawable.robot_7
        else -> Res.drawable.robot_8
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.size(46.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(groupIcon),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = group.groupName.capitalizeFirstLetter(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = group.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@ComponentPreviews
@Composable
fun GroupRowLayoutPreview() {
    GroupRowLayout(
        group = Group("1", "Gemini Assistant", "03 Nov, 11:30 AM", "robot_2.png")
    )
}
