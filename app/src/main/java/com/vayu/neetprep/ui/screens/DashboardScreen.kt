package com.vayu.neetprep.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vayu.neetprep.model.DashboardStats
import com.vayu.neetprep.model.LearningModule
import com.vayu.neetprep.model.Task
import com.vayu.neetprep.ui.components.*
import com.vayu.neetprep.ui.theme.AccuracyBlue
import com.vayu.neetprep.ui.theme.BackgroundGray
import com.vayu.neetprep.ui.theme.StreakOrange
import com.vayu.neetprep.ui.theme.TealPrimary

@Composable
fun DashboardScreen(onTabSelected: (String) -> Unit) {
    // Mock Data
    val stats = DashboardStats(streakCount = 12, accuracyPercentage = 85, overallProgress = 0.65f)
    val currentModule = LearningModule(
        subject = "BIOLOGY",
        title = "Plant Kingdom",
        chapter = "Chapter 3 • Botany",
        progress = 0.7f,
        timeLeftMinutes = 15
    )
    val tasks = listOf(
        Task(1, "Complete Physics Quiz", "Optics • 20 Questions", false),
        Task(2, "Read NCERT Chemistry", "Pages 40-55 • Haloalkanes", true),
        Task(3, "Watch Lecture: Laws of Motion", "Physics • 45 mins", false)
    )

    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                selectedItem = "Home",
                onTabSelected = onTabSelected
            ) 
        },
        containerColor = BackgroundGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            DashboardHeader(userName = "Harshit", progress = stats.overallProgress)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Continue Learning", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            ContinueLearningCard(currentModule)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    icon = Icons.Default.LocalFireDepartment, 
                    value = "${stats.streakCount}", 
                    label = "Day Streak", 
                    iconColor = StreakOrange, 
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Default.Quiz, 
                    value = "${stats.accuracyPercentage}%", 
                    label = "Accuracy", 
                    iconColor = AccuracyBlue, 
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Today's Tasks", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("See all", color = TealPrimary, fontWeight = FontWeight.SemiBold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            tasks.forEach { task ->
                TaskItem(task)
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
