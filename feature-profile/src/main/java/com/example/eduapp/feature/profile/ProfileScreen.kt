package com.example.eduapp.feature.profile

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

import com.example.eduapp.core.domain.model.HelpRequest
import com.example.eduapp.core.domain.model.TestResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAchievementsDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updatePhoto(it, context) }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val user = uiState.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { photoPickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.photoUri != null) {
                AsyncImage(
                    model = uiState.photoUri,
                    contentDescription = "Аватар",
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(20.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Фото өзгерту",
                    modifier = Modifier.padding(4.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = user?.name ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = user?.email ?: "", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = if (user?.role == "teacher") "Мұғалім" else "Оқушы",
            fontSize = 14.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(32.dp))

        ProfileMenuButton(
            icon = Icons.Default.Settings, title = "Баптаулар",
            subtitle = "Аты-жөні, email, құпия сөз",
            onClick = { showSettingsDialog = true }
        )
        if (user?.role != "teacher") {
            Spacer(modifier = Modifier.height(12.dp))
            ProfileMenuButton(
                icon = Icons.Default.EmojiEvents, title = "Менің жетістіктерім",
                subtitle = "${uiState.results.size} нәтиже",
                onClick = { showAchievementsDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // === ЗДЕСЬ ГЛАВНОЕ ИЗМЕНЕНИЕ ===
        if (user?.role == "teacher") {
            ProfileMenuButton(
                icon = Icons.Default.Email, title = "Оқушылардың хабарламалары",
                subtitle = "${uiState.helpRequests.size} хабарлама",
                onClick = { showHelpDialog = true }
            )
        } else {
            ProfileMenuButton(
                icon = Icons.Default.Help, title = "Көмек",
                subtitle = "Мұғалімге сұрақ жіберу",
                onClick = { showHelpDialog = true }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = { viewModel.signOut(); onSignOut() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Шығу", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    // === Баптаулар диалог ===
    if (showSettingsDialog) {
        ModalBottomSheet(onDismissRequest = { showSettingsDialog = false }) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Баптаулар", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = uiState.editName, onValueChange = viewModel::onEditNameChange,
                    label = { Text("Аты-жөні") }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp), singleLine = true
                )
                OutlinedTextField(
                    value = uiState.editEmail, onValueChange = viewModel::onEditEmailChange,
                    label = { Text("Email") }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp), singleLine = true
                )
                OutlinedTextField(
                    value = uiState.editPassword, onValueChange = viewModel::onEditPasswordChange,
                    label = { Text("Жаңа құпия сөз") }, placeholder = { Text("Өзгерту үшін толтырыңыз") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                    singleLine = true, visualTransformation = PasswordVisualTransformation()
                )
                if (uiState.settingsMessage != null) {
                    Text(
                        text = uiState.settingsMessage!!,
                        color = if (uiState.settingsMessage!!.contains("✓")) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error, fontSize = 14.sp
                    )
                }
                Button(
                    onClick = viewModel::saveSettings,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp), enabled = !uiState.isSavingSettings
                ) {
                    if (uiState.isSavingSettings) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                    else Text("Сақтау")
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // === Жетістіктер диалог ===
    if (showAchievementsDialog && user?.role!="teacher") {
        ModalBottomSheet(onDismissRequest = { showAchievementsDialog = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Менің жетістіктерім", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.results.isEmpty()) {
                    Text("Әлі нәтиже жоқ", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(16.dp))
                } else {
                    uiState.results.forEach { result ->
                        ResultCard(result = result)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // === Көмек / Хабарламалар диалог ===
    if (showHelpDialog) {
        ModalBottomSheet(onDismissRequest = { showHelpDialog = false }) {
            if (user?.role == "teacher") {
                // Учитель — список сообщений от учеников
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Оқушылардың хабарламалары", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    if (uiState.helpRequests.isEmpty()) {
                        Text(
                            "Хабарлама жоқ",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.helpRequests) { request ->
                                HelpRequestCard(request = request)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            } else {
                // Ученик — форма отправки
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Көмек", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Мұғалімге сұрақ жіберу:", fontWeight = FontWeight.Medium)
                    OutlinedTextField(
                        value = uiState.helpText, onValueChange = viewModel::onHelpTextChange,
                        label = { Text("Сұрағыңызды жазыңыз...") },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), minLines = 3
                    )
                    if (uiState.helpSent) {
                        Text("Сұрағыңыз жіберілді! ✓", color = MaterialTheme.colorScheme.primary)
                    }
                    Button(
                        onClick = viewModel::sendHelpRequest,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !uiState.isSendingHelp && uiState.helpText.isNotBlank()
                    ) {
                        if (uiState.isSendingHelp) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                        else {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Жіберу")
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileMenuButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String, subtitle: String, onClick: () -> Unit
) {
    Card(
        onClick = onClick, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = subtitle, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ResultCard(result: TestResult) {
    val percentage = if (result.totalQuestions > 0) (result.score * 100) / result.totalQuestions else 0
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = result.testTitle, fontWeight = FontWeight.Medium)
                Text(text = "$percentage% дұрыс", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("${result.score}/${result.totalQuestions}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun HelpRequestCard(request: HelpRequest) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val dateStr = if (request.sentAt > 0) dateFormat.format(Date(request.sentAt)) else ""

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = request.userName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
                Text(
                    text = dateStr,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = request.message,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
