package data.network.utils

import io.ktor.util.encodeBase64
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/*
    val requestBody = mapOf(
            "contents" to listOf(
                mapOf("parts" to listOf(mapOf("text" to content)))
            )
        )
        *****************************************
    val requestBody = mapOf(
            "contents" to listOf(
                mapOf("parts" to listOf(mapOf("text" to content))),
                mapOf("inline_data" to mapOf("mime_type" to "image/jpeg", "data" to image))
            )
        )

*/


@Serializable
data class ContentItem(val parts: List<RequestPart>)

@Serializable
data class RequestBody(val contents: List<ContentItem>)

@Serializable
data class RequestPart(
    val text: String? = null,
    val inlineData: RequestInlineData? = null
)

@Serializable
data class RequestInlineData(
    @SerialName("mimeType") val mimeType: String,
    @SerialName("data") val data: String
)

class RequestBuilder {

    private val parts = mutableListOf<RequestPart>()

    fun addText(content: String): RequestBuilder {
        parts.add(RequestPart(text = content))
        return this
    }

    fun addImage(images: List<ByteArray>, mimeType: String = "image/jpeg"): RequestBuilder {
        images.map { image ->
            val inlineData = RequestInlineData(mimeType, image.encodeBase64())
            parts.add(RequestPart(inlineData = inlineData))
        }
        return this
    }

    fun build(): RequestBody = RequestBody(contents = listOf(ContentItem(parts = parts)))
}

inline fun <reified T : Any> String.fromJson(): T = Json.decodeFromString(this)

inline fun <reified T : Any> T.toJson(): String = Json.encodeToString(this)
