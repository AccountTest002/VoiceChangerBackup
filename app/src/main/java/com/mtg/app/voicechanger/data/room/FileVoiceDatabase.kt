package com.mtg.app.voicechanger.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mtg.app.voicechanger.data.model.FileVoice
import kotlinx.coroutines.CoroutineScope

@Database(entities = [FileVoice::class], version = 3)
abstract class FileVoiceDatabase : RoomDatabase() {
    abstract fun dao(): FileVoiceDAO

    companion object {
        private var INSTANCE: FileVoiceDatabase? = null

        fun getDatabase(context: Context): FileVoiceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FileVoiceDatabase::class.java,
                    FileVoiceDatabase.DATABASE_NAME
                )
                    .addMigrations(migration_1_to_2)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        const val DATABASE_NAME = "filevoice.db"

        var migration_1_to_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE filevoice ADD COLUMN image TEXT")
            }
        }
    }
}