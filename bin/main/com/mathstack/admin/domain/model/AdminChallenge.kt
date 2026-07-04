package com.mathstack.admin.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class AdminChallenge(
    val id: UUID,
    val title: String,
    val description: String,
    val subjectId: Int?,
    val difficulty: String,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val rewardCoins: Int,
    val rewardXp: Int,
    val targetScore: Int,
    val status: String,
    val createdAt: LocalDateTime
)
