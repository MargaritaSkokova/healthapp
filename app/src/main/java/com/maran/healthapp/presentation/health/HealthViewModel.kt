package com.maran.healthapp.presentation.health

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Sick
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.maran.healthapp.data.dao.HealthStateDao
import com.maran.healthapp.domain.models.HealthStateModel
import com.maran.healthapp.domain.repositories.HealthStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val repository: HealthStateRepository,
    private val healthStateDao: HealthStateDao
) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val weeklyStatistics = mutableStateOf("")

    val lifeList: List<Pair<ImageVector, String>> = listOf(
        Pair(Icons.Default.Block, "Exhausted"),
        Pair(Icons.Default.SentimentDissatisfied, "Tired"),
        Pair(Icons.Default.SentimentNeutral, "Okay"),
        Pair(Icons.Default.SentimentSatisfied, "Good"),
        Pair(Icons.Default.SentimentVerySatisfied, "Great")
    )

    val healthList: List<Pair<ImageVector, String>> = listOf(
        Pair(Icons.Default.Sick, "Very Sick"),
        Pair(Icons.Default.MoodBad, "Unwell"),
        Pair(Icons.Default.Mood, "Average"),
        Pair(Icons.Default.FavoriteBorder, "Healthy"),
        Pair(Icons.Default.Favorite, "Excellent")
    )

    val moodList: List<Pair<ImageVector, String>> = listOf(
        Pair(Icons.Default.SentimentVeryDissatisfied, "Awful"),
        Pair(Icons.Default.SentimentDissatisfied, "Down"),
        Pair(Icons.Default.SentimentNeutral, "Meh"),
        Pair(Icons.Default.SentimentSatisfied, "Happy"),
        Pair(Icons.Default.SentimentVerySatisfied, "Ecstatic")
    )
    val chosenLifeIndex = mutableIntStateOf(0)
    val chosenHealthIndex = mutableIntStateOf(0)
    val chosenMoodIndex = mutableIntStateOf(0)
    val wasChecked = mutableStateOf(hasCheckedInToday())
    val text = mutableStateOf("")

    @SuppressLint("NewApi")
    fun hasCheckedInToday(): Boolean {
        val dateSignedStr = preferences.getString("date_checked", null) ?: return false
        val dateSigned = LocalDateTime.parse(dateSignedStr)
        return dateSigned.toLocalDate().dayOfYear == LocalDateTime.now().toLocalDate().dayOfYear
    }

    @SuppressLint("NewApi")
    fun saveDaily() {
        with(preferences.edit()) {
            putString("date_checked", LocalDateTime.now().toString())
            apply()
        }

        coroutineScope.launch {
            val healthModel = HealthStateModel(
                date = LocalDate.now(),
                lifeState = chosenLifeIndex.intValue,
                healthState = chosenHealthIndex.intValue,
                moodState = chosenMoodIndex.intValue,
                message = text.value,
            )
            repository.saveState(
                healthModel
            )
            healthStateDao.insert(healthModel)
        }

        wasChecked.value = true
    }

    @SuppressLint("NewApi")
    fun getWeeklyStatistics() {
        coroutineScope.launch {
            val statistics = repository.getAllStates()
            val oneWeekAgo = LocalDate.now().minusWeeks(1)
            val recentStates =
                statistics.filter { it.date.isAfter(oneWeekAgo) || it.date.isEqual(oneWeekAgo) }
            var sum = 0
            recentStates.forEach {
                sum += it.healthState + it.lifeState + it.moodState
            }
            val percentage: Int
            if (recentStates.isEmpty()) {
                percentage = 100
            } else {
                percentage = sum / recentStates.size
            }

            weeklyStatistics.value =
                "For the past weeks you health satisfaction was $percentage, today: life = ${chosenLifeIndex.intValue}, health = ${chosenHealthIndex.intValue}, mood = ${chosenMoodIndex.intValue}, ${text.value}"
        }
    }
}