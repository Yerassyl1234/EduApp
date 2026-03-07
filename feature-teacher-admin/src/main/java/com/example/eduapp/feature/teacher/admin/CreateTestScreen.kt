package com.example.eduapp.feature.teacher.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
fun CreateTestScreen(
    onBackClick: () -> Unit,
    viewModel: CreateTestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onBackClick()
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Артқа", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("Жаңа тест жасау", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.testTitle,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Тест атауы") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00897B),
                    cursorColor = Color(0xFF00897B),
                    focusedLabelColor = Color(0xFF00897B)
                )
            )

            OutlinedTextField(
                value = uiState.testDescription,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Сипаттама (міндетті емес)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00897B),
                    cursorColor = Color(0xFF00897B),
                    focusedLabelColor = Color(0xFF00897B)
                )
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFDAE5E3))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF00897B).copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = viewModel::previousQuestion,
                        enabled = uiState.currentQuestionIndex > 0
                    ) {
                        Icon(Icons.AutoMirrored.Filled.NavigateBefore, contentDescription = "Алдыңғы", tint = Color(0xFF004D40))
                    }

                    Text(
                        text = "Сұрақ ${uiState.currentQuestionIndex + 1} / ${uiState.questions.size}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004D40)
                    )

                    IconButton(
                        onClick = viewModel::nextQuestion,
                        enabled = uiState.currentQuestionIndex < uiState.questions.size - 1
                    ) {
                        Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = "Келесі", tint = Color(0xFF004D40))
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = viewModel::addQuestion,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Сұрақ қосу")
                }

                OutlinedButton(
                    onClick = { viewModel.deleteQuestion(uiState.currentQuestionIndex) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    enabled = uiState.questions.size > 1,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBA1A1A).copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBA1A1A))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Жою")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFDAE5E3))

            val currentQuestion = uiState.questions[uiState.currentQuestionIndex]

            OutlinedTextField(
                value = currentQuestion.text,
                onValueChange = viewModel::onQuestionTextChange,
                label = { Text("Сұрақ мәтіні") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00897B),
                    cursorColor = Color(0xFF00897B),
                    focusedLabelColor = Color(0xFF00897B)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Жауап нұсқалары (дұрысын таңдаңыз):",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF004D40)
            )
            val labels = listOf("A", "B", "C", "D")
            currentQuestion.options.forEachIndexed { index, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = currentQuestion.correctAnswerIndex == index,
                        onClick = { viewModel.onCorrectAnswerChange(index) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF00897B)
                        )
                    )
                    OutlinedTextField(
                        value = option,
                        onValueChange = { viewModel.onOptionChange(index, it) },
                        label = { Text("${labels[index]} нұсқа") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00897B),
                            cursorColor = Color(0xFF00897B),
                            focusedLabelColor = Color(0xFF00897B)
                        )
                    )
                }
            }
            if (uiState.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        text = uiState.error!!,
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFBA1A1A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Барлығы: ${uiState.questions.size} сұрақ",
                fontSize = 14.sp,
                color = Color(0xFF6B7B78)
            )
            Button(
                onClick = viewModel::saveTest,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = !uiState.isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00897B),
                    disabledContainerColor = Color(0xFF00897B).copy(alpha = 0.5f)
                )
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Тестті сақтау", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
