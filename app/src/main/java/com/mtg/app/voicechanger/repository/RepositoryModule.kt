package com.mtg.app.voicechanger.repository

import com.mtg.app.voicechanger.data.room.FileVoiceDAO
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Inject
    @Singleton
    fun provideRepository(dao: FileVoiceDAO?): FileVoiceRepository? {
        return dao?.let { FileVoiceRepository(it) }
    }
}
