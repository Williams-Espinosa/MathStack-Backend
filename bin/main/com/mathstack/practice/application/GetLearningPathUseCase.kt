package com.mathstack.practice.application

import com.mathstack.academic.domain.repository.AcademicRepository
import com.mathstack.practice.domain.repository.PracticeRepository
import java.util.UUID

data class LearningPathLessonDto(
    val id: String,
    val title: String,
    val difficultyLevel: Int,
    val xp: Int,
    val status: String,
    val subjectName: String
)

data class LearningPathResponseDto(
    val subjectId: Int,
    val subjectName: String,
    val lessons: List<LearningPathLessonDto>
)

class GetLearningPathUseCase(
    private val practiceRepository: PracticeRepository,
    private val academicRepository: AcademicRepository
) {
    operator fun invoke(userId: UUID): LearningPathResponseDto {
        val diagnostics = practiceRepository.findDiagnosticsByUserId(userId)
        
        val maxDeficiencyBySubject = diagnostics.groupBy { it.subjectId }
            .mapValues { entry -> entry.value.maxOf { it.deficiencyScore } }
            
        var targetSubjectIds = maxDeficiencyBySubject.filterValues { it >= 20 }.keys.toList()
        
        if (targetSubjectIds.isEmpty()) {
            val highest = maxDeficiencyBySubject.maxByOrNull { it.value }?.key ?: 1
            targetSubjectIds = listOf(highest)
        }

        val allSubjects = academicRepository.listSubjects().associateBy { it.id }
        
        val allLessons = targetSubjectIds.flatMap { subjectId ->
            val lessons = academicRepository.listLessonsBySubject(subjectId)
            lessons.map { it to (allSubjects[subjectId]?.name ?: "Materia desconocida") }
        }
        
        val userPaths = practiceRepository.findLearningPathsByUserId(userId)
        val userPathMap = userPaths.associateBy { it.lessonId }

        val lessonsResponse = mutableListOf<LearningPathLessonDto>()
        var previousLessonCompleted = true 

        for ((lesson, subjectName) in allLessons.sortedBy { it.first.difficultyLevel }) {
            val userPath = userPathMap[lesson.id]
            
            val status = when {
                userPath != null -> userPath.status
                previousLessonCompleted -> "available"
                else -> "locked"
            }

            lessonsResponse.add(
                LearningPathLessonDto(
                    id = lesson.id.toString(),
                    title = lesson.title,
                    difficultyLevel = lesson.difficultyLevel,
                    xp = lesson.difficultyLevel * 25,
                    status = status,
                    subjectName = subjectName
                )
            )
            
            previousLessonCompleted = (status == "completed")
        }

        return LearningPathResponseDto(
            subjectId = 0,
            subjectName = "Ruta de Reforzamiento",
            lessons = lessonsResponse
        )
    }
}
