package com.maran.healthapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.maran.healthapp.domain.models.HealthStateModel
import kotlinx.coroutines.flow.Flow
import java.util.Date
import androidx.room.Update as Update

@Dao
interface HealthStateDao {
    @Insert
    suspend fun insert(healthState: HealthStateModel)

    @Update
    suspend fun update(healthState: HealthStateModel)

    @Delete
    suspend fun delete(healthState: HealthStateModel)

    @Query("SELECT * FROM health_states ORDER BY date DESC")
    fun getAll(): Flow<List<HealthStateModel>>
}