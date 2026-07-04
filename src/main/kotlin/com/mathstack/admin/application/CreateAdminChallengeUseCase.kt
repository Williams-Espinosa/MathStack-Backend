package com.mathstack.admin.application

import com.mathstack.admin.domain.model.AdminChallenge
import com.mathstack.admin.domain.repository.AdminChallengeRepository
import java.time.LocalDateTime
import java.util.UUID

data class CreateAdminChallengeCommand(
    val title: String,
    val description: String,
    val subjectId: Int?,
    val difficulty: String,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val rewardCoins: Int,
    val rewardXp: Int,
    val targetScore: Int
)

class CreateAdminChallengeUseCase(
    private val repository: AdminChallengeRepository
) {
    operator fun invoke(command: CreateAdminChallengeCommand): AdminChallenge {
        val challenge = AdminChallenge(
            id = UUID.randomUUID(),
            title = command.title,
            description = command.description,
            subjectId = command.subjectId,
            difficulty = command.difficulty,
            startDate = command.startDate,
            endDate = command.endDate,
            rewardCoins = command.rewardCoins,
            rewardXp = command.rewardXp,
            targetScore = command.targetScore,
            status = "ACTIVE",
            createdAt = LocalDateTime.now()
        )
        return repository.create(challenge)
    }
}
