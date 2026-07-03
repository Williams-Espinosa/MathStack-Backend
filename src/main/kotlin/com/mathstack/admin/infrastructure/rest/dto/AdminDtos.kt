package com.mathstack.admin.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class DashboardStatsResponse(
    val totalUsers: Int,
    val activeUsers: Int,
    val totalLessons: Int,
    val completedLessons: Int,
    val totalChallenges: Int,
    val activeChallenges: Int,
    val difficultyStats: List<DifficultyStatsResponse>,
    val userGrowth: List<UserGrowthResponse>,
    val activityBySubject: List<ActivityBySubjectResponse>
)

@Serializable
data class DifficultyStatsResponse(
    val subjectId: Int,
    val subjectName: String,
    val totalAttempts: Int,
    val failureRate: Double,
    val averageDeficiencyScore: Double,
    val averageScore: Double = 0.0,
    val usersStruggling: Int = 0
)

@Serializable
data class UserGrowthResponse(
    val date: String,
    val count: Int
)

@Serializable
data class ActivityBySubjectResponse(
    val subject: String,
    val count: Int
)

@Serializable
data class OverviewStatsResponse(
    val cpuUsagePercent: Double,
    val memoryUsageMb: Long
)

@Serializable
data class UpdateCoinsRequest(
    val coins: Int
)

@Serializable
data class GenerateAvatarRequest(
    val seed: String,
    val style: String = "bottts"
)

@Serializable
data class GenerateAvatarResponse(
    val avatarUrl: String
)
