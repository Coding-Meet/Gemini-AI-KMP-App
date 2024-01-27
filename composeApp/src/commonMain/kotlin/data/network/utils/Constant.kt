package data.network.utils


object Constant {
    private const val BASE_URL = "https://generativelanguage.googleapis.com"

    private val NGEMINI_API_KEY = "AIzaSyBZ4r4qf_-G9lE1K3N7pHop499KkfdDEbM"

    val GEMINI_PRO =
        "$BASE_URL/v1beta/models/gemini-pro:generateContent?key=${NGEMINI_API_KEY}"

    val GEMINI_PRO_VISION =
        "$BASE_URL/v1beta/models/gemini-pro-vision:generateContent?key=${NGEMINI_API_KEY}"
}