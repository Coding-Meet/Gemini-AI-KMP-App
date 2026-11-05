package com.coding.meet.gaminiaikmp.screenshot

import com.coding.meet.gaminiaikmp.domain.model.Group

internal val sampleGroups = listOf(
    Group("1", "Kmp", "12-Feb-2024 3:23 PM", "robot_1.png"),
    Group("2", "Android Jetpack Compose", "12-Feb-2024 2:10 PM", "robot_2.png"),
    Group("3", "Gemini 2.5 Flash Assistant", "11-Feb-2024 6:45 PM", "robot_3.png"),
    Group("4", "Multiplatform Architecture", "10-Feb-2024 11:20 AM", "robot_4.png"),
    Group("5", "Kotlin Coroutines & Flow", "09-Feb-2024 4:15 PM", "robot_5.png")
)

internal val geminiResponseText = """
**Kotlin Multiplatform (KMP)** with Compose allows sharing UI across **Android, iOS, Desktop, and Web**.

* **Single Codebase:** 100% shared UI in `commonMain`
* **Native Performance:** Compiles directly to native code
* **Jetpack Compose:** Modern declarative UI components
""".trimIndent()

internal val geminiVisionText = """
**GEMINI AI KMP APP**

This image showcases the **Gemini AI KMP App** running across **Android, iOS, macOS, and Web** with shared Compose UI.
""".trimIndent()

