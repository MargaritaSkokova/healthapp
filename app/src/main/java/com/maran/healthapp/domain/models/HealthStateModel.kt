package com.maran.healthapp.domain.models

import java.time.LocalDate

data class HealthStateModel(
    val date: LocalDate,
    val lifeState: Int,
    val healthState: Int,
    val moodState: Int,
    val message: String,
)