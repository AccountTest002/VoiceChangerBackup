package com.mtg.app.voicechanger

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mtg.app.voicechanger.view.viewmodel.MainViewModel

class ViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(
    application
) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}