package com.vayu.neetprep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.vayu.neetprep.model.MCQ
import com.vayu.neetprep.ui.theme.TealPrimary
import com.vayu.neetprep.ui.theme.TextDark

@Composable
fun MCQScreen(topicName: String, onBack: () -> Unit) {
    val mcqs = remember {
        listOf(
            MCQ(1, "Which of the following is a unicellular alga?", null, listOf("Chlamydomonas", "Volvox", "Ulothrix", "Spirogyra"), 0),
            MCQ(2, "Identify the given algal structure:", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Spirogyra.jpg/300px-Spirogyra.jpg", listOf("Spirogyra", "Volvox", "Ulothrix", "Chara"), 0),
            MCQ(3, "The cell wall of algae is made up of:", null, listOf("Cellulose, Galactans and Mannans", "Hemicellulose, Pectin and Proteins", "Pectin, Cellulose and Proteins", "Cellulose, Hemicellulose and Pectin"), 0),
            MCQ(4, "This alga shows a colonial form. Identify it:", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Volvox_aureus.jpg/220px-Volvox_aureus.jpg", listOf("Ulothrix", "Spirogyra", "Volvox", "Chlamydomonas"), 2),
            MCQ(5, "Algin is obtained from which type of algae?", null, listOf("Red Algae", "Brown Algae", "Green Algae", "Yellow Algae"), 1)
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var showCorrectAnswer by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    if (isFinished) {
        ScoreScreen(score = score, total = mcqs.size, onBack = onBack, onRetry = {
            currentIndex = 0
            selectedOptionIndex = null
            showCorrectAnswer = false
            score = 0
            isFinished = false
        })
    } else {
        Scaffold(
            topBar = {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(topicName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).background(TealPrimary, CircleShape))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "MCQ MODE",
                                    color = TealPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                        Surface(
                            color = Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "Q ${currentIndex + 1}/${mcqs.size}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                    LinearProgressIndicator(
                        progress = { (currentIndex + 1).toFloat() / mcqs.size },
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = TealPrimary,
                        trackColor = Color(0xFFE2E8F0)
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8FAFC))
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                val currentMCQ = mcqs[currentIndex]

                Text(
                    text = currentMCQ.questionText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    lineHeight = 28.sp
                )

                currentMCQ.questionImage?.let { imageUrl ->
                    Spacer(modifier = Modifier.height(16.dp))
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Question Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray.copy(alpha = 0.2f)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                currentMCQ.options.forEachIndexed { index, option ->
                    val isSelected = selectedOptionIndex == index
                    val isCorrect = currentMCQ.correctOptionIndex == index
                    
                    val borderColor = when {
                        showCorrectAnswer && isCorrect -> Color(0xFF10B981)
                        showCorrectAnswer && isSelected && !isCorrect -> Color(0xFFEF4444)
                        isSelected -> TealPrimary
                        else -> Color(0xFFE2E8F0)
                    }

                    val bgColor = when {
                        showCorrectAnswer && isCorrect -> Color(0xFF10B981).copy(alpha = 0.1f)
                        showCorrectAnswer && isSelected && !isCorrect -> Color(0xFFEF4444).copy(alpha = 0.1f)
                        isSelected -> TealPrimary.copy(alpha = 0.05f)
                        else -> Color.White
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                            .background(bgColor, RoundedCornerShape(12.dp))
                            .clickable(enabled = !showCorrectAnswer) {
                                selectedOptionIndex = index
                            }
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(24.dp),
                                shape = CircleShape,
                                border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                                color = if (isSelected) borderColor else Color.Transparent
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (isSelected) {
                                        Box(modifier = Modifier.size(8.dp).background(Color.White, CircleShape))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = option,
                                fontSize = 16.sp,
                                color = if (isSelected) TextDark else Color(0xFF64748B),
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (!showCorrectAnswer) {
                            if (selectedOptionIndex != null) {
                                showCorrectAnswer = true
                                if (selectedOptionIndex == currentMCQ.correctOptionIndex) {
                                    score++
                                }
                            }
                        } else {
                            if (currentIndex < mcqs.size - 1) {
                                currentIndex++
                                selectedOptionIndex = null
                                showCorrectAnswer = false
                            } else {
                                isFinished = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOptionIndex == null) Color(0xFFCBD5E1) else TealPrimary
                    ),
                    enabled = selectedOptionIndex != null
                ) {
                    Text(
                        text = if (!showCorrectAnswer) "CHECK ANSWER" else if (currentIndex < mcqs.size - 1) "NEXT QUESTION" else "FINISH",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreScreen(score: Int, total: Int, onBack: () -> Unit, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = Color(0xFFFFD700),
            modifier = Modifier.size(120.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Quiz Completed!",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextDark
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            "You've finished the topic",
            fontSize = 16.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Surface(
            color = TealPrimary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.size(160.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "$score/$total",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TealPrimary
                )
                Text(
                    "Score",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(64.dp))
        
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
        ) {
            Text("CONTINUE TO TOPICS", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE2E8F0))
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("TRY AGAIN", fontWeight = FontWeight.Bold, color = TextDark)
        }
    }
}
