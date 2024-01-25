package data.services


import data.DataResult
import data.entity.GeminiContent
import data.entity.generateContent.GeminiGenerateContentRequest
import data.entity.generateContent.GeminiGenerateContentResponse
import data.network.client
import data.toDataResult
import io.ktor.client.request.*

class GeminiAPIService {
    suspend fun generateContent(
        key: String,
        list: List<GeminiContent>
    ): DataResult<GeminiGenerateContentResponse> {
        return client.post {
            url("v1beta/models/gemini-pro:generateContent")
            parameter("key",key)
            setBody(
                GeminiGenerateContentRequest(
                    contents = list
                )
            )
        }.toDataResult<GeminiGenerateContentResponse>()
    }

}