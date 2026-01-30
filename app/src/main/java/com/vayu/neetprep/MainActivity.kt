package com.vayu.neetprep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.vayu.neetprep.ui.screens.ChaptersScreen
import com.vayu.neetprep.ui.screens.DashboardScreen
import com.vayu.neetprep.ui.screens.ExploreSubjectsScreen
import com.vayu.neetprep.ui.screens.LoginScreen
import com.vayu.neetprep.ui.screens.MockTestsScreen
import com.vayu.neetprep.ui.screens.OnboardingScreen
import com.vayu.neetprep.ui.screens.PerformanceAnalysisScreen
import com.vayu.neetprep.ui.screens.SettingsScreen
import com.vayu.neetprep.ui.screens.TopicsScreen
import com.vayu.neetprep.ui.screens.FlashcardScreen
import com.vayu.neetprep.ui.screens.MCQScreen
import com.vayu.neetprep.ui.theme.NeetPrepTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeetPrepTheme {
                var currentScreen by remember { mutableStateOf("onboarding") }
                var selectedSubject by remember { mutableStateOf("Biology") }
                var selectedChapter by remember { mutableStateOf("Plant Kingdom") }
                var selectedTopic by remember { mutableStateOf("Algae") }

                val onTabSelected: (String) -> Unit = { tab ->
                    when (tab) {
                        "Home" -> currentScreen = "dashboard"
                        "Practice" -> currentScreen = "explore_subjects"
                        "Tests" -> currentScreen = "mock_tests"
                        "Analysis" -> currentScreen = "performance_analysis"
                        "Profile" -> currentScreen = "settings"
                    }
                }

                when (currentScreen) {
                    "onboarding" -> OnboardingScreen(onGetStarted = { currentScreen = "login" })
                    "login" -> LoginScreen(
                        onLoginSuccess = { currentScreen = "dashboard" },
                        onSkip = { currentScreen = "dashboard" }
                    )
                    "dashboard" -> DashboardScreen(onTabSelected = onTabSelected)
                    "explore_subjects" -> ExploreSubjectsScreen(
                        onBack = { currentScreen = "dashboard" },
                        onTabSelected = onTabSelected,
                        onSubjectClick = { subject ->
                            selectedSubject = subject
                            currentScreen = "chapters"
                        }
                    )
                    "chapters" -> ChaptersScreen(
                        subjectName = selectedSubject,
                        onBack = { currentScreen = "explore_subjects" },
                        onChapterClick = { chapter ->
                            selectedChapter = chapter
                            currentScreen = "topics"
                        }
                    )
                    "topics" -> TopicsScreen(
                        chapterName = selectedChapter,
                        onBack = { currentScreen = "chapters" },
                        onTopicSelected = { topic, mode ->
                            selectedTopic = topic
                            currentScreen = if (mode == "Flashcard") "flashcard" else "mcq"
                        }
                    )
                    "flashcard" -> FlashcardScreen(
                        topicName = selectedTopic,
                        onBack = { currentScreen = "topics" }
                    )
                    "mcq" -> MCQScreen(
                        topicName = selectedTopic,
                        onBack = { currentScreen = "topics" }
                    )
                    "performance_analysis" -> PerformanceAnalysisScreen(
                        onBack = { currentScreen = "dashboard" },
                        onTabSelected = onTabSelected
                    )
                    "mock_tests" -> MockTestsScreen(onTabSelected = onTabSelected)
                    "settings" -> SettingsScreen(
                        onBack = { currentScreen = "dashboard" },
                        onTabSelected = onTabSelected
                    )
                }
            }
        }
    }
}
