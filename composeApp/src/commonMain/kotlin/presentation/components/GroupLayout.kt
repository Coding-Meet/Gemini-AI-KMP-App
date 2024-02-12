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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.borderColor
import theme.selectedBGColor
import theme.whiteColor
import utils.capitalizeFirstLetter

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GroupRowLayout(group: Group, customModifier: Modifier = Modifier) {
    Row(
        Modifier
            .fillMaxWidth()
            .then(customModifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(group.icon), null,
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