package com.mtg.app.voicechanger.data.room

import android.content.Context
import androidx.room.Room.databaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//class DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideDatabase(
//        @ApplicationContext context: Context?
//    ): FileVoiceDatabase {
//        return databaseBuilder(
//            context!!,
//            FileVoiceDatabase::class.java,
//            FileVoiceDatabase.DATABASE_NAME
//        )
//            .addMigrations(FileVoiceDatabase.migration_1_to_2)
//            .allowMainThreadQueries()
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideDAO(database: FileVoiceDatabase): FileVoiceDAO? {
//        return database.dao()
//    }
//}
