package presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.model.Group
import gemini_ai_kmp_app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import theme.borderColor
import theme.selectedBGColor
import theme.whiteColor
import utils.capitalizeFirstLetter

@Composable
fun GroupLayout(group: Group, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        onClick = {
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) selectedBGColor else borderColor,
            contentColor = whiteColor
        )
    ) {
        GroupRowLayout(group, Modifier.padding(10.dp))
    }
}

@Composable
fun GroupRowLayout(group: Group, customModifier: Modifier = Modifier) {
    val groupIcon = when (group.icon) {
        "robot_1.png" -> {
            Res.drawable.robot_1
        }

        "robot_2.png" -> {
            Res.drawable.robot_2
        }

        "robot_3.png" -> {
            Res.drawable.robot_3
        }

        "robot_4.png" -> {
            Res.drawable.robot_4
        }

        "robot_5.png" -> {
            Res.drawable.robot_5
        }

        else -> {
            Res.drawable.robot_6
        }
    }
    Row(
        Modifier
            .fillMaxWidth()
            .then(customModifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(groupIcon), null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(0.8f)
        ) {
            Text(
                text = group.groupName.capitalizeFirstLetter(),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
            Text(
                text = group.date,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp))
        }
    }
}