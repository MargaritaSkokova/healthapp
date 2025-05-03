package com.maran.healthapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.maran.healthapp.presentation.article.ArticleScreen
import com.maran.healthapp.presentation.health.HealthScreen

@Composable
fun NavigationStack(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.HealthScreen.route
    ) {
        composable(Screen.HealthScreen.route) { HealthScreen(navController = navController) }
        composable(Screen.ArticleScreen.route) { ArticleScreen(navController = navController) }
    }
}