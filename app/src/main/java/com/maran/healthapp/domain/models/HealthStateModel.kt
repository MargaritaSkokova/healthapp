package com.maran.healthapp.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "health_states")
data class HealthStateModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    @ColumnInfo(name = "life_state") val lifeState: Int,
    @ColumnInfo(name = "health_state") val healthState: Int,
    @ColumnInfo(name = "mood_state") val moodState: Int,
    val message: String
) {
    init {
        require(lifeState in 0..4) { "lifeState must be between 0 and 4" }
        require(healthState in 0..4) { "healthState must be between 0 and 4" }
        require(moodState in 0..4) { "moodState must be between 0 and 4" }
    }
}