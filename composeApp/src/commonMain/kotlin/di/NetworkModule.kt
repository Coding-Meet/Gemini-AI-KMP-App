package di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module



@OptIn(ExperimentalSerializationApi::class)
val networkModule = module {
    single {
        HttpClient {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    explicitNulls = false
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
                requestTimeoutMillis = 60_000
            }
            defaultRequest {
                url("https://generativelanguage.googleapis.com/")
                contentType(ContentType.Application.Json)
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        co.touchlab.kermit.Logger.d(tag = "KtorClient", null) {
                            message
                        }
                    }
                }
            }

        }
    }


}
