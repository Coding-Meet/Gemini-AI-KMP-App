package data.mapper

import data.models.*
import domain.models.NGemini


fun NGeminiResponseDto.toNGemini(): NGemini {

    return NGemini(candidates?.map { it.toCandidate() } ?: emptyList(),
        promptFeedback?.toPromptFeedback() ?: NGemini.PromptFeedback(emptyList()))
}

fun CandidateDto.toCandidate(): NGemini.Candidate {
    return NGemini.Candidate(content?.toContent(),
        finishReason,
        index ?: 0,
        safetyRatings?.map { it.toSafetyRating() } ?: emptyList())
}

fun ContentDto.toContent(): NGemini.Content {
    return NGemini.Content(parts?.map { it.toPart() } ?: emptyList(), role.orEmpty())
}

fun PartDto.toPart(): NGemini.Part {
    return NGemini.Part(
        text.orEmpty()
    )
}

fun PromptFeedbackDto.toPromptFeedback(): NGemini.PromptFeedback {
    return NGemini.PromptFeedback(safetyRatings?.map { it.toSafetyRating() } ?: emptyList())
}

fun SafetyRatingDto.toSafetyRating(): NGemini.SafetyRating {
    return NGemini.SafetyRating(
        category.orEmpty(), probability.orEmpty()
    )
}
