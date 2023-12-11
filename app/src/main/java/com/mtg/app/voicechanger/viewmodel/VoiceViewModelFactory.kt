package com.mtg.app.voicechanger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mtg.app.voicechanger.repository.FileVoiceRepository

class VoiceViewModelFactory(private val repository: FileVoiceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FileVoiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FileVoiceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}