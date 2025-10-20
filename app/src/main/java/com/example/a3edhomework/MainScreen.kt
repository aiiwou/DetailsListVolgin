package com.example.a3edhomework

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.a3edhomework.horses.presentation.model.HorseUIModel
import com.example.a3edhomework.horses.presentation.screen.HorseDetailScreen
import com.example.a3edhomework.horses.presentation.screen.HorseListScreen
import com.example.a3edhomework.navigation.Route
import com.example.a3edhomework.navigation.TopLevelBackStack

interface TopLevelRoute: Route {
    val icon: ImageVector
}

data object Animals: TopLevelRoute {
    override val icon = Icons.Default.Favorite
}
data object Horses: TopLevelRoute {
    override val icon = Icons.Default.FavoriteBorder
}

data class HorseDetails(val horseId: String) : Route

@Composable
fun MainScreen() {
    val topLevelBackStack = remember { TopLevelBackStack<Route>(Horses) }

    Scaffold(bottomBar = {
        NavigationBar {
            listOf(Horses, Animals).forEach { route ->
                NavigationBarItem(
                    icon = { Icon(imageVector = route.icon, contentDescription = null) },
                    selected = topLevelBackStack.topLevelKey == route,
                    onClick = {
                        topLevelBackStack.addTopLevel(route)
                    }
                )
            }
        }
    }) { padding ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            modifier = Modifier.padding(padding),
            entryProvider = entryProvider {
                entry<Horses> {
                    HorseListScreen(topLevelBackStack)
                }
                entry<Animals> {
                    ContentGreen("Animals") { }
                }
                entry<HorseDetails> { detail ->
                    // ФИКС: передаем только horseId
                    HorseDetailScreen(
                        horseId = detail.horseId,
                        onBackClick = { topLevelBackStack.removeLast() }
                    )
                }
            }
        )
    }
}

@Composable
fun ContentGreen(text: String, content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {
        Text(text)
        content()
    }
}