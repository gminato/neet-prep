package com.vayu.neetprep.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.vayu.neetprep.model.ScoreTrendPoint
import com.vayu.neetprep.model.SubjectPerformance
import com.vayu.neetprep.model.WeakArea
import com.vayu.neetprep.ui.components.BottomNavigationBar
import com.vayu.neetprep.ui.theme.BackgroundGray
import com.vayu.neetprep.ui.theme.TealPrimary
import com.vayu.neetprep.ui.theme.TextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceAnalysisScreen(onBack: () -> Unit, onTabSelected: (String) -> Unit) {
    val scoreTrend = listOf(
        ScoreTrendPoint("TEST 1", 450),
        ScoreTrendPoint("TEST 2", 520),
        ScoreTrendPoint("TEST 3", 490),
        ScoreTrendPoint("TEST 4", 580),
        ScoreTrendPoint("TEST 5", 640)
    )

    val performances = listOf(
        SubjectPerformance("Biology", 95, "#FF109181", iconName = "spa"),
        SubjectPerformance("Chemistry", 80, "#FF3E8EF0", iconName = "science"),
        SubjectPerformance("Physics", 65, "#FFF07D3E", tag = "WEAK", tagColorHex = "#FFFFEBEE", tagTextColorHex = "#FFFF0000", iconName = "bolt")
    )

    val weakAreas = listOf(
        WeakArea("Rotational Motion", "Physics • High Weightage", "#FFF07D3E"),
        WeakArea("Thermodynamics", "Physics • Conceptual", "#FFF07D3E")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance Analysis", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedItem = "Analysis", onTabSelected = onTabSelected)
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
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabItem("Overview", isSelected = true, modifier = Modifier.weight(1f))
                TabItem("Accuracy", isSelected = false, modifier = Modifier.weight(1f))
                TabItem("Time", isSelected = false, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Total Score Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Score", color = Color.Gray, fontSize = 14.sp)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text("640", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
                            Text("/720", fontSize = 24.sp, color = Color.LightGray, modifier = Modifier.padding(bottom = 8.dp))
                        }
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Top 5%", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                        CircularProgressIndicator(
                            progress = { 0.88f },
                            modifier = Modifier.fillMaxSize(),
                            color = TealPrimary,
                            strokeWidth = 10.dp,
                            trackColor = Color.LightGray.copy(alpha = 0.2f),
                            strokeCap = StrokeCap.Round
                        )
                        Text("88%", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TealPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Score Trend Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Score Trend", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Surface(
                            color = BackgroundGray,
                            shape = RoundedCornerShape(8.dp),
                            onClick = { /* TODO */ }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                Text("Last 5 Tests", fontSize = 12.sp, color = Color.Gray)
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Simple Trend Line Mock
                    Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val path = Path().apply {
                                moveTo(0f, size.height * 0.8f)
                                cubicTo(size.width * 0.2f, size.height * 0.7f, size.width * 0.4f, size.height * 0.5f, size.width * 0.5f, size.height * 0.6f)
                                cubicTo(size.width * 0.6f, size.height * 0.7f, size.width * 0.8f, size.height * 0.3f, size.width, size.height * 0.2f)
                            }
                            drawPath(path, color = TealPrimary, style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))
                            
                            // Draw points
                            drawCircle(color = Color.White, radius = 6.dp.toPx(), center = Offset(size.width, size.height * 0.2f))
                            drawCircle(color = TealPrimary, radius = 6.dp.toPx(), center = Offset(size.width, size.height * 0.2f), style = Stroke(width = 2.dp.toPx()))
                        }
                        
                        // Score label
                        Surface(
                            modifier = Modifier.align(Alignment.TopEnd).padding(end = 10.dp),
                            color = TealPrimary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text("640", color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        scoreTrend.forEach { point ->
                            Text(point.label, fontSize = 10.sp, color = Color.LightGray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Subject Performance", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    performances.forEachIndexed { index, perf ->
                        SubjectPerformanceRow(perf)
                        if (index < performances.size - 1) Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Improve Weak Areas", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("View all", color = TealPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    weakAreas.forEachIndexed { index, area ->
                        WeakAreaItem(area)
                        if (index < weakAreas.size - 1) HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BackgroundGray)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun TabItem(label: String, isSelected: Boolean, modifier: Modifier) {
    Surface(
        modifier = modifier.height(40.dp),
        color = if (isSelected) TealPrimary else Color.Transparent,
        shape = RoundedCornerShape(10.dp),
        onClick = { /* TODO */ }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if (isSelected) Color.White else Color.Gray,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SubjectPerformanceRow(perf: SubjectPerformance) {
    val color = Color(perf.colorHex.toColorInt())
    val icon = when(perf.iconName) {
        "spa" -> Icons.Default.Spa
        "science" -> Icons.Default.Science
        else -> Icons.Default.Bolt
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(perf.subject, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f))
            
            if (perf.tag != null) {
                val tagColor = Color(perf.tagColorHex!!.toColorInt())
                val tagTextColor = Color(perf.tagTextColorHex!!.toColorInt())
                Surface(color = tagColor, shape = RoundedCornerShape(6.dp), modifier = Modifier.padding(end = 8.dp)) {
                    Text(perf.tag, color = tagTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
            
            Text("${perf.percentage}%", fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { perf.percentage / 100f },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
            color = color,
            trackColor = BackgroundGray
        )
    }
}

@Composable
fun WeakAreaItem(area: WeakArea) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(Color(area.colorHex.toColorInt()), CircleShape))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(area.topic, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(area.detail, color = Color.Gray, fontSize = 12.sp)
        }
        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier.size(32.dp).border(1.dp, Color.LightGray.copy(alpha = 0.3f), CircleShape)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(16.dp))
        }
    }
}
