package com.studentos.app.domain.planner

import com.studentos.app.domain.model.Assignment
import com.studentos.app.domain.model.Course
import com.studentos.app.domain.model.CourseDifficulty
import java.time.LocalDate

data class StudyPlan(
    val recommendedTasks: List<Assignment>,
    val recommendedStudySessions: List<Course>
)

class DailyPlanner {
    fun generatePlan(
        assignments: List<Assignment>,
        courses: List<Course>
    ): StudyPlan {
        // Prioritize tasks based on:
        // 1. Completion status (incomplete first)
        // 2. Due date (urgent first)
        // 3. Course difficulty (weighted)
        val recommendedTasks = assignments
            .filter { !it.completed }
            .sortedWith(
                compareBy<Assignment> { it.dueDate }
                    .thenByDescending { assignment ->
                        val course = courses.find { it.id == assignment.courseId }
                        when (course?.difficulty) {
                            CourseDifficulty.HARD -> 3
                            CourseDifficulty.MEDIUM -> 2
                            CourseDifficulty.EASY -> 1
                            else -> 0
                        }
                    }
            )
            .take(5)

        // Recommend study sessions for courses with upcoming tasks
        val courseIdsWithUpcomingTasks = recommendedTasks.map { it.courseId }.toSet()
        val recommendedCourses = courses.filter { it.id in courseIdsWithUpcomingTasks }
            .sortedByDescending { it.difficulty }
            .ifEmpty { courses.sortedByDescending { it.difficulty }.take(2) }

        return StudyPlan(
            recommendedTasks = recommendedTasks,
            recommendedStudySessions = recommendedCourses
        )
    }
}
