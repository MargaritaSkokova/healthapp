package com.maran.healthapp.presentation.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.maran.healthapp.data.AppDatabase
import com.maran.healthapp.data.api.ArticleRepositoryImpl
import com.maran.healthapp.data.api.NewsApi
import com.maran.healthapp.data.dao.HealthStateDao
import com.maran.healthapp.domain.repositories.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "app-database"
        )
            .addMigrations(
                AppDatabase.MIGRATION_1_2
            )
            .build()
    }

    @Provides
    fun provideHealthStateDao(database: AppDatabase): HealthStateDao {
        return database.healthStateDao()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        /*val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }*/

        val okHttpClient = OkHttpClient.Builder()
            //.addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)

    @Provides
    fun bindNewsRepository(impl: ArticleRepositoryImpl): ArticleRepository {
        return impl
    }
}