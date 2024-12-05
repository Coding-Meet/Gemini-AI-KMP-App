package com.coding.meet.gaminiaikmp.data.mapper

import com.coding.meet.gaminiaikmp.data.models.CandidateDto
import com.coding.meet.gaminiaikmp.data.models.ContentDto
import com.coding.meet.gaminiaikmp.data.models.GeminiResponseDto
import com.coding.meet.gaminiaikmp.data.models.PartDto

import com.coding.meet.gaminiaikmp.domain.model.Gemini


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

