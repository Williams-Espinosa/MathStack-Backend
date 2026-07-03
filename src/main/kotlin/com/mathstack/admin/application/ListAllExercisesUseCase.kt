package com.mathstack.admin.application

import com.mathstack.academic.domain.repository.AcademicRepository
import com.mathstack.admin.infrastructure.rest.dto.LearningMaterialResponse

class ListAllExercisesUseCase(
    private val academicRepository: AcademicRepository
) {
    operator fun invoke(): List<LearningMaterialResponse> {
        return academicRepository.listAllExercises().mapNotNull { exercise ->
            val lesson = academicRepository.findLessonById(exercise.lessonId) ?: return@mapNotNull null
            val subject = academicRepository.findSubjectById(lesson.subjectId)
            val lessonType = academicRepository.findLessonTypeById(lesson.lessonTypeId)

            val difficultyName = when {
                lesson.difficultyLevel <= 3 -> "beginner"
                lesson.difficultyLevel <= 7 -> "intermediate"
                else -> "advanced"
            }

            LearningMaterialResponse(
                id = exercise.id.toString(),
                title = "Ejercicio: ${exercise.conceptTested ?: "Práctica"}",
                subjectId = lesson.subjectId,
                subject = subject?.name ?: "Unknown",
                lessonTypeId = lesson.lessonTypeId,
                lessonType = lessonType?.name ?: "Unknown",
                difficulty = difficultyName,
                difficultyLevel = lesson.difficultyLevel,
                type = "exercise",
                content = exercise.content,
                exerciseCount = 1
            )
        }
    }
}
