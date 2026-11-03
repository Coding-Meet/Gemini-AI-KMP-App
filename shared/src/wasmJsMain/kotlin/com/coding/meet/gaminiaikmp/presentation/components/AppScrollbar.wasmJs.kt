package com.coding.meet.gaminiaikmp.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun AppVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier,
        style = defaultScrollbarStyle().copy(
            unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
            hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            shape = RoundedCornerShape(4.dp),
            thickness = 8.dp
        )
    )
}

@Composable
actual fun AppVerticalScrollbar(
    lazyListState: LazyListState,
    modifier: Modifier,
    reverseLayout: Boolean
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(lazyListState),
        modifier = modifier,
        reverseLayout = reverseLayout,
        style = defaultScrollbarStyle().copy(
            unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
            hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            shape = RoundedCornerShape(4.dp),
            thickness = 8.dp
        )
    )
}

