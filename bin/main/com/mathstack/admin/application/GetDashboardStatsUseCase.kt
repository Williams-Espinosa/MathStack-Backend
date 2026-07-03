package com.mathstack.admin.application

import com.mathstack.admin.infrastructure.rest.dto.DashboardStatsResponse
import com.mathstack.admin.infrastructure.rest.dto.DifficultyStatsResponse
import com.mathstack.admin.infrastructure.rest.dto.UserGrowthResponse
import com.mathstack.admin.infrastructure.rest.dto.ActivityBySubjectResponse
import com.mathstack.users.domain.repository.UserRepository

class GetDashboardStatsUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(): DashboardStatsResponse {
        val allUsers = userRepository.findAll()
        val totalUsers = allUsers.size
        val activeUsers = totalUsers 
        
        return DashboardStatsResponse(
            totalUsers = totalUsers,
            activeUsers = activeUsers,
            totalLessons = 45,
            completedLessons = 1200,
            totalChallenges = 35,
            activeChallenges = 12,
            difficultyStats = listOf(
                DifficultyStatsResponse(
                    subjectId = 1,
                    subjectName = "Álgebra",
                    totalAttempts = 540,
                    failureRate = 0.42,
                    averageDeficiencyScore = 6.5,
                    averageScore = 65.0,
                    usersStruggling = 45
                ),
                DifficultyStatsResponse(
                    subjectId = 2,
                    subjectName = "Geometría",
                    totalAttempts = 320,
                    failureRate = 0.25,
                    averageDeficiencyScore = 4.2,
                    averageScore = 78.5,
                    usersStruggling = 12
                )
            ),
            userGrowth = listOf(
                UserGrowthResponse("2026-07-01", 5),
                UserGrowthResponse("2026-07-02", 12),
                UserGrowthResponse("2026-07-03", totalUsers)
            ),
            activityBySubject = listOf(
                ActivityBySubjectResponse("Álgebra", 450),
                ActivityBySubjectResponse("Geometría", 200),
                ActivityBySubjectResponse("Cálculo", 150)
            )
        )
    }
}
