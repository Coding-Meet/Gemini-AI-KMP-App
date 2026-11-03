package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.meet.gaminiaikmp.di.toComposeImageBitmap
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Role
import com.coding.meet.gaminiaikmp.presentation.preview.ComponentPreviews
import com.mikepenz.markdown.m3.Markdown
import gemini_ai_kmp_app.shared.generated.resources.Res
import gemini_ai_kmp_app.shared.generated.resources.ic_paste
import org.jetbrains.compose.resources.painterResource

@Composable
fun MessageItem(
    chatMessage: ChatMessage,
    rotate: Float = 0f,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val isGEMINIMessage = chatMessage.participant != Role.YOU

    val backgroundColor = when (chatMessage.participant) {
        Role.GEMINI -> MaterialTheme.colorScheme.surfaceContainerHigh
        Role.YOU -> MaterialTheme.colorScheme.primaryContainer
        Role.ERROR -> MaterialTheme.colorScheme.errorContainer
    }

    val contentColor = when (chatMessage.participant) {
        Role.GEMINI -> MaterialTheme.colorScheme.onSurface
        Role.YOU -> MaterialTheme.colorScheme.onPrimaryContainer
        Role.ERROR -> MaterialTheme.colorScheme.onErrorContainer
    }

    val horizontalAlignment = if (isGEMINIMessage) {
        Alignment.Start
    } else {
        Alignment.End
    }

    val auroraColors = listOf(
        Color(0xFF4285F4),
        Color(0xFF9B51E0),
        Color(0xFFEA4335),
        Color(0xFFFBBC05),
        Color(0xFF34A853),
        Color(0xFF4285F4)
    )

    val cardShape = if (isGEMINIMessage) {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    }

    val cardPadding = if (isGEMINIMessage) {
        PaddingValues(end = 36.dp)
    } else {
        PaddingValues(start = 36.dp)
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .padding(cardPadding)
                .clip(cardShape)
                .padding(if (chatMessage.isPending) 2.dp else 0.dp)
                .drawWithContent {
                    if (chatMessage.isPending) {
                        rotate(rotate) {
                            drawCircle(
                                brush = Brush.sweepGradient(auroraColors),
                                radius = size.width,
                                blendMode = BlendMode.SrcIn,
                            )
                        }
                    }
                    drawContent()
                },
            shape = cardShape,
            color = backgroundColor,
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.wrapContentSize().padding(4.dp)
            ) {
                if (chatMessage.images.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        reverseLayout = true,
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End)
                    ) {
                        items(chatMessage.images) { imageBytes ->
                            val bitmap = imageBytes.toComposeImageBitmap()
                            Image(
                                bitmap = bitmap,
                                contentDescription = null,
                                modifier = Modifier
                                    .heightIn(max = 200.dp)
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.FillHeight
                            )
                        }
                    }
                }

                if (isGEMINIMessage) {
                    SelectionContainer {
                        Markdown(
                            content = chatMessage.text,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = if (chatMessage.participant == Role.GEMINI) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.error
                                },
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (chatMessage.participant == Role.GEMINI) "Gemini" else "Error",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (chatMessage.participant == Role.GEMINI) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.error
                                }
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(32.dp),
                            onClick = {
                                clipboardManager.setText(AnnotatedString(chatMessage.text))
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_paste),
                                contentDescription = "Copy message",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    SelectionContainer {
                        Text(
                            text = chatMessage.text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = contentColor,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@ComponentPreviews
@Composable
fun MessageItemUserPreview() {
    MessageItem(
        chatMessage = ChatMessage(
            messageId = "1",
            groupId = "1",
            text = "Hello Gemini! How are you today?",
            participant = Role.YOU
        )
    )
}

@ComponentPreviews
@Composable
fun MessageItemGeminiPreview() {
    MessageItem(
        chatMessage = ChatMessage(
            messageId = "2",
            groupId = "1",
            text = "Hello! I am doing well. How can I assist you with your Kotlin Multiplatform project today?",
            participant = Role.GEMINI
        )
    )
}
