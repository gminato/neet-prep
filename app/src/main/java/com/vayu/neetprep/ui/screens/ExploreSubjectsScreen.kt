package com.vayu.neetprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vayu.neetprep.model.Subject
import com.vayu.neetprep.ui.components.BottomNavigationBar
import com.vayu.neetprep.ui.theme.BackgroundGray
import com.vayu.neetprep.ui.theme.TealPrimary
import com.vayu.neetprep.ui.theme.TextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreSubjectsScreen(onBack: () -> Unit, onTabSelected: (String) -> Unit, onSubjectClick: (String) -> Unit) {
    // Mocking "data coming from backend"
    val subjectsFromBackend = listOf(
        Subject(
            id = 1,
            title = "Physics",
            progress = 0.32f,
            tag = "WEAK",
            tagColorHex = "#FFFFEBEE",
            tagTextColorHex = "#FFFF0000",
            iconName = "science",
            iconTintHex = "#FF1E88E5",
            isGridItem = true
        ),
        Subject(
            id = 2,
            title = "Chemistry",
            progress = 0.58f,
            tag = null,
            iconName = "science",
            iconTintHex = "#FFAB47BC",
            isGridItem = true
        ),
        Subject(
            id = 3,
            title = "Biology",
            progress = 0.75f,
            tag = "Strong",
            tagColorHex = "#FFE8F5E9",
            tagTextColorHex = "#FF2E7D32",
            iconName = "spa",
            iconTintHex = "#FF43A047",
            isGridItem = false
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore Subjects", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFDAB9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedItem = "Practice", onTabSelected = onTabSelected)
        },
        containerColor = BackgroundGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Your Progress",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Text(
                text = "Keep up the momentum in your weak areas.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Logic to display grid items from backend data
            val gridItems = subjectsFromBackend.filter { it.isGridItem }
            Row(modifier = Modifier.fillMaxWidth()) {
                gridItems.forEachIndexed { index, subject ->
                    SubjectGridItem(
                        subject = subject,
                        modifier = Modifier.weight(1f).clickable { onSubjectClick(subject.title) }
                    )
                    if (index < gridItems.size - 1) {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logic to display list items from backend data
            subjectsFromBackend.filter { !it.isGridItem }.forEach { subject ->
                SubjectListItem(subject = subject, onClick = { onSubjectClick(subject.title) })
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SubjectGridItem(
    subject: Subject,
    modifier: Modifier
) {
    val iconTint = Color(android.graphics.Color.parseColor(subject.iconTintHex))
    val tagColor = subject.tagColorHex?.let { Color(android.graphics.Color.parseColor(it)) } ?: Color.Transparent
    val tagTextColor = subject.tagTextColorHex?.let { Color(android.graphics.Color.parseColor(it)) } ?: Color.Transparent
    val icon = if (subject.iconName == "science") Icons.Default.Science else Icons.Default.Spa

    Card(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(iconTint.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
                }
                if (subject.tag != null) {
                    Surface(
                        color = tagColor,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = subject.tag,
                            color = tagTextColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(subject.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("${(subject.progress * 100).toInt()}% Completed", color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { subject.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = iconTint,
                trackColor = Color.LightGray.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun SubjectListItem(subject: Subject, onClick: () -> Unit) {
    val iconTint = Color(android.graphics.Color.parseColor(subject.iconTintHex))
    val tagColor = subject.tagColorHex?.let { Color(android.graphics.Color.parseColor(it)) } ?: Color.Transparent
    val tagTextColor = subject.tagTextColorHex?.let { Color(android.graphics.Color.parseColor(it)) } ?: Color.Transparent
    val icon = if (subject.iconName == "science") Icons.Default.Science else Icons.Default.Spa

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(iconTint.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(subject.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    if (subject.tag != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = tagColor,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = subject.tag,
                                color = tagTextColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                Text("${(subject.progress * 100).toInt()}% Completed", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { subject.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = TealPrimary,
                    trackColor = Color.LightGray.copy(alpha = 0.2f)
                )
            }
        }
    }
}
