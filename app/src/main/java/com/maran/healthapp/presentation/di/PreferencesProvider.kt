package com.maran.healthapp.presentation.di

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesProvider @Inject constructor(val sharedPreferences: SharedPreferences)