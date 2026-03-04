package com.example.eduapp.feature.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestResultScreen(
    score: Int,
    total: Int,
    onBackToTests: () -> Unit
) {
    val percentage = if (total > 0) (score * 100) / total else 0
    val emoji = when {
        percentage >= 90 -> "🏆"
        percentage >= 70 -> "👍"
        percentage >= 50 -> "📚"
        else -> "💪"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = emoji, fontSize = 64.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Нәтиже",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$score / $total",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$percentage%",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when {
                percentage >= 90 -> "Керемет! Тамаша нәтиже!"
                percentage >= 70 -> "Жақсы! Білімің жеткілікті."
                percentage >= 50 -> "Орташа. Тағы қайталау керек."
                else -> "Қайта оқып, тағы тырыс!"
            },
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBackToTests,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Тесттерге оралу", fontSize = 16.sp)
        }
    }
}
