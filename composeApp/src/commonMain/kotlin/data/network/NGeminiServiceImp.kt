package data.network

import data.models.NGeminiResponseDto
import data.network.utils.Constant.GEMINI_PRO
import data.network.utils.Constant.GEMINI_PRO_VISION
import data.network.utils.RequestBuilder
import data.network.utils.fromJson
import data.network.utils.toJson
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.util.InternalAPI


class NGeminiServiceImp(
    private val client: HttpClient
) : NGeminiService {


    @OptIn(InternalAPI::class)
    override suspend fun generateContent(content: String): NGeminiResponseDto {
        val url = GEMINI_PRO
        val requestBody = RequestBuilder()
            .addText(content)
            .build()
        return try {
            val responseText: String = client.post(url) {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                body = requestBody.toJson()
            }.bodyAsText()
            println("API Response: $responseText")
            responseText.fromJson()
        } catch (e: Exception) {
            println("Error during API request: ${e.message}")
            throw e
        }
    }


    @OptIn(InternalAPI::class)
    override suspend fun generateContentWithImage(
        content: String,
        images: List<ByteArray>
    ): NGeminiResponseDto {
        val url = GEMINI_PRO_VISION
        val requestBody = RequestBuilder()
            .addText(content)
            .addImage(images)
            .build()
        return try {
            val responseText: String = client.post(url) {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                body = requestBody.toJson()
            }.bodyAsText()
            println("API Response: $responseText")
            responseText.fromJson()
        } catch (e: Exception) {
            println("Error during API request: ${e.message}")
            throw e
        }
    }
}