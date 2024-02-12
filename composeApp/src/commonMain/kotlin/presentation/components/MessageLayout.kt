package presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import di.TextComposable
import di.setClipData
import di.toComposeImageBitmap
import domain.model.ChatMessage
import domain.model.Role
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.borderColor
import theme.lightBorderColor
import theme.redColor
import theme.whiteColor


@OptIn(ExperimentalResourceApi::class)
@Composable
fun MessageItem(chatMessage: ChatMessage) {
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val isGEMINIMessage = chatMessage.participant != Role.YOU

    val backgroundColor = when (chatMessage.participant) {
        Role.GEMINI -> borderColor
        Role.YOU -> borderColor
        Role.ERROR -> redColor
    }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = EaseOutSine
            )
        ), label = ""
    )
    val horizontalAlignment = if (isGEMINIMessage) {
        Alignment.Start
    } else {
        Alignment.End
    }
    val circleColors = listOf(
        Color(0xFF5851D8),
        Color(0xFF833AB4),
        Color(0xFFC13584),
        Color(0xFFE1306C),
        Color(0xFFFD1D1D),
        Color(0xFFF56040),
        Color(0xFFF77737),
        Color(0xFFFCAF45),
        Color(0xFFFFDC80),
        Color(0xFF5851D8)
    )
    val cardShape = if (isGEMINIMessage) {
        RoundedCornerShape(
            16.dp, 16.dp, 16.dp, 0.dp
        )
    } else {
        RoundedCornerShape(
            16.dp, 16.dp, 0.dp, 16.dp
        )
    }

    val cardPadding = if (isGEMINIMessage) {
        PaddingValues(end = 24.dp)
    } else {
        PaddingValues(start = 24.dp)
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.wrapContentSize().padding(cardPadding)
                .clip(cardShape)
                .padding(2.dp)
                .drawWithContent {
                    rotate(
                        if (chatMessage.isPending) {
                            rotate
                        } else {
                            0f
                        }
                    ) {
                        drawCircle(
                            brush = if (chatMessage.isPending && chatMessage.participant == Role.YOU) {
                                Brush.sweepGradient(
                                    circleColors
                                )
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        backgroundColor,
                                        backgroundColor
                                    )
                                )
                            },
                            radius = size.width,
                            blendMode = BlendMode.SrcIn,
                        )
                    }
                    drawContent()
                }.background(backgroundColor, cardShape)
        ) {
            Column(
                modifier = Modifier.wrapContentSize()
            ) {
                if (chatMessage.images.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.wrapContentSize().padding(bottom = 4.dp),
                        reverseLayout = true,
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End)
                    ) {
                        items(chatMessage.images) {
                            val bitmap = it.toComposeImageBitmap()
                            Image(
                                bitmap,
                                contentDescription = null,
                                modifier = Modifier.height(192.dp).clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.FillHeight,
                            )
                        }
                    }
                }

                TextComposable(chatMessage.text, isGEMINIMessage)
                if (isGEMINIMessage) {
                    Box(
                        Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            if (chatMessage.participant == Role.GEMINI) "by Gemini" else "by Error",
                            color = whiteColor,
                            fontSize = 18.sp,
                            modifier = Modifier.align(Alignment.CenterStart).padding(start = 10.dp),
                            fontWeight = FontWeight.Bold)
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = lightBorderColor
                            ),
                            onClick = {
                                coroutineScope.launch {
                                    setClipData(clipboardManager, chatMessage.text)
                                }
                            }) {
                            Icon(
                                painterResource("ic_paste.xml"),
                                contentDescription = "copy",
                                tint = whiteColor
                            )
                        }
                    }
                }

            }
        }
    }

}
