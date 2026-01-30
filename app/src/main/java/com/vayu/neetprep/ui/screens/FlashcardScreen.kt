package com.vayu.neetprep.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vayu.neetprep.model.Flashcard
import com.vayu.neetprep.ui.theme.TealPrimary
import com.vayu.neetprep.ui.theme.TextDark
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FlashcardScreen(topicName: String, onBack: () -> Unit) {
    val flashcards = remember {
        listOf(
            Flashcard(1, "What is the primary pigment in Chlorophyceae?", "Chlorophyll a and b"),
            Flashcard(2, "Which algae is used as food supplement by space travellers?", "Chlorella"),
            Flashcard(3, "Brown algae are also known as?", "Phaeophyceae"),
            Flashcard(4, "What is the stored food in Rhodophyceae?", "Floridean starch"),
            Flashcard(5, "Which algae is known for causing red tide?", "Gonyaulax")
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

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
                                "FLASHCARD MODE",
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
                            "Card ${currentIndex + 1}/${flashcards.size}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / flashcards.size },
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 48.dp)
            ) {
                // Background indicator for right swipe
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        Icons.Default.Done,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier
                            .size(48.dp)
                            .alpha((offsetX.value / 300f).coerceIn(0f, 0.4f))
                    )
                }

                // Flashcard
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                        .graphicsLayer {
                            rotationZ = offsetX.value / 20f
                        }
                        .pointerInput(currentIndex) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (offsetX.value > 300f) {
                                        // Swiped Right
                                        scope.launch {
                                            offsetX.animateTo(1000f, tween(300))
                                            if (currentIndex < flashcards.size - 1) {
                                                currentIndex++
                                                isFlipped = false
                                                offsetX.snapTo(0f)
                                            } else {
                                                onBack()
                                            }
                                        }
                                    } else {
                                        scope.launch {
                                            offsetX.animateTo(0f, tween(300))
                                        }
                                    }
                                },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    // Only allow dragging to the right
                                    val newOffset = (offsetX.value + dragAmount).coerceAtLeast(0f)
                                    scope.launch {
                                        offsetX.snapTo(newOffset)
                                    }
                                }
                            )
                        }
                ) {
                    FlashcardItem(
                        flashcard = flashcards[currentIndex],
                        isFlipped = isFlipped,
                        onClick = { isFlipped = !isFlipped }
                    )
                }
            }

            // Buttons: FLIP and NEXT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { isFlipped = !isFlipped },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TealPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary)
                ) {
                    Text("FLIP", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        scope.launch {
                            offsetX.animateTo(1000f, tween(300))
                            if (currentIndex < flashcards.size - 1) {
                                currentIndex++
                                isFlipped = false
                                offsetX.snapTo(0f)
                            } else {
                                onBack()
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text("NEXT", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FlashcardItem(flashcard: Flashcard, isFlipped: Boolean, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "flashcard_rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // Front Side (Question)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        Icons.Default.Psychology,
                        contentDescription = null,
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        flashcard.question,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.TouchApp,
                            contentDescription = null,
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "TAP TO FLIP",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            } else {
                // Back Side (Answer)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(32.dp)
                        .graphicsLayer { rotationY = 180f }
                ) {
                    Text(
                        "Answer",
                        color = TealPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        flashcard.answer,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextDark,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
