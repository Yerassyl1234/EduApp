package com.example.eduapp.feature.teacher.admin


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

private val GradientColors = listOf(
    Color(0xFF26A69A),
    Color(0xFF00897B),
    Color(0xFF004D40)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
    onBackClick: () -> Unit,
    onSectionClick: (String) -> Unit,
    viewModel: CoursesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }
    var showEditDialog by remember { mutableStateOf<Pair<String, String>?>(null) }

    Scaffold(
        containerColor = Color(0xFFF0F4F3),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF00897B),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Жаңа секция қосу")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Артқа", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Курстар", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF00897B))
                }
                return@Scaffold
            }

            if (uiState.categories.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF00897B).copy(alpha = 0.12f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Book, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color(0xFF00897B))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Секциялар әлі жоқ", fontSize = 18.sp, color = Color(0xFF6B7B78), fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Жаңа секция қосу үшін + батырмасын басыңыз", fontSize = 14.sp, color = Color(0xFF6B7B78))
                    }
                }
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                    items(uiState.categories, key = { it.id }) { category ->
                    Card(
                        onClick = { onSectionClick(category.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(42.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF00897B).copy(alpha = 0.12f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Folder, contentDescription = null, tint = Color(0xFF00897B), modifier = Modifier.size(22.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = category.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1C1B1F))
                                    Text(text = "Реттік нөмірі: ${category.order}", fontSize = 13.sp, color = Color(0xFF6B7B78))
                                }
                                IconButton(onClick = { showEditDialog = Pair(category.id, category.title) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Өңдеу", tint = Color(0xFF00897B))
                                }
                                IconButton(onClick = { showDeleteDialog = category.id }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Жою", tint = Color(0xFFBA1A1A))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (category.published) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth().height(42.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF00897B).copy(alpha = 0.12f)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "✅ Басты бетте жарияланды",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF00897B)
                                        )
                                    }
                                }
                            } else {
                                Button(
                                    onClick = { viewModel.publishCategory(category.id) },
                                    modifier = Modifier.fillMaxWidth().height(42.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26A69A))
                                ) {
                                    Icon(Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Басты бетке жариялау", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showAddDialog) {
        var newTitle by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = Color.White,
            title = { Text("Жаңа секция қосу", color = Color(0xFF004D40)) },
            text = {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Секция атауы") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00897B),
                        cursorColor = Color(0xFF00897B),
                        focusedLabelColor = Color(0xFF00897B)
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            viewModel.addCategory(newTitle.trim())
                            showAddDialog = false
                        }
                    }
                ) { Text("Қосу", color = Color(0xFF00897B), fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Бас тарту", color = Color(0xFF6B7B78)) }
            }
        )
    }
    showDeleteDialog?.let { categoryId ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            containerColor = Color.White,
            title = { Text("Секцияны жою", color = Color(0xFF004D40)) },
            text = { Text("Бұл секцияны және оның барлық подкатегорияларын жойғыңыз келе ме?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCategory(categoryId)
                        showDeleteDialog = null
                    }
                ) { Text("Жою", color = Color(0xFFBA1A1A)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Бас тарту", color = Color(0xFF6B7B78)) }
            }
        )
    }
    showEditDialog?.let { (categoryId, currentTitle) ->
        var editedTitle by remember { mutableStateOf(currentTitle) }
        AlertDialog(
            onDismissRequest = { showEditDialog = null },
            containerColor = Color.White,
            title = { Text("Секция атауын өңдеу", color = Color(0xFF004D40)) },
            text = {
                OutlinedTextField(
                    value = editedTitle,
                    onValueChange = { editedTitle = it },
                    label = { Text("Жаңа атау") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00897B),
                        cursorColor = Color(0xFF00897B),
                        focusedLabelColor = Color(0xFF00897B)
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editedTitle.isNotBlank()) {
                            viewModel.updateCategoryTitle(categoryId, editedTitle.trim())
                            showEditDialog = null
                        }
                    }
                ) { Text("Сақтау", color = Color(0xFF00897B), fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = null }) { Text("Бас тарту", color = Color(0xFF6B7B78)) }
            }
        )
    }
}
