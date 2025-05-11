package com.maran.healthapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.maran.healthapp.data.dao.HealthStateDao
import com.maran.healthapp.domain.models.ArticleModel
import com.maran.healthapp.domain.models.HealthStateModel

@Database(
    entities = [HealthStateModel::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun healthStateDao(): HealthStateDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS health_states (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        date timestamp NOT NULL,
                        life_state INTEGER NOT NULL,
                        health_state INTEGER NOT NULL,
                        mood_state INTEGER NOT NULL,
                        message TEXT
                    )
                """)
            }
        }
    }
}