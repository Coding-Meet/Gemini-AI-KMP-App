package com.coding.meet.gaminiaikmp.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun AppVerticalScrollbar(
    scrollState: ScrollState,
    modifier: Modifier
) {
    // iOS uses native system touch overscroll and scroll indicators
}

@Composable
actual fun AppVerticalScrollbar(
    lazyListState: LazyListState,
    modifier: Modifier,
    reverseLayout: Boolean
) {
    // iOS uses native system touch overscroll and scroll indicators
}

