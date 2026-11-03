package com.coding.meet.gaminiaikmp.domain.model

data class GeminiModelInfo(
    val id: String,
    val name: String,
    val description: String,
    val badge: String? = null,
    val inputTokenLimit: Int? = null,
    val outputTokenLimit: Int? = null,
    val isThinking: Boolean = false
)

/**
 * Official default list of models based on current Google Gemini API documentation.
 * Flagship models: Gemini 3.8 Flash (Recommended) and Gemini 4 Argon.
 */
val DEFAULT_GEMINI_MODELS = listOf(
    GeminiModelInfo(
        id = "gemini-3.8-flash",
        name = "Gemini 3.8 Flash",
        description = "Our most intelligent Flash model, engineered for long-horizon software engineering, agents, and complex workflows.",
        badge = "Recommended",
        inputTokenLimit = 1048576,
        outputTokenLimit = 65536,
        isThinking = true
    ),
    GeminiModelInfo(
        id = "gemini-4-argon",
        name = "Gemini 4 Argon",
        description = "Flagship reasoning model with a shared 1M context window and long decodes up to 1M tokens.",
        badge = "Gemini 4",
        inputTokenLimit = 1048576,
        outputTokenLimit = 1048576,
        isThinking = true
    ),
    GeminiModelInfo(
        id = "gemini-3.5-flash-lite",
        name = "Gemini 3.5 Flash-Lite",
        description = "Our fastest, most cost-effective 3.5 model for high-throughput execution.",
        badge = "Fast & Lite",
        inputTokenLimit = 1048576,
        outputTokenLimit = 65536,
        isThinking = true
    ),
    GeminiModelInfo(
        id = "gemini-3.6-flash",
        name = "Gemini 3.6 Flash",
        description = "Previous-generation Flash model, balancing speed and multimodal capabilities across everyday tasks.",
        badge = "Stable",
        inputTokenLimit = 1048576,
        outputTokenLimit = 65536,
        isThinking = true
    ),
    GeminiModelInfo(
        id = "gemini-3.1-flash-lite",
        name = "Gemini 3.1 Flash-Lite",
        description = "Frontier-class performance rivaling larger models at a fraction of the cost.",
        badge = "Lite",
        inputTokenLimit = 1048576,
        outputTokenLimit = 65536,
        isThinking = true
    ),
    GeminiModelInfo(
        id = "gemini-flash-latest",
        name = "Gemini Flash Latest",
        description = "Latest automatically updated release of Gemini Flash.",
        badge = "Latest",
        inputTokenLimit = 1048576,
        outputTokenLimit = 65536,
        isThinking = true
    ),
    GeminiModelInfo(
        id = "gemini-2.5-flash-lite",
        name = "Gemini 2.5 Flash-Lite",
        description = "The fastest and most budget-friendly multimodal model in the 2.5 family.",
        badge = "2.5 Lite",
        inputTokenLimit = 1048576,
        outputTokenLimit = 65536,
        isThinking = true
    ),
    GeminiModelInfo(
        id = "gemma-4-31b-it",
        name = "Gemma 4 31B IT",
        description = "Open-weights instruction-tuned Gemma 4 model with reasoning.",
        badge = "Gemma",
        inputTokenLimit = 262144,
        outputTokenLimit = 32768,
        isThinking = true
    ),
    GeminiModelInfo(
        id = "gemini-2.5-flash",
        name = "Gemini 2.5 Flash",
        description = "Mid-size multimodal model (routes to gemini-3.5-flash-lite on Dec 1, 2026).",
        badge = "Legacy",
        inputTokenLimit = 1048576,
        outputTokenLimit = 65536,
        isThinking = true
    ),
    GeminiModelInfo(
        id = "gemini-2.5-pro",
        name = "Gemini 2.5 Pro",
        description = "Advanced reasoning model for complex tasks (routes to gemini-3.8-flash on Dec 1, 2026).",
        badge = "Legacy Pro",
        inputTokenLimit = 1048576,
        outputTokenLimit = 65536,
        isThinking = true
    )
)
