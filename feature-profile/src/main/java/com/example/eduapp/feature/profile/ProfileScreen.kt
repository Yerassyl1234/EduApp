package com.example.eduapp.feature.profile

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import coil.compose.AsyncImage

import com.example.eduapp.core.domain.model.HelpRequest
import com.example.eduapp.core.domain.model.TestResult

private val GradientColors = listOf(
    Color(0xFF26A69A),
    Color(0xFF00897B),
    Color(0xFF004D40)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

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
            CircularProgressIndicator(color = Color(0xFF00897B))
        }
        return
    }

    val user = uiState.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F3))
    ) {
        // === GRADIENT HEADER WITH AVATAR ===
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(GradientColors),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .statusBarsPadding()
                .padding(top = 16.dp, bottom = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(90.dp)
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
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.padding(18.dp),
                                tint = Color.White
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = user?.name ?: "",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = user?.email ?: "",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = if (user?.role == "teacher") "Мұғалім" else "Оқушы",
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // === MENU ITEMS ===
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            ProfileMenuButton(
                icon = Icons.Default.Settings, title = "Баптаулар",
                subtitle = "Аты-жөні, email, құпия сөз",
                onClick = { showSettingsDialog = true }
            )
            if (user?.role != "teacher") {
                Spacer(modifier = Modifier.height(10.dp))
                ProfileMenuButton(
                    icon = Icons.Default.EmojiEvents, title = "Менің жетістіктерім",
                    subtitle = "${uiState.results.size} нәтиже",
                    onClick = { showAchievementsDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

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
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBA1A1A)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFBA1A1A))
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Шығу", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // === Баптаулар диалог ===
    if (showSettingsDialog) {
        ModalBottomSheet(
            onDismissRequest = { showSettingsDialog = false },
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Баптаулар", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF004D40))
                OutlinedTextField(
                    value = uiState.editName, onValueChange = viewModel::onEditNameChange,
                    label = { Text("Аты-жөні") }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp), singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00897B),
                        cursorColor = Color(0xFF00897B),
                        focusedLabelColor = Color(0xFF00897B)
                    )
                )
                OutlinedTextField(
                    value = uiState.editEmail, onValueChange = viewModel::onEditEmailChange,
                    label = { Text("Email") }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp), singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00897B),
                        cursorColor = Color(0xFF00897B),
                        focusedLabelColor = Color(0xFF00897B)
                    )
                )
                OutlinedTextField(
                    value = uiState.editPassword, onValueChange = viewModel::onEditPasswordChange,
                    label = { Text("Жаңа құпия сөз") }, placeholder = { Text("Өзгерту үшін толтырыңыз") },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                    singleLine = true, visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00897B),
                        cursorColor = Color(0xFF00897B),
                        focusedLabelColor = Color(0xFF00897B)
                    )
                )
                if (uiState.settingsMessage != null) {
                    Text(
                        text = uiState.settingsMessage!!,
                        color = if (uiState.settingsMessage!!.contains("✓")) Color(0xFF00897B)
                        else Color(0xFFBA1A1A), fontSize = 14.sp
                    )
                }
                Button(
                    onClick = viewModel::saveSettings,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp), enabled = !uiState.isSavingSettings,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00897B),
                        disabledContainerColor = Color(0xFF00897B).copy(alpha = 0.5f)
                    )
                ) {
                    if (uiState.isSavingSettings) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    else Text("Сақтау", fontWeight = FontWeight.SemiBold)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // === Жетістіктер диалог ===
    if (showAchievementsDialog && user?.role!="teacher") {
        ModalBottomSheet(
            onDismissRequest = { showAchievementsDialog = false },
            containerColor = Color.White
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Менің жетістіктерім", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF004D40))
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.results.isEmpty()) {
                    Text("Әлі нәтиже жоқ", color = Color(0xFF6B7B78), modifier = Modifier.padding(16.dp))
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
        ModalBottomSheet(
            onDismissRequest = { showHelpDialog = false },
            containerColor = Color.White
        ) {
            if (user?.role == "teacher") {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Оқушылардың хабарламалары", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF004D40))
                    Spacer(modifier = Modifier.height(12.dp))
                    if (uiState.helpRequests.isEmpty()) {
                        Text(
                            "Хабарлама жоқ",
                            color = Color(0xFF6B7B78),
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.helpRequests, key = { it.id }) { request ->
                                HelpRequestCard(request = request)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Көмек", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF004D40))
                    Text("Мұғалімге сұрақ жіберу:", fontWeight = FontWeight.Medium, color = Color(0xFF004D40))
                    OutlinedTextField(
                        value = uiState.helpText, onValueChange = viewModel::onHelpTextChange,
                        label = { Text("Сұрағыңызды жазыңыз...") },
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00897B),
                            cursorColor = Color(0xFF00897B),
                            focusedLabelColor = Color(0xFF00897B)
                        )
                    )
                    if (uiState.helpSent) {
                        Text("Сұрағыңыз жіберілді! ✓", color = Color(0xFF00897B), fontWeight = FontWeight.Medium)
                    }
                    Button(
                        onClick = viewModel::sendHelpRequest,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        enabled = !uiState.isSendingHelp && uiState.helpText.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00897B),
                            disabledContainerColor = Color(0xFF00897B).copy(alpha = 0.4f)
                        )
                    ) {
                        if (uiState.isSendingHelp) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        else {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Жіберу", fontWeight = FontWeight.SemiBold)
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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF00897B).copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Color(0xFF00897B), modifier = Modifier.size(22.dp))
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF1C1B1F))
                Text(text = subtitle, fontSize = 13.sp, color = Color(0xFF6B7B78))
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF6B7B78))
        }
    }
}

@Composable
fun ResultCard(result: TestResult) {
    val percentage = if (result.totalQuestions > 0) (result.score * 100) / result.totalQuestions else 0
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8F7))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = result.testTitle, fontWeight = FontWeight.Medium, color = Color(0xFF1C1B1F))
                Text(text = "$percentage% дұрыс", fontSize = 13.sp, color = Color(0xFF6B7B78))
            }
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF00897B).copy(alpha = 0.12f)
            ) {
                Text(
                    "${result.score}/${result.totalQuestions}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00897B),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun HelpRequestCard(request: HelpRequest) {
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }
    val dateStr = if (request.sentAt > 0) dateFormat.format(Date(request.sentAt)) else ""

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8F7))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
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
                        tint = Color(0xFF00897B)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = request.userName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF1C1B1F)
                    )
                }
                Text(
                    text = dateStr,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7B78)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = request.message,
                fontSize = 14.sp,
                color = Color(0xFF1C1B1F)
            )
        }
    }
}
