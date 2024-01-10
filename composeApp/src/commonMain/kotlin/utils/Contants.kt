package utils

import screens.MenuItem


enum class TYPE{
    MOBILE,
    DESKTOP,
    WEB,
}
enum class Screens{
    MAIN,
    DETAIL,
    SETTING
}

val menuItems = listOf(
    MenuItem(
        "summarize",
        "Generate text from text-only input",
        "Sample app that summarizes text",
        "ic_text.xml",
    ),
    MenuItem(
        "photo_reasoning",
        "Generate text from text-and-image input (multimodal)",
        "Sample app for uploading images and asking about them",
        "ic_image_text.xml",
    ),
    MenuItem(
        "chat",
        "Build multi-turn conversations (chat)",
        "Sample app demonstrating a conversational UI",
        "ic_chat.xml",
    )
)