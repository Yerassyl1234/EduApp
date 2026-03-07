package com.example.eduapp.feature.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

private val GradientColors = listOf(
    Color(0xFF26A69A),
    Color(0xFF00897B),
    Color(0xFF004D40)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    onBackClick: () -> Unit,
    onFinish: () -> Unit,
    viewModel: TestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isFinished) {
        TestCompletedScreen(
            isSaving = uiState.isSaving,
            onBack = onBackClick
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F3))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(GradientColors),
                    shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .statusBarsPadding()
                .padding(top = 8.dp, bottom = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Артқа",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (uiState.questions.isNotEmpty()) {
                            "${uiState.currentQuestionIndex + 1} / ${uiState.questions.size}"
                        } else {
                            uiState.testTitle
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                if (uiState.questions.isNotEmpty()) {
                    LinearProgressIndicator(
                        progress = {
                            (uiState.currentQuestionIndex + 1).toFloat() / uiState.questions.size
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = Color(0xFF00897B)) }
            return
        }

        if (uiState.questions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("Сұрақтар жоқ", color = Color(0xFF6B7B78)) }
            return
        }

        val question = uiState.questions[uiState.currentQuestionIndex]
        val selectedAnswer = uiState.selectedAnswers[uiState.currentQuestionIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Text(
                    text = question.text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF1C1B1F)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val labels = listOf("A", "B", "C", "D")
            question.options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == index

                Card(
                    onClick = { viewModel.selectAnswer(index) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Color(0xFF00897B) else Color(0xFFDAE5E3)
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF00897B).copy(alpha = 0.08f) else Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF00897B) else Color(0xFFF0F4F3)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = labels.getOrElse(index) { "" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) Color.White else Color(0xFF6B7B78)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = option,
                            color = if (isSelected) Color(0xFF004D40) else Color(0xFF1C1B1F)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (uiState.currentQuestionIndex > 0) {
                    OutlinedButton(
                        onClick = viewModel::previousQuestion,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFF00897B)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF00897B)
                        )
                    ) { Text("Алдыңғы", fontWeight = FontWeight.Medium) }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                val isLast = uiState.currentQuestionIndex == uiState.questions.size - 1
                Button(
                    onClick = {
                        if (isLast) viewModel.finishTest()
                        else viewModel.nextQuestion()
                    },
                    shape = RoundedCornerShape(14.dp),
                    enabled = selectedAnswer != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00897B),
                        disabledContainerColor = Color(0xFF00897B).copy(alpha = 0.4f)
                    )
                ) {
                    Text(
                        if (isLast) "Аяқтау" else "Келесі",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun TestCompletedScreen(
    isSaving: Boolean,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F3))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isSaving) {
            CircularProgressIndicator(color = Color(0xFF00897B))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Нәтиже сақталуда...", fontSize = 16.sp, color = Color(0xFF6B7B78))
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color(0xFF00897B)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Тест аяқталды!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004D40)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Нәтижеңіз сәтті сақталды.\nМұғалім нәтижеңізді көре алады.",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF6B7B78),
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00897B)
                        )
                    ) {
                        Text("Тесттерге оралу", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
