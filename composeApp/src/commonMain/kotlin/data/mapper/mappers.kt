package data.mapper

import data.models.*
import domain.model.Gemini


fun GeminiResponseDto.toGemini(): Gemini {
    return Gemini(candidates.map { it.toCandidate() })
}

fun CandidateDto.toCandidate(): Gemini.Candidate {
    return Gemini.Candidate(content.toContent())
}

fun ContentDto.toContent(): Gemini.Content {
    return Gemini.Content(parts.map { it.toPart() }, role)
}

fun PartDto.toPart(): Gemini.Part {
    return Gemini.Part(text)
}

