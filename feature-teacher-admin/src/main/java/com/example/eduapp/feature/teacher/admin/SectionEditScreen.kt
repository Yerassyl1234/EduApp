package com.example.eduapp.feature.teacher.admin

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

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
        topBar = {
            TopAppBar(
                title = { Text(uiState.category?.title ?: "Секция") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Артқа")
                    }
                },
                actions = {
                    IconButton(onClick = { showEditTitleDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Атауын өңдеу")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddComponentDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Подкатегория қосу")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (uiState.components.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Подкатегориялар әлі жоқ",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Жаңа подкатегория қосу үшін + батырмасын басыңыз",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.components) { component ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Article,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = component.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { showEditComponentDialog = component }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Өңдеу",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(onClick = { showDeleteComponentDialog = component.id }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Жою",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        if (component.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = component.description,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 3
                            )
                        }

                        if (component.composition.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Құрамы:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            component.composition.forEach { item ->
                                Text(
                                    text = "• $item",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }

                        if (component.function.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Қызметі: ${component.function}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // === AR көру батырмасы (placeholder) ===
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { /* AR функционалы әлі іске асырылмаған */ },
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
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
            title = { Text("Подкатегорияны жою") },
            text = { Text("Бұл подкатегорияны жойғыңыз келе ме?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteComponent(componentId)
                        showDeleteComponentDialog = null
                    }
                ) { Text("Жою", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteComponentDialog = null }) { Text("Бас тарту") }
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
            title = { Text("Секция атауын өңдеу") },
            text = {
                OutlinedTextField(
                    value = editedTitle,
                    onValueChange = { editedTitle = it },
                    label = { Text("Жаңа атау") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
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
                ) { Text("Сақтау") }
            },
            dismissButton = {
                TextButton(onClick = { showEditTitleDialog = false }) { Text("Бас тарту") }
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
        title = { Text(title) },
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
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = compDescription,
                        onValueChange = { compDescription = it },
                        label = { Text("Сипаттамасы") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 5
                    )
                }
                item {
                    OutlinedTextField(
                        value = compComposition,
                        onValueChange = { compComposition = it },
                        label = { Text("Құрамы (әр жол = 1 элемент)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 5
                    )
                }
                item {
                    OutlinedTextField(
                        value = compFunction,
                        onValueChange = { compFunction = it },
                        label = { Text("Қызметі") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4
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
            ) { Text("Сақтау") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Бас тарту") }
        }
    )
}
