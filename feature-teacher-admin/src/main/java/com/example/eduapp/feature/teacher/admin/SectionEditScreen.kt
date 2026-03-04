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
fun SectionEditScreen(
    onBackClick: () -> Unit,
    viewModel: SectionEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddComponentDialog by remember { mutableStateOf(false) }
    var showDeleteComponentDialog by remember { mutableStateOf<String?>(null) }
    var showEditComponentDialog by remember { mutableStateOf<com.example.eduapp.core.domain.model.Component?>(null) }
    var showEditTitleDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF0F4F3),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddComponentDialog = true },
                containerColor = Color(0xFF00897B),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Подкатегория қосу")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // === GRADIENT HEADER ===
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
                    Text(
                        text = uiState.category?.title ?: "Секция",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showEditTitleDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Атауын өңдеу", tint = Color.White)
                    }
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF00897B))
                }
                return@Scaffold
            }

            if (uiState.components.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF00897B).copy(alpha = 0.12f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color(0xFF00897B))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Подкатегориялар әлі жоқ", fontSize = 18.sp, color = Color(0xFF6B7B78), fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Жаңа подкатегория қосу үшін + батырмасын басыңыз", fontSize = 14.sp, color = Color(0xFF6B7B78))
                    }
                }
                return@Scaffold
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.components, key = { it.id }) { component ->
                    Card(
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
                                    modifier = Modifier.size(38.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFF00897B).copy(alpha = 0.12f)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Article, contentDescription = null, tint = Color(0xFF00897B), modifier = Modifier.size(20.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = component.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f),
                                    color = Color(0xFF1C1B1F)
                                )
                                IconButton(onClick = { showEditComponentDialog = component }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Өңдеу", tint = Color(0xFF00897B), modifier = Modifier.size(20.dp))
                                }
                                IconButton(onClick = { showDeleteComponentDialog = component.id }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Жою", tint = Color(0xFFBA1A1A), modifier = Modifier.size(20.dp))
                                }
                            }

                            if (component.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = component.description,
                                    fontSize = 14.sp,
                                    color = Color(0xFF6B7B78),
                                    maxLines = 3
                                )
                            }

                            if (component.composition.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Құрамы:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF00897B)
                                )
                                component.composition.forEach { item ->
                                    Text(
                                        text = "• $item",
                                        fontSize = 13.sp,
                                        color = Color(0xFF6B7B78),
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }

                            if (component.function.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Қызметі: ${component.function}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF6B7B78)
                                )
                            }

                            // === AR көру батырмасы ===
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { /* AR функционалы әлі іске асырылмаған */ },
                                modifier = Modifier.fillMaxWidth().height(42.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26A69A))
                            ) {
                                Icon(Icons.Default.ViewInAr, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AR көру", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }

    // === Жаңа подкатегория қосу диалогы ===
    if (showAddComponentDialog) {
        ComponentEditDialog(
            title = "Жаңа подкатегория қосу",
            onDismiss = { showAddComponentDialog = false },
            onConfirm = { title, description, composition, function ->
                viewModel.addComponent(title, description, composition, function)
                showAddComponentDialog = false
            }
        )
    }

    // === Подкатегорияны жою диалогы ===
    showDeleteComponentDialog?.let { componentId ->
        AlertDialog(
            onDismissRequest = { showDeleteComponentDialog = null },
            containerColor = Color.White,
            title = { Text("Подкатегорияны жою", color = Color(0xFF004D40)) },
            text = { Text("Бұл подкатегорияны жойғыңыз келе ме?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteComponent(componentId)
                        showDeleteComponentDialog = null
                    }
                ) { Text("Жою", color = Color(0xFFBA1A1A)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteComponentDialog = null }) { Text("Бас тарту", color = Color(0xFF6B7B78)) }
            }
        )
    }

    // === Подкатегорияны өңдеу диалогы ===
    showEditComponentDialog?.let { component ->
        ComponentEditDialog(
            title = "Подкатегорияны өңдеу",
            initialTitle = component.title,
            initialDescription = component.description,
            initialComposition = component.composition,
            initialFunction = component.function,
            onDismiss = { showEditComponentDialog = null },
            onConfirm = { title, description, composition, function ->
                viewModel.updateComponent(
                    component.copy(
                        title = title,
                        description = description,
                        composition = composition,
                        function = function
                    )
                )
                showEditComponentDialog = null
            }
        )
    }

    // === Секция атауын өңдеу диалогы ===
    if (showEditTitleDialog) {
        var editedTitle by remember { mutableStateOf(uiState.category?.title ?: "") }
        AlertDialog(
            onDismissRequest = { showEditTitleDialog = false },
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
                            viewModel.updateSectionTitle(editedTitle.trim())
                            showEditTitleDialog = false
                        }
                    }
                ) { Text("Сақтау", color = Color(0xFF00897B), fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = {
                TextButton(onClick = { showEditTitleDialog = false }) { Text("Бас тарту", color = Color(0xFF6B7B78)) }
            }
        )
    }
}

@Composable
fun ComponentEditDialog(
    title: String,
    initialTitle: String = "",
    initialDescription: String = "",
    initialComposition: List<String> = emptyList(),
    initialFunction: String = "",
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, composition: List<String>, function: String) -> Unit
) {
    var compTitle by remember { mutableStateOf(initialTitle) }
    var compDescription by remember { mutableStateOf(initialDescription) }
    var compComposition by remember { mutableStateOf(initialComposition.joinToString("\n")) }
    var compFunction by remember { mutableStateOf(initialFunction) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = { Text(title, color = Color(0xFF004D40)) },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = compTitle,
                        onValueChange = { compTitle = it },
                        label = { Text("Атауы") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00897B),
                            cursorColor = Color(0xFF00897B),
                            focusedLabelColor = Color(0xFF00897B)
                        )
                    )
                }
                item {
                    OutlinedTextField(
                        value = compDescription,
                        onValueChange = { compDescription = it },
                        label = { Text("Сипаттамасы") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 5,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00897B),
                            cursorColor = Color(0xFF00897B),
                            focusedLabelColor = Color(0xFF00897B)
                        )
                    )
                }
                item {
                    OutlinedTextField(
                        value = compComposition,
                        onValueChange = { compComposition = it },
                        label = { Text("Құрамы (әр жол = 1 элемент)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 5,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00897B),
                            cursorColor = Color(0xFF00897B),
                            focusedLabelColor = Color(0xFF00897B)
                        )
                    )
                }
                item {
                    OutlinedTextField(
                        value = compFunction,
                        onValueChange = { compFunction = it },
                        label = { Text("Қызметі") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00897B),
                            cursorColor = Color(0xFF00897B),
                            focusedLabelColor = Color(0xFF00897B)
                        )
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (compTitle.isNotBlank()) {
                        val compositionList = compComposition
                            .split("\n")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                        onConfirm(compTitle.trim(), compDescription.trim(), compositionList, compFunction.trim())
                    }
                }
            ) { Text("Сақтау", color = Color(0xFF00897B), fontWeight = FontWeight.SemiBold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Бас тарту", color = Color(0xFF6B7B78)) }
        }
    )
}
