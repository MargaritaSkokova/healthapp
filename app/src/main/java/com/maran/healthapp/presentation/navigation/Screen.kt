package com.maran.healthapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.ui.graphics.vector.ImageVector
import com.maran.healthapp.R

sealed class Screen(val route: String, val labelId: Int?, val icon: ImageVector?) {
    object HealthScreen : Screen("health_screen", R.string.health_screen, Icons.Filled.HealthAndSafety)
    object ArticleScreen : Screen("article_screen", R.string.article_screen, Icons.Filled.Article)
    object ReaderScreen : Screen("readerScreen", null, null)
}