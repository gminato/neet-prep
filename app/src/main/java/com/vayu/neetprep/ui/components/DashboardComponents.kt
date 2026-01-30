package com.vayu.neetprep.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vayu.neetprep.model.LearningModule
import com.vayu.neetprep.model.Task
import com.vayu.neetprep.ui.theme.TealPrimary

@Composable
fun DashboardHeader(userName: String, progress: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Good evening,",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            Text(
                text = "Let's crush some goals today!",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
        
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = TealPrimary,
                strokeWidth = 6.dp,
                trackColor = Color.LightGray.copy(alpha = 0.3f),
                strokeCap = StrokeCap.Round,
            )
            Text("${(progress * 100).toInt()}%", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@Composable
fun ContinueLearningCard(module: LearningModule) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF00332B))
                ) {
                   Canvas(modifier = Modifier.fillMaxSize()) {
                       drawCircle(color = TealPrimary, radius = size.minDimension / 3)
                   }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(module.subject, color = TealPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(module.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(module.chapter, color = Color.Gray, fontSize = 14.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Progress", color = Color.Gray, fontSize = 12.sp)
                Text("${module.timeLeftMinutes} mins left", color = Color.Gray, fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { module.progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = TealPrimary,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resume Learning")
                }
            }
        }
    }
}

@Composable
fun StatCard(icon: ImageVector, value: String, label: String, iconColor: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Text(label, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(2.dp, if (task.isCompleted) TealPrimary else Color.LightGray, RoundedCornerShape(6.dp))
                    .background(if (task.isCompleted) TealPrimary else Color.Transparent, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    color = if (task.isCompleted) Color.Gray else Color.Black
                )
                Text(
                    text = task.subTitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp).background(if (task.isCompleted) Color(0xFFE0F2F1) else Color(0xFFF5F5F5), CircleShape).padding(4.dp)
            )
        }
    }
}

@Composable
fun BottomNavigationBar(selectedItem: String, onTabSelected: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(if (selectedItem == "Home") Icons.Filled.Home else Icons.Outlined.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = selectedItem == "Home",
            onClick = { onTabSelected("Home") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = TealPrimary, selectedTextColor = TealPrimary, unselectedIconColor = Color.Gray)
        )
        NavigationBarItem(
            icon = { Icon(if (selectedItem == "Practice") Icons.Filled.HistoryEdu else Icons.Outlined.HistoryEdu, contentDescription = null) },
            label = { Text("Practice") },
            selected = selectedItem == "Practice",
            onClick = { onTabSelected("Practice") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = TealPrimary, selectedTextColor = TealPrimary, unselectedIconColor = Color.Gray)
        )
        NavigationBarItem(
            icon = { Icon(if (selectedItem == "Tests") Icons.Filled.Assignment else Icons.Outlined.Assignment, contentDescription = null) },
            label = { Text("Tests") },
            selected = selectedItem == "Tests",
            onClick = { onTabSelected("Tests") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = TealPrimary, selectedTextColor = TealPrimary, unselectedIconColor = Color.Gray)
        )
        NavigationBarItem(
            icon = { Icon(if (selectedItem == "Analysis") Icons.Filled.BarChart else Icons.Outlined.BarChart, contentDescription = null) },
            label = { Text("Analysis") },
            selected = selectedItem == "Analysis",
            onClick = { onTabSelected("Analysis") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = TealPrimary, selectedTextColor = TealPrimary, unselectedIconColor = Color.Gray)
        )
        NavigationBarItem(
            icon = { Icon(if (selectedItem == "Profile") Icons.Filled.Person else Icons.Outlined.Person, contentDescription = null) },
            label = { Text("Profile") },
            selected = selectedItem == "Profile",
            onClick = { onTabSelected("Profile") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = TealPrimary, selectedTextColor = TealPrimary, unselectedIconColor = Color.Gray)
        )
    }
}
