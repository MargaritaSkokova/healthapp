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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    private val healthStateDao: HealthStateDao
) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val isLoading = mutableStateOf(true)
    val weeklyStatistics = mutableStateOf("")
    val weeklyIconStatistics = mutableStateOf(Icons.Default.Block)
    val weeklySuggestion = mutableStateOf("")
    val weeklyLifeSuggestion = mutableStateOf("")
    val weeklyHealthSuggestion = mutableStateOf("")
    val weeklyMoodSuggestion = mutableStateOf("")
    val weeklyLife = mutableStateOf(listOf<Pair<ImageVector?, String>>())
    val weeklyHealth = mutableStateOf(listOf<Pair<ImageVector?, String>>())
    val weeklyMood = mutableStateOf(listOf<Pair<ImageVector?, String>>())

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

    init {
        getWeeklyStatistics()
    }

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

            healthStateDao.insert(healthModel)
        }

        wasChecked.value = true

        getWeeklyStatistics()
    }

    @SuppressLint("NewApi")
    fun getWeeklyStatistics() {
        isLoading.value = true
        coroutineScope.launch {
            healthStateDao.getAll().collect { allStates ->
                val today = LocalDate.now()
                val mondayOfThisWeek = today.with(DayOfWeek.MONDAY)
                val weekDates = (0..6).map { mondayOfThisWeek.plusDays(it.toLong()) }
                val recentStates = allStates.filter { state ->
                    val date = state.date
                    !date.isBefore(mondayOfThisWeek) && !date.isAfter(today)
                }

                val dateToState = recentStates.associateBy { it.date }
                val completeWeek = weekDates.map { date ->
                    dateToState[date]
                }

                val sum = recentStates.sumOf { it.healthState + it.lifeState + it.moodState }
                val percentage = if (recentStates.isEmpty()) {
                    100
                } else {
                    ((sum.toFloat() / (recentStates.size * 12)) * 100).toInt()
                }

                val healthSum = recentStates.sumOf { it.healthState }
                val healthPercentage = if (recentStates.isEmpty()) {
                    100f
                } else {
                    healthSum.toFloat() / recentStates.size
                }

                val lifeSum = recentStates.sumOf { it.lifeState }
                val lifePercentage = if (recentStates.isEmpty()) {
                    100f
                } else {
                    lifeSum.toFloat() / recentStates.size
                }

                val moodSum = recentStates.sumOf { it.moodState }
                val moodPercentage = if (recentStates.isEmpty()) {
                    100f
                } else {
                    moodSum.toFloat() / recentStates.size
                }

                weeklyStatistics.value =
                    "For the past weeks your health satisfaction was $percentage%"
                weeklyIconStatistics.value = getIcon(percentage)
                weeklySuggestion.value = getSuggestion(percentage)
                weeklyLife.value = completeWeek.mapIndexed { index, it ->
                    Pair(
                        if (it == null) null else lifeList[it.lifeState].first, weekDates[index].format(
                            DateTimeFormatter.ofPattern("dd.MM")
                        )
                    )
                }
                weeklyLifeSuggestion.value = getLifeSuggestion(lifePercentage)
                weeklyHealth.value = completeWeek.mapIndexed { index, it ->
                    Pair(
                        if (it == null) null else healthList[it.healthState].first, weekDates[index].format(
                            DateTimeFormatter.ofPattern("dd.MM")
                        )
                    )

                }
                weeklyHealthSuggestion.value = getHealthSuggestion(healthPercentage)
                weeklyMood.value = completeWeek.mapIndexed { index, it ->
                    Pair(
                        if (it == null) null else moodList[it.moodState].first, weekDates[index].format(
                            DateTimeFormatter.ofPattern("dd.MM")
                        )
                    )
                }
                weeklyMoodSuggestion.value = getMoodSuggestion(moodPercentage)
                isLoading.value = false
            }
        }
    }

    fun getIcon(percentage: Int): ImageVector {
        return when (percentage) {
            in 80..100 -> Icons.Default.SentimentVerySatisfied
            in 60..<80 -> Icons.Default.SentimentSatisfied
            in 40..<60 -> Icons.Default.SentimentNeutral
            in 20..<40 -> Icons.Default.SentimentDissatisfied
            in 0..<20 -> Icons.Default.SentimentVeryDissatisfied
            else -> Icons.Default.SentimentVerySatisfied
        }
    }

    fun getSuggestion(percentage: Int): String {
        return when (percentage) {
            in 80..100 -> "You're doing great! Keep up the good work and maintain your routine."
            in 60..<80 -> "You're doing well, but there's room for improvement. Consider small positive changes."
            in 40..<60 -> "Things seem a bit unstable. Try identifying stressors and take time to rest."
            in 20..<40 -> "It might be a tough week. Talk to someone you trust or try relaxing activities."
            in 0..<20 -> "Consider prioritizing your well-being. Seeking support or taking a break may help."
            else -> "No data available for recommendation."
        }
    }

    fun getLifeSuggestion(average: Float): String {
        return when (average) {
            in 3.0..4.0 -> "You're doing well in life! Keep up your good habits and routines."
            in 2.0..<3.0 -> "Life's been a bit up and down. Try adding more balance and joy into your routine."
            in 1.0..<2.0 -> "It might be time to reflect and reconnect with things that bring you meaning."
            in 0.0..<1.0 -> "Consider slowing down and focusing on what truly matters. Support might help."
            else -> "No data available for recommendation."
        }
    }

    fun getHealthSuggestion(average: Float): String {
        return when (average) {
            in 3.0..4.0 -> "Your health is on track! Keep maintaining your physical and mental wellness."
            in 2.0..<3.0 -> "You're doing okay, but regular activity or rest might help improve your health."
            in 1.0..<2.0 -> "Try to be mindful of your body's needs—small routines like walking or sleep can help."
            in 0.0..<1.0 -> "Your health may need more attention. Don’t hesitate to seek help or slow down."
            else -> "No data available for recommendation."
        }
    }

    fun getMoodSuggestion(average: Float): String {
        return when (average) {
            in 3.0..4.0 -> "You're in a good mental space! Keep nurturing your emotional well-being."
            in 2.0..<3.0 -> "Your mood has its ups and downs. Stay connected and take moments to recharge."
            in 1.0..<2.0 -> "It's okay to feel low. Mindful practices or talking to someone might help."
            in 0.0..<1.0 -> "Take care of yourself — reaching out for support can be the first step forward."
            else -> "No data available for recommendation."
        }
    }
}