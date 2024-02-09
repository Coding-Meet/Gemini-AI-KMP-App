package domain.model

data class Gemini(
    val candidates: List<Candidate> = emptyList()
) {
    data class Candidate(
        val content: Content,
    )

    data class Content(
        val parts: List<Part>,
        val role: String
    )

    data class Part(
        val text: String
    )

}