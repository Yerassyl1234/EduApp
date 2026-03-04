package com.example.eduapp.feature.teacher.admin


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.compose.LifecycleResumeEffect

private val GradientColors = listOf(
    Color(0xFF26A69A),
    Color(0xFF00897B),
    Color(0xFF004D40)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    onCreateTestClick: () -> Unit,
    onTestListClick: () -> Unit,
    onStudentsClick: () -> Unit,
    onCoursesClick: () -> Unit,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showTestListSheet by remember { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F3)),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // === GRADIENT HEADER ===
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(GradientColors),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "Админ панель",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item {
            AdminMenuItem(
                icon = Icons.Default.Book,
                title = "Курстар",
                onClick = onCoursesClick
            )
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            AdminMenuItem(
                icon = Icons.Default.List,
                title = "Тесттер тізімі",
                subtitle = "${uiState.tests.size} тест",
                onClick = { showTestListSheet = true }
            )
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            AdminMenuItem(
                icon = Icons.Default.Add,
                title = "Жаңа тест жасау",
                onClick = onCreateTestClick
            )
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {
            AdminMenuItem(
                icon = Icons.Default.People,
                title = "Қолданушылар",
                subtitle = "Нәтижелер: ${uiState.allResults.size}",
                onClick = onStudentsClick
            )
        }


    }

    // === Тесттер тізімі BottomSheet ===
    if (showTestListSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTestListSheet = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Тесттер тізімі", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF004D40))
                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.tests.isEmpty()) {
                    Text(
                        "Тест жоқ",
                        color = Color(0xFF6B7B78),
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.tests, key = { it.id }) { test ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8F7))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            modifier = Modifier.size(38.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            color = Color(0xFF00897B).copy(alpha = 0.12f)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    Icons.Default.Quiz,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(20.dp),
                                                    tint = Color(0xFF00897B)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = test.title,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 15.sp,
                                                color = Color(0xFF1C1B1F)
                                            )
                                            Text(
                                                text = "${test.questionCount} сұрақ",
                                                fontSize = 13.sp,
                                                color = Color(0xFF6B7B78)
                                            )
                                        }
                                    }
                                    IconButton(onClick = { viewModel.deleteTest(test.id); }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Жою",
                                            tint = Color(0xFFBA1A1A)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AdminMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF00897B).copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF00897B),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF1C1B1F))
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7B78)
                    )
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF6B7B78))
        }
    }
}
