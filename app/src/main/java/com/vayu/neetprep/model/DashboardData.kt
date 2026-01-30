package com.vayu.neetprep.model

data class Task(
    val id: Int,
    val title: String,
    val subTitle: String,
    val isCompleted: Boolean
)

data class DashboardStats(
    val streakCount: Int,
    val accuracyPercentage: Int,
    val overallProgress: Float
)

data class LearningModule(
    val subject: String,
    val title: String,
    val chapter: String,
    val progress: Float,
    val timeLeftMinutes: Int
)

data class Subject(
    val id: Int,
    val title: String,
    val progress: Float,
    val tag: String? = null,
    val tagColorHex: String? = null,
    val tagTextColorHex: String? = null,
    val iconName: String,
    val iconTintHex: String,
    val isGridItem: Boolean = true
)

data class ScoreTrendPoint(
    val label: String,
    val score: Int
)

data class SubjectPerformance(
    val subject: String,
    val percentage: Int,
    val colorHex: String,
    val tag: String? = null,
    val tagColorHex: String? = null,
    val tagTextColorHex: String? = null,
    val iconName: String
)

data class WeakArea(
    val topic: String,
    val detail: String,
    val colorHex: String
)

data class MockTest(
    val id: Int,
    val title: String,
    val difficulty: String, // EASY, MEDIUM, HARD
    val duration: String,
    val marks: Int
)

data class Chapter(
    val id: Int,
    val title: String,
    val completedTopics: Int,
    val totalTopics: Int,
    val isLocked: Boolean = false
)

data class Topic(
    val id: Int,
    val title: String,
    val isCompleted: Boolean,
    val isLocked: Boolean = false
)
