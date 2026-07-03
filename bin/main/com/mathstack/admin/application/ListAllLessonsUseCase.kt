package com.mathstack.admin.application

import com.mathstack.academic.domain.repository.AcademicRepository
import com.mathstack.admin.infrastructure.rest.dto.LearningMaterialResponse

class ListAllLessonsUseCase(
    private val academicRepository: AcademicRepository
) {
    operator fun invoke(): List<LearningMaterialResponse> {
        return academicRepository.listLessons().map { lesson ->
            val subject = academicRepository.findSubjectById(lesson.subjectId)
            val lessonType = academicRepository.findLessonTypeById(lesson.lessonTypeId)
            val exercises = academicRepository.listExercisesByLesson(lesson.id)

            val difficultyName = when {
                lesson.difficultyLevel <= 3 -> "beginner"
                lesson.difficultyLevel <= 7 -> "intermediate"
                else -> "advanced"
            }

            LearningMaterialResponse(
                id = lesson.id.toString(),
                title = lesson.title,
                subjectId = lesson.subjectId,
                subject = subject?.name ?: "Unknown",
                lessonTypeId = lesson.lessonTypeId,
                lessonType = lessonType?.name ?: "Unknown",
                difficulty = difficultyName,
                difficultyLevel = lesson.difficultyLevel,
                type = "lesson",
                content = lesson.content,
                exerciseCount = exercises.size
            )
        }
    }
}
