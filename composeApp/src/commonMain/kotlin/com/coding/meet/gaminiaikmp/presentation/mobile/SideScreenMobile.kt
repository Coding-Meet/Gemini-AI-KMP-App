package com.coding.meet.gaminiaikmp.presentation.mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.coding.meet.gaminiaikmp.presentation.commoninterface.DistributeComp
import com.coding.meet.gaminiaikmp.theme.lightBackgroundColor


class SideScreenMobile() : DistributeComp {
    @Composable
    override fun ContentComposable(content: @Composable () -> Unit) {
        Row(
            modifier = Modifier.fillMaxSize().background(lightBackgroundColor)
        ) {
            content()
        }
    }

}