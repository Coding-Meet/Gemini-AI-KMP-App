package data.network

import data.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.*


class GeminiServiceImp(
    private val client: HttpClient
) : GeminiService {

    override suspend fun generateContent(content: String, apiKey: String): GeminiResponseDto {
        val parts = mutableListOf<RequestPart>()
        parts.add(RequestPart(text = content))
        val requestBody = RequestBody(contents = listOf(ContentItem(parts = parts)))

        return try {
            val responseText = client.post {
                url("v1beta/models/${GEMINI_PRO}:generateContent")
                parameter("key", apiKey)
                setBody(Json.encodeToString(requestBody))
            }.body<GeminiResponseDto>()
            println("API Response: $responseText")
            responseText
        } catch (e: Exception) {
            println("Error during API request: ${e.message}")
            throw e
        }
    }


    override suspend fun generateContentWithImage(
        content: String,
        apiKey: String,
        images: List<ByteArray>
    ): GeminiResponseDto {
        val parts = mutableListOf<RequestPart>()
        parts.add(RequestPart(text = content))
        images.map { image ->
            val inlineData = RequestInlineData("image/jpeg", image.encodeBase64())
            parts.add(RequestPart(inlineData = inlineData))
        }
        val requestBody = RequestBody(contents = listOf(ContentItem(parts = parts)))

        return try {
            val responseText = client.post {
                url("v1beta/models/${GEMINI_PRO_VISION}:generateContent")
                parameter("key", apiKey)
                setBody(Json.encodeToString(requestBody))
            }.body<GeminiResponseDto>()

            println("API Response: $responseText")
            responseText

        } catch (e: Exception) {
            println("Error during API request: ${e.message}")
            throw e
        }
    }
}