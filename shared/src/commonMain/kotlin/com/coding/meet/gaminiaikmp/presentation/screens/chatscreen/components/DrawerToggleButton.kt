package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews

/**
 * Collapsible toggle handle button positioned on the continuous pane divider.
 * Clicking toggles the drawer / sidebar between collapsed and expanded states.
 */
@Composable
fun DrawerToggleButton(
    isDesktopDrawerOpen: Boolean,
    onToggleDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onToggleDrawer,
        shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        shadowElevation = 0.dp,
        modifier = modifier.size(24.dp, 56.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (isDesktopDrawerOpen) {
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft
                } else {
                    Icons.AutoMirrored.Filled.KeyboardArrowRight
                },
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp),
                contentDescription = if (isDesktopDrawerOpen) "Collapse sidebar" else "Expand sidebar"
            )
        }
    }
}

@ComponentPreviews
@Composable
fun DrawerToggleButtonCollapsedPreview() {
    DrawerToggleButton(
        isDesktopDrawerOpen = false,
        onToggleDrawer = {}
    )
}

@ComponentPreviews
@Composable
fun DrawerToggleButtonExpandedPreview() {
    DrawerToggleButton(
        isDesktopDrawerOpen = true,
        onToggleDrawer = {}
    )
}
