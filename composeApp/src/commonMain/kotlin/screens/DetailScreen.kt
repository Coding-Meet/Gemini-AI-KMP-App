package screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.*
import models.Robot
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skia.Bitmap
import theme.*
import utils.TYPE
import utils.capitalizeFirstLetter
import utils.generateRandomKey
import viewmodels.MainViewModel
import kotlin.random.Random


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(viewModel: MainViewModel) {
    Column(
        Modifier.fillMaxHeight().background(lightBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.robotList.isNotEmpty() && viewModel.currentPos != -1) {
            UserBarLayout(viewModel, viewModel.robotList[viewModel.currentPos]) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(viewModel.allPlatform != TYPE.MOBILE) {
                        IconButton(
                            onClick = {
                                viewModel.isDesktopDrawerOpen = !viewModel.isDesktopDrawerOpen
                            },
                            modifier = Modifier.size(30.dp, 80.dp).background(borderColor, RoundedCornerShape(10))
                                .clip(RoundedCornerShape(10))
                        ) {
                            Icon(
                                imageVector =
                                if (viewModel.isDesktopDrawerOpen) {
                                    Icons.Default.KeyboardArrowRight
                                } else {
                                    Icons.Default.KeyboardArrowLeft
                                },
                                tint = whiteColor,
                                contentDescription = "drawer",
                            )
                        }
                    }
                    val lazyListState = rememberLazyListState()
                    LazyColumn(
                        Modifier.fillMaxSize().background(lightBackgroundColor),
                        lazyListState,
                        reverseLayout = true,
                        contentPadding = PaddingValues(horizontal = 10.dp),
                    ) {
                        items(10) {
                            MessageItem(
                                viewModel,
                                isInComing = Random.nextBoolean(),
                                images = emptyList(),
                                content = generateRandomKey(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement()
                            )
                        }
                    }
                }
            }
        } else {
            Column(
            ) {
                Text("Welcome To Gemini Ai Kmp App", color = whiteColor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun UserBarLayout(
    viewModel: MainViewModel,
    robot: Robot,
    content: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


    Column(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = borderColor,
                titleContentColor = whiteColor,
            ),
            title = {
                UserRow(robot)
            },
            actions = {
                IconButton(onClick = {
                    viewModel.robotList.remove(robot)
                    viewModel.currentPos = -1
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "delete",
                        tint = Color.Red
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
        Row(
            modifier = Modifier.weight(1f)
        ) {
            content()
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth().background(borderColor).padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            items(viewModel.imageUris) { imageUri ->
                Box(
                    modifier = Modifier.padding(4.dp).background(lightBorderColor, RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = painterResource(imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .requiredSize(100.dp)
                    )

                    Icon(
                        Icons.Default.Close,
                        tint = whiteColor,
                        contentDescription = "remove",
                        modifier = Modifier.padding(end = 8.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .align(Alignment.TopEnd)
                            .clickable {
                                viewModel.imageUris.remove(imageUri)
                            }
                    )
                }
            }
        }
        BottomTextBar(viewModel)
    }
}

@Composable
fun BottomTextBar(viewModel: MainViewModel) {
    Row(
        modifier = Modifier.background(borderColor).padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            if (viewModel.imageUris.size != 3) {
                viewModel.imageUris.add(
                    "robot_${(1..8).random()}.png",
                )
            }

        }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "upload",
                tint = whiteColor
            )
        }
        TextField(
            value = viewModel.userText,
            onValueChange = { viewModel.userText = it },
            modifier = Modifier.weight(1f)
                .background(borderColor),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            maxLines = 3,
            placeholder = {
                Text("Type a message", color = textHintColor)
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = lightBorderColor,
                focusedContainerColor = lightBorderColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = whiteColor,
                unfocusedTextColor = whiteColor,
                focusedPlaceholderColor = whiteColor,
                unfocusedPlaceholderColor = whiteColor,
                unfocusedLabelColor = whiteColor,
                focusedLabelColor = whiteColor,
                cursorColor = whiteColor,
                selectionColors = TextSelectionColors(selectionColor, selectionColor)
            ),
            shape = RoundedCornerShape(10.dp)
        )
        FloatingActionButton(
            containerColor = lightBorderColor,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = if (viewModel.isGenerating) 0.dp else 6.dp,
                pressedElevation = 0.dp
            ),
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = {
                if (viewModel.isGenerating) {

                }
//                viewModel.isGenerating = true
//                if (promptText.isNotBlank() && isGenerating.not()) {
//                    mainViewModel.sendText(promptText, imageBitmaps)
//                    promptText = ""
//                    imageBitmaps.clear()
//                    keyboardController?.hide()
//                } else if (promptText.isBlank()) {
//                    Toast.makeText(
//                        context,
//                        "Please enter a message",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
            }
        ) {
            AnimatedContent(
                targetState = viewModel.isGenerating,
            ) { generating ->
                if (generating) {
                    CircularProgressIndicator(
                        trackColor = whiteColor,
                        color = lightBorderColor,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "send",
                        tint = whiteColor
                    )
                }
            }
        }

    }
}

@Composable
fun MessageItem(
    viewModel: MainViewModel,
    isInComing: Boolean,
    images: List<Bitmap>,
    content: String,
    modifier: Modifier = Modifier
) {

    val cardShape by remember {
        derivedStateOf {
            if (isInComing) {
                RoundedCornerShape(
                    16.dp,
                    16.dp,
                    16.dp,
                    0.dp
                )
            } else {
                RoundedCornerShape(
                    16.dp,
                    16.dp,
                    0.dp,
                    16.dp
                )
            }
        }
    }

    val cardPadding by remember {
        derivedStateOf {
            if (isInComing) {
                PaddingValues(end = 24.dp)
            } else {
                PaddingValues(start = 24.dp)
            }
        }
    }

    Column(modifier = modifier.padding(vertical = 10.dp)) {
        if (images.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                reverseLayout = true,
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End)
            ) {
                items(images.size) { index ->
//                    val image = images[index]
//                    Image(
//                        bitmap = image,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .height(60.dp)
//                            .border(
//                                width = 2.dp,
//                                color = MaterialTheme.colorScheme.surfaceVariant,
//                            )
//                    )
                }
            }
        }
        Row(
            horizontalArrangement = if (isInComing) Arrangement.Start else Arrangement.End,
            verticalAlignment = Alignment.Top,
            modifier = modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(cardPadding),
                shape = cardShape,
                colors = CardDefaults.cardColors(
                    containerColor = borderColor
                )
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    SelectionContainer {
                        Text(
                            text = content,
                            color = whiteColor,
                            style = MaterialTheme.typography.bodyMedium
                        )
//                        if (viewModel.allPlatform != TYPE.WEB){
//                            com.mikepenz.markdown.compose.Markdown(content)
//                        }
                    }
                }
            }
        }
    }
}