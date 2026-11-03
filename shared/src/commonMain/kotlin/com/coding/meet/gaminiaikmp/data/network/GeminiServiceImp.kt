package com.coding.meet.gaminiaikmp.data.network

import com.coding.meet.gaminiaikmp.data.models.*
import com.coding.meet.gaminiaikmp.domain.use_cases.GetSelectedModelUseCase
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalEncodingApi::class)
class GeminiServiceImp(
    private val client: HttpClient,
    private val getSelectedModelUseCase: GetSelectedModelUseCase
) : GeminiService {

    private val json = Json {
        prettyPrint = false
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false // Crucial: Never send null fields like "inlineData": null to Gemini API
    }

    private fun getSelectedModel(): String {
        return getSelectedModelUseCase()
    }

    override suspend fun generateContent(content: String, apiKey: String): GeminiResponseDto {
        val parts = mutableListOf<RequestPart>()
        parts.add(RequestPart(text = content))
        val requestBody = RequestBody(contents = listOf(ContentItem(parts = parts)))
        val model = getSelectedModel()

        return try {
            val response = client.post {
                url("v1beta/models/$model:generateContent")
                parameter("key", apiKey)
                setBody(json.encodeToString(requestBody))
            }
            val responseText = response.bodyAsText()
            println("Gemini API Response (${response.status}): $responseText")

            val dto = json.decodeFromString<GeminiResponseDto>(responseText)
            if (dto.error != null) {
                throw Exception(dto.error.message ?: "Gemini API Error: ${dto.error.status}")
            }
            if (dto.candidates.isEmpty()) {
                throw Exception("Failed to generate content: No candidates returned.")
            }
            dto
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
        images.forEach { image ->
            val inlineData = RequestInlineData("image/jpeg", Base64.Default.encode(image))
            parts.add(RequestPart(inlineData = inlineData))
        }
        val requestBody = RequestBody(contents = listOf(ContentItem(parts = parts)))
        val model = getSelectedModel()

        return try {
            val response = client.post {
                url("v1beta/models/$model:generateContent")
                parameter("key", apiKey)
                setBody(json.encodeToString(requestBody))
            }
            val responseText = response.bodyAsText()
            println("Gemini API Response (${response.status}): $responseText")

            val dto = json.decodeFromString<GeminiResponseDto>(responseText)
            if (dto.error != null) {
                throw Exception(dto.error.message ?: "Gemini API Error: ${dto.error.status}")
            }
            if (dto.candidates.isEmpty()) {
                throw Exception("Failed to generate content: No candidates returned.")
            }
            dto
        } catch (e: Exception) {
            println("Error during API request: ${e.message}")
            throw e
        }
    }

    override suspend fun listModels(apiKey: String): ListModelsResponseDto {
        return try {
            val response = client.get {
                url("v1beta/models")
                parameter("key", apiKey)
            }
            val responseText = response.bodyAsText()
            json.decodeFromString<ListModelsResponseDto>(responseText)
        } catch (e: Exception) {
            println("Error fetching Gemini models: ${e.message}")
            throw e
        }
    }
}
