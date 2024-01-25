package domain

sealed interface GeminiResult <T> {

    data class Loading<T>(val isLoading: Boolean): GeminiResult<T>

    data class Success<T>(val data: T): GeminiResult<T>

    data class Error<T>(val error: String?): GeminiResult<T>
}