package com.example.a3edhomework.horses.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.a3edhomework.horses.presentation.viewModel.FilterViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onApplyFilters: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: FilterViewModel = koinViewModel()
    val filtersState by viewModel.filtersState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filters") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearFilters() }) {
                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = {
                        viewModel.saveFilters()
                        onApplyFilters()
                        onBackClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Применить фильтры")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = filtersState.nameQuery,
                onValueChange = { viewModel.updateNameQuery(it) },
                label = { Text("Имя лошади") },
                placeholder = { Text("Например: Deep Impact") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = filtersState.minEarnings,
                onValueChange = { viewModel.updateMinEarnings(it) },
                label = { Text("Мин. призовые (¥)") },
                placeholder = { Text("Например: 1000000000") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = filtersState.owner,
                onValueChange = { viewModel.updateOwner(it) },
                label = { Text("Владелец") },
                placeholder = { Text("Например: Sunday Racing") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Текущие фильтры:", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Имя: ${filtersState.nameQuery.ifEmpty { "не задано" }}")
                    Text("Мин. призовые: ${filtersState.minEarnings.ifEmpty { "не задано" }}")
                    Text("Владелец: ${filtersState.owner.ifEmpty { "не задано" }}")
                }
            }
        }
    }
}