package com.example.a3edhomework

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import com.example.a3edhomework.horses.presentation.screen.EditProfileScreen
import com.example.a3edhomework.horses.presentation.screen.FavoritesScreen
import com.example.a3edhomework.horses.presentation.screen.FilterScreen
import com.example.a3edhomework.horses.presentation.screen.HorseDetailScreen
import com.example.a3edhomework.horses.presentation.screen.HorseListScreen
import com.example.a3edhomework.horses.presentation.screen.ProfileScreen
import com.example.a3edhomework.navigation.Route
import com.example.a3edhomework.navigation.TopLevelBackStack

interface TopLevelRoute: Route {
    val icon: ImageVector
}
data object FilterSettings : Route
data object Animals: TopLevelRoute {
    override val icon = Icons.Default.Favorite
}
data object Horses: TopLevelRoute {
    override val icon = Icons.Default.FavoriteBorder
}

data object Favorites: TopLevelRoute {  // ← ДОБАВЬ эту вкладку
    override val icon = Icons.Default.Star
}
data class HorseDetails(val horseId: String) : Route

data object Profile : TopLevelRoute { // ← новая вкладка
    override val icon = Icons.Default.Person
}

data object EditProfile : Route

@Composable
fun MainScreen() {
    val topLevelBackStack = remember { TopLevelBackStack<Route>(Horses) }

    Scaffold(bottomBar = {
        NavigationBar {
            listOf(Horses, Animals, Favorites, Profile).forEach { route ->
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
                entry<Favorites> {
                    FavoritesScreen(
                        onBackClick = { topLevelBackStack.removeLast() },
                        topLevelBackStack
                    )
                }
                entry<HorseDetails> { detail ->
                    HorseDetailScreen(
                        horseId = detail.horseId,
                        onBackClick = { topLevelBackStack.removeLast() }
                    )
                }
                entry<FilterSettings> {
                    FilterScreen(
                        onApplyFilters = {
                        },
                        onBackClick = { topLevelBackStack.removeLast() }
                    )
                }

                entry<Profile> {
                    ProfileScreen(
                        onEdit = { topLevelBackStack.add(EditProfile) },
                        onBackClick = { topLevelBackStack.removeLast() }
                    )
                }
                entry<EditProfile> {
                    EditProfileScreen(
                        onDone = { topLevelBackStack.removeLast() },
                        onBackClick = { topLevelBackStack.removeLast() },
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