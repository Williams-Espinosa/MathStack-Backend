package com.mathstack.admin.application

import com.mathstack.social.domain.repository.SocialRepository
import com.mathstack.academic.domain.repository.AcademicRepository
import com.mathstack.admin.infrastructure.rest.dto.ChallengeResponse

class ListAllChallengesUseCase(
    private val socialRepository: SocialRepository,
    private val academicRepository: AcademicRepository
) {
    operator fun invoke(): List<ChallengeResponse> {
        return socialRepository.listAllChallenges().mapNotNull { challenge ->
            val exercise = academicRepository.findExerciseById(challenge.exerciseId)
            val lesson = exercise?.let { academicRepository.findLessonById(it.lessonId) }
            val participants = socialRepository.getChallengeParticipants(challenge.id)

            ChallengeResponse(
                id = challenge.id.toString(),
                creatorId = challenge.creatorId.toString(),
                status = challenge.status,
                createdAt = "2024-01-01T00:00:00Z", 
                title = lesson?.title?.let { "Reto: $it" } ?: "Reto de Ejercicio",
                description = exercise?.conceptTested ?: "Demuestra tus habilidades",
                participants = participants.size,
                isActive = challenge.status == "ACTIVE"
            )
        }
    }
}
