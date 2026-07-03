package com.mathstack.admin.application

import com.mathstack.admin.infrastructure.rest.dto.DashboardStatsResponse
import com.mathstack.admin.infrastructure.rest.dto.DifficultyStatsResponse
import com.mathstack.admin.infrastructure.rest.dto.UserGrowthResponse
import com.mathstack.admin.infrastructure.rest.dto.ActivityBySubjectResponse
import com.mathstack.users.domain.repository.UserRepository
import com.mathstack.academic.domain.repository.AcademicRepository
import com.mathstack.social.domain.repository.SocialRepository
import com.mathstack.practice.domain.repository.PracticeRepository
import java.time.format.DateTimeFormatter

class GetDashboardStatsUseCase(
    private val userRepository: UserRepository,
    private val academicRepository: AcademicRepository,
    private val socialRepository: SocialRepository,
    private val practiceRepository: PracticeRepository,
) {
    operator fun invoke(): DashboardStatsResponse {
        val allUsers = userRepository.findAll()
        val totalUsers = allUsers.size
        val activeUsers = totalUsers 
        
        val lessons = academicRepository.listLessons()
        val totalLessons = lessons.size
        
        val sessions = practiceRepository.findAllSessions()
        val completedLessons = sessions.sumOf { it.minutesSpent }
        
        val challenges = socialRepository.listAllChallenges()
        val totalChallenges = challenges.size
        val activeChallenges = challenges.count { it.status == "ACTIVE" }
        
        val diagnostics = practiceRepository.findAllDiagnostics()
        val subjects = academicRepository.listSubjects().associateBy { it.id }
        
        val difficultyStats = diagnostics.groupBy { it.subjectId }.map { (subjectId, results) ->
            DifficultyStatsResponse(
                subjectId = subjectId,
                subjectName = subjects[subjectId]?.name ?: "Unknown",
                totalAttempts = results.size,
                failureRate = results.count { it.deficiencyScore > 50 } / results.size.toDouble(),
                averageDeficiencyScore = results.map { it.deficiencyScore }.average(),
                averageScore = 100.0 - results.map { it.deficiencyScore }.average(),
                usersStruggling = results.count { it.deficiencyScore > 70 }
            )
        }
        
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val userGrowth = allUsers.groupBy { it.createdAt.toLocalDate().format(formatter) }
            .map { (date, users) -> UserGrowthResponse(date, users.size) }
            .sortedBy { it.date }
            
        val activityBySubject = difficultyStats.map {
            ActivityBySubjectResponse(it.subjectName, it.totalAttempts)
        }

        return DashboardStatsResponse(
            totalUsers = totalUsers,
            activeUsers = activeUsers,
            totalLessons = totalLessons,
            completedLessons = completedLessons,
            totalChallenges = totalChallenges,
            activeChallenges = activeChallenges,
            difficultyStats = difficultyStats,
            userGrowth = userGrowth,
            activityBySubject = activityBySubject
        )
    }
}
