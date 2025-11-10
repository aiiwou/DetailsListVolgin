package com.example.a3edhomework.horses.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.a3edhomework.horses.presentation.model.HorseUIModel
import com.example.a3edhomework.horses.presentation.repository.HorseRepository
import com.example.a3edhomework.horses.presentation.viewModel.HorsesListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseDetailScreen(
    horseId: String,
    onBackClick: () -> Unit
) {
    val repository: HorseRepository = koinInject()
    val horsesListViewModel: HorsesListViewModel = koinViewModel()

    var horse by remember { mutableStateOf<HorseUIModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val favoriteState by horsesListViewModel.favoriteState.collectAsState()
    val isFavorite = favoriteState[horseId] ?: false

    LaunchedEffect(horseId) {
        isLoading = true
        try {
            horse = withContext(Dispatchers.IO) {
                repository.getHorseById(horseId)
            }

            horsesListViewModel.checkFavorite(horseId)
        } catch (e: Exception) {
            error = "Ошибка загрузки: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(horse?.name ?: "Загрузка...") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (horse != null) {
                        IconButton(
                            onClick = {
                                horse?.let { horsesListViewModel.toggleFavorite(it) }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.Add,
                                contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Загрузка данных...")
                }
            }
            error != null -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Ошибка: $error")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        error = null
                        isLoading = true
                    }) {
                        Text("Повторить")
                    }
                }
            }
            horse != null -> {
                HorseDetailContent(
                    horse = horse!!,
                    padding = padding,
                    isFavorite = isFavorite
                )
            }
        }
    }
}

@Composable
fun HorseDetailContent(
    horse: HorseUIModel,
    padding: PaddingValues,
    isFavorite: Boolean = false
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = horse.imageUrl,
            contentDescription = horse.name,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.Add,
                contentDescription = null,
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isFavorite) "В избранном" else "Не в избранном",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Имя: ${horse.name}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Владелец: ${horse.owner}", style = MaterialTheme.typography.bodyMedium)
        horse.majorWin?.let { Text(text = "Главная победа: $it", style = MaterialTheme.typography.bodyMedium) }
        Text(text = "Призовые: ${horse.earnings}", style = MaterialTheme.typography.bodyMedium)
    }
}