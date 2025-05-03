package com.maran.healthapp.domain.repositories

import com.maran.healthapp.domain.models.HealthStateModel
import javax.inject.Inject

class HealthStateRepository @Inject constructor(){
    suspend fun getAllStates(): List<HealthStateModel> {
        // todo
        return listOf()
    }

    suspend fun saveState(state: HealthStateModel) {
        // todo
    }
}