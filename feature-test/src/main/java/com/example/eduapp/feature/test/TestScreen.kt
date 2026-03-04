package com.example.eduapp.feature.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(
    onBackClick: () -> Unit,
    onFinish: () -> Unit,
    viewModel: TestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Если тест завершён — показываем экран завершения
    if (uiState.isFinished) {
        TestCompletedScreen(
            isSaving = uiState.isSaving,
            onBack = onBackClick
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState.questions.isNotEmpty()) {
                        Text("${uiState.currentQuestionIndex + 1} / ${uiState.questions.size}")
                    } else {
                        Text(uiState.testTitle)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Артқа")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        if (uiState.questions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { Text("Сұрақтар жоқ") }
            return@Scaffold
        }

        val question = uiState.questions[uiState.currentQuestionIndex]
        val selectedAnswer = uiState.selectedAnswers[uiState.currentQuestionIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Прогресс бар
            LinearProgressIndicator(
                progress = {
                    (uiState.currentQuestionIndex + 1).toFloat() / uiState.questions.size
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Вопрос
            Text(
                text = question.text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Варианты ответов
            val labels = listOf("A", "B", "C", "D")
            question.options.forEachIndexed { index, option ->
                val isSelected = selectedAnswer == index

                OutlinedCard(
                    onClick = { viewModel.selectAnswer(index) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${labels.getOrElse(index) { "" }})",
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = option)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопки навигации
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (uiState.currentQuestionIndex > 0) {
                    OutlinedButton(
                        onClick = viewModel::previousQuestion,
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Алдыңғы") }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                val isLast = uiState.currentQuestionIndex == uiState.questions.size - 1
                Button(
                    onClick = {
                        if (isLast) viewModel.finishTest()
                        else viewModel.nextQuestion()
                    },
                    shape = RoundedCornerShape(12.dp),
                    enabled = selectedAnswer != null
                ) {
                    Text(if (isLast) "Аяқтау" else "Келесі")
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
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isSaving) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Нәтиже сақталуда...", fontSize = 16.sp)
        } else {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Тест аяқталды!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Нәтижеңіз сәтті сақталды.\nМұғалім нәтижеңізді көре алады.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Тесттерге оралу", fontSize = 16.sp)
            }
        }
    }
}
