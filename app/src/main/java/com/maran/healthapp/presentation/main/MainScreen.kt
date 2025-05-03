package com.maran.healthapp.presentation.main

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.maran.healthapp.presentation.navigation.NavigationStack
import com.maran.healthapp.presentation.navigation.Screen

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavigationStack(navController, modifier)
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(Screen.HealthScreen, Screen.ArticleScreen)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = {
                    if (screen.icon != null && screen.labelId != null) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = stringResource(screen.labelId),
                            tint = if (currentRoute == screen.route) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                label = {
                    if (screen.labelId != null) {
                        Text(
                            text = stringResource(screen.labelId),
                            style = MaterialTheme.typography.titleSmall,
                            color = if (currentRoute == screen.route) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            )
        }
    }
}