package com.coding.meet.gaminiaikmp.presentation.desktopweb

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.meet.gaminiaikmp.presentation.commoninterface.DistributeComp
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatViewModel
import com.coding.meet.gaminiaikmp.presentation.screens.detailscreen.DetailScreen
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel
import com.coding.meet.gaminiaikmp.theme.borderColor
import com.coding.meet.gaminiaikmp.theme.lightBackgroundColor


class SideScreenDesktop(private val mainViewModel: MainViewModel, private val chatViewModel: ChatViewModel) :
    DistributeComp {
    @Composable
    override fun ContentComposable(content: @Composable () -> Unit) {
        Row(
            modifier = Modifier.fillMaxSize().background(lightBackgroundColor)
                .padding(20.dp)
        ) {
            AnimatedVisibility(mainViewModel.isDesktopDrawerOpen,
                Modifier.fillMaxHeight().weight(0.25f),
            ) {
                Column(
                    Modifier.background(lightBackgroundColor).border(2.dp, borderColor),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
            val customModifier = if(mainViewModel.isDesktopDrawerOpen){
                Modifier.fillMaxHeight().weight(0.75f)
            }else{
                Modifier.fillMaxSize()
            }
            Column(
                customModifier.padding(start = 2.dp).background(lightBackgroundColor)
                    .border(2.dp, borderColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreen(mainViewModel, chatViewModel = chatViewModel)
            }
        }
    }

}
