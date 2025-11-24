package com.example.a3edhomework.horses.presentation.screen

import java.util.Calendar
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.a3edhomework.horses.presentation.viewModel.ProfileViewModel
import androidx.core.net.toUri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.a3edhomework.horses.presentation.model.UserProfile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.example.a3edhomework.horses.presentation.receivers.NotificationScheduler
import org.koin.androidx.compose.koinViewModel
import java.util.regex.Pattern

@SuppressLint("ScheduleExactAlarm")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onDone: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val profile by viewModel.profileState.collectAsState()
    val context = LocalContext.current

    var fullName by remember { mutableStateOf(profile.fullName) }
    var resumeUrl by remember { mutableStateOf(profile.resumeUrl) }
    var classTime by remember { mutableStateOf(profile.classTime) }
    var avatarUri by remember { mutableStateOf(if (profile.avatarUri.isBlank()) null else Uri.parse(profile.avatarUri)) }

    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var showExactAlarmDialog by remember { mutableStateOf(false) }

    val isClassTimeValid = remember(classTime) {
        classTime.isEmpty() || isValidTimeFormat(classTime)
    }
    val isFormValid = fullName.isNotBlank() && isClassTimeValid

    val timePickerState = rememberTimePickerState(
        initialHour = parseHourFromTime(classTime),
        initialMinute = parseMinuteFromTime(classTime)
    )

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { avatarUri = it }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) tempCameraUri?.let { avatarUri = it }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            tempCameraUri = createImageUri(context)
            tempCameraUri?.let { takePictureLauncher.launch(it) }
        }
    }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) pickImageLauncher.launch("image/*")
    }

    fun handleGallery() {
        showImageSourceDialog = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickImageLauncher.launch("image/*")
        } else {
            storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    fun handleCamera() {
        showImageSourceDialog = false
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    fun canScheduleExactAlarms(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            return alarmManager?.canScheduleExactAlarms() ?: false
        }
        return true
    }

    fun saveProfileAndMaybeSchedule() {
        val newProfile = UserProfile(
            fullName = fullName,
            avatarUri = avatarUri?.toString().orEmpty(),
            resumeUrl = resumeUrl,
            classTime = classTime
        )

        viewModel.save(newProfile)

        if (classTime.isNotBlank() && isValidTimeFormat(classTime)) {
            if (canScheduleExactAlarms(context)) {
                NotificationScheduler().scheduleClassNotification(context, fullName, classTime)
                onDone()
            } else {
                showExactAlarmDialog = true
            }
        } else {
            onDone()
        }
    }

    fun saveProfileWithoutScheduling() {
        val newProfile = UserProfile(
            fullName = fullName,
            avatarUri = avatarUri?.toString().orEmpty(),
            resumeUrl = resumeUrl,
            classTime = classTime
        )
        viewModel.save(newProfile)
        onDone()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование профиля") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = { saveProfileAndMaybeSchedule() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = isFormValid
                ) {
                    Text("Готово")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUri),
                        contentDescription = "Аватар",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable { showImageSourceDialog = true }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { showImageSourceDialog = true }) {
                        Text("Изменить фото")
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ФИО", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("ФИО") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = fullName.isBlank()
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Время любимой пары", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = classTime,
                        onValueChange = { classTime = it },
                        label = { Text("Время (HH:mm)") },
                        trailingIcon = {
                            IconButton(onClick = { showTimePickerDialog = true }) {
                                Icon(Icons.Filled.Add, null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isClassTimeValid
                    )
                    Text(
                        text = "Вы получите уведомление в указанное время",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Резюме", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = resumeUrl,
                        onValueChange = { resumeUrl = it },
                        label = { Text("Ссылка") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (showImageSourceDialog) {
        ImageSourceDialog(
            onDismiss = { showImageSourceDialog = false },
            onGallerySelected = { handleGallery() },
            onCameraSelected = { handleCamera() }
        )
    }

    if (showTimePickerDialog) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onDismiss = { showTimePickerDialog = false },
            onConfirm = {
                classTime = "%02d:%02d".format(timePickerState.hour, timePickerState.minute)
                showTimePickerDialog = false
            }
        )
    }

    if (showExactAlarmDialog) {
        AlertDialog(
            onDismissRequest = { showExactAlarmDialog = false },
            title = { Text("Точные будильники отключены") },
            text = {
                Text("Чтобы получить уведомление точно в указанный момент, разрешите приложению устанавливать точные будильники в настройках. " +
                        "Без этого будет сохранён профиль, но уведомление может прийти с задержкой или не прийти вовсе.")
            },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                    saveProfileWithoutScheduling()
                    showExactAlarmDialog = false
                }) {
                    Text("Открыть настройки")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    saveProfileWithoutScheduling()
                    showExactAlarmDialog = false
                }) {
                    Text("Сохранить без уведомления")
                }
            }
        )
    }
}

private fun isValidTimeFormat(time: String): Boolean {
    val pattern = Pattern.compile("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")
    return pattern.matcher(time).matches()
}

private fun parseHourFromTime(time: String): Int =
    if (isValidTimeFormat(time)) time.split(":")[0].toInt()
    else Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

private fun parseMinuteFromTime(time: String): Int =
    if (isValidTimeFormat(time)) time.split(":")[1].toInt()
    else Calendar.getInstance().get(Calendar.MINUTE)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    timePickerState: TimePickerState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите время") },
        text = { TimePicker(state = timePickerState) },
        confirmButton = {
            Button(onClick = onConfirm) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@Composable
private fun ImageSourceDialog(
    onDismiss: () -> Unit,
    onGallerySelected: () -> Unit,
    onCameraSelected: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Источник фотографии") },
        text = { Text("Выберите источник фотографии") },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onGallerySelected) { Text("Галерея") }
                Button(onClick = onCameraSelected) { Text("Камера") }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

private fun createImageUri(context: Context): Uri? {
    return try {
        val cv = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DISPLAY_NAME, "profile_${System.currentTimeMillis()}.jpg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
            }
        }
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
    } catch (_: Exception) {
        null
    }
}