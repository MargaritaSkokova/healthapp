package com.maran.healthapp.presentation.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(value = [ActivityComponent::class])
object ProviderModule {
    @Provides
    @Singleton
    fun preferencesProvider(preferences: SharedPreferences): PreferencesProvider {
        return PreferencesProvider(preferences)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("default_prefs", Context.MODE_PRIVATE)
    }
}