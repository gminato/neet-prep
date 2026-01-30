package com.vayu.neetprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vayu.neetprep.model.MockTest
import com.vayu.neetprep.ui.components.BottomNavigationBar
import com.vayu.neetprep.ui.theme.BackgroundGray
import com.vayu.neetprep.ui.theme.TealPrimary
import com.vayu.neetprep.ui.theme.TextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockTestsScreen(onTabSelected: (String) -> Unit) {
    val categories = listOf("All", "Full Syllabus", "Biology", "Physics")
    val difficulties = listOf(
        DifficultyItem("Easy", Color(0xFF4CAF50)),
        DifficultyItem("Medium", Color(0xFFF57C00)),
        DifficultyItem("Hard", Color(0xFFD32F2F))
    )

    val mockTests = listOf(
        MockTest(1, "All India Mock Test #12", "MEDIUM", "3h 20m", 720),
        MockTest(2, "Biology Unit Test: Genetics", "HARD", "1h 00m", 180),
        MockTest(3, "Physics: Mechanics Basics", "EASY", "0h 45m", 100),
        MockTest(4, "Full Syllabus Test #5", "MEDIUM", "3h 20m", 720)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mock Tests", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedItem = "Tests", onTabSelected = onTabSelected)
        },
        containerColor = BackgroundGray
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = category == "All",
                            onClick = { /* TODO */ },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TealPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Color.Gray
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = Color.LightGray.copy(alpha = 0.5f),
                                enabled = true,
                                selected = category == "All"
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }

            item {
                Column {
                    Text(
                        "DIFFICULTY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        difficulties.forEach { diff ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(diff.color, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(diff.label, color = diff.color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            items(mockTests) { test ->
                MockTestCard(test)
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.LightGray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("You're all caught up!", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}

data class DifficultyItem(val label: String, val color: Color)

@Composable
fun MockTestCard(test: MockTest) {
    val tagColor = when (test.difficulty) {
        "EASY" -> Color(0xFFE8F5E9)
        "MEDIUM" -> Color(0xFFFFF3E0)
        "HARD" -> Color(0xFFFFEBEE)
        else -> Color.Gray
    }
    val tagTextColor = when (test.difficulty) {
        "EASY" -> Color(0xFF4CAF50)
        "MEDIUM" -> Color(0xFFF57C00)
        "HARD" -> Color(0xFFD32F2F)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = test.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = tagColor,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = test.difficulty,
                        color = tagTextColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoBadge(icon = Icons.Default.AccessTime, text = test.duration)
                InfoBadge(icon = Icons.Default.EmojiEvents, text = "${test.marks} Marks")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Start Test", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun InfoBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        color = BackgroundGray,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Icon(icon, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        }
    }
}
