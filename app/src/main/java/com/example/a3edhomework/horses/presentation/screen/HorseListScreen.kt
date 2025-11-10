package com.example.a3edhomework.horses.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.a3edhomework.HorseDetails
import com.example.a3edhomework.horses.presentation.MockData
import com.example.a3edhomework.horses.presentation.domain.useCase.GetHorsesUseCase
import com.example.a3edhomework.horses.presentation.model.HorseListState
import com.example.a3edhomework.horses.presentation.model.HorseUIModel
import com.example.a3edhomework.horses.presentation.repository.HorseRepository
import com.example.a3edhomework.horses.presentation.viewModel.HorsesListViewModel
import com.example.a3edhomework.navigation.Route
import com.example.a3edhomework.navigation.TopLevelBackStack
import androidx.lifecycle.ViewModelProvider
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import com.example.a3edhomework.FilterSettings
import com.example.a3edhomework.horses.presentation.domain.cache.BadgeCache
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorseListScreen(topLevelBackStack: TopLevelBackStack<Route>) {
    val viewModel: HorsesListViewModel = koinViewModel()
    val badgeCache: BadgeCache = koinInject()
    val state = viewModel.viewState.collectAsState().value

    val hasActiveFilters by remember {
        derivedStateOf { badgeCache.getHasActiveFilters() }
    }

    Scaffold(
        floatingActionButton = {
            Box {
                FloatingActionButton(
                    onClick = { topLevelBackStack.add(FilterSettings) }
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Фильтры")
                }
                if (hasActiveFilters) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .drawWithCache {
                                onDrawWithContent {
                                    drawCircle(
                                        color = Color.Red,
                                        radius = size.minDimension / 2
                                    )
                                }
                            }
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (state) {
                is HorseListState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Text("Загрузка лошадей...", modifier = Modifier.padding(16.dp))
                    }
                }
                is HorseListState.Success -> {
                    HorseListContent(
                        horses = state.horses,
                        topLevelBackStack = topLevelBackStack
                    )
                }
                is HorseListState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Ошибка: Сервер долго не отвечает.")
                        Button(onClick = { viewModel.loadHorses() }) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HorseListContent(
    horses: List<HorseUIModel>,
    topLevelBackStack: TopLevelBackStack<Route>
) {
    LazyColumn {
        items(horses.size) { index ->
            val horse = horses[index]
            HorseListItem(horse, onHorseClick = {
                topLevelBackStack.add(HorseDetails(horse.id))
            })
        }
    }
}

@Composable
fun HorseListItem(horse: HorseUIModel, onHorseClick: (HorseUIModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onHorseClick(horse) }
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = horse.imageUrl,
                contentDescription = horse.name,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = horse.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "Призовые: ${horse.earnings}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}