package com.mtg.app.voicechanger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mtg.app.voicechanger.data.model.FileVoice
import com.mtg.app.voicechanger.repository.FileVoiceRepository
import com.mtg.app.voicechanger.utils.constant.Constants.TAG


class FileVoiceViewModel(private val repository: FileVoiceRepository) :
    ViewModel() {
    private val fileVoices: MutableLiveData<List<FileVoice>> = repository.fileVoices
    private val fileVideos: MutableLiveData<List<FileVoice>> = repository.fileVideos

    fun getFileVoices(): LiveData<List<FileVoice>> {
        fileVoices.postValue(repository.getAllAudio())
        return fileVoices
    }

    fun getFileVideos(): LiveData<List<FileVoice>> {
        return fileVideos
    }

    fun check(path: String?): FileVoice? {
        return path?.let { repository.check(it) }
    }

    fun insert(fileVoice: FileVoice) {
        Log.d(TAG, "FileVoiceViewModel: insert " + fileVoice.path)
        repository.insert(fileVoice)
        fileVoices.value = repository.fileVoices.value
        fileVideos.value = repository.fileVideos.value
    }

    fun insertBG(fileVoice: FileVoice) {
        Log.d(TAG, "FileVoiceViewModel: insertBG " + fileVoice.path)
        repository.insert(fileVoice)
        fileVoices.postValue(repository.fileVoicesBG.value)
        fileVideos.postValue(repository.fileVideosBG.value)
    }

    fun insertVideoBG(fileVoice: FileVoice) {
        Log.d(TAG, "FileVoiceViewModel: insertVideoBG " + fileVoice.path)
        repository.insert(fileVoice)
        fileVideos.postValue(repository.fileVideosBG.value)
    }

    fun update(fileVoice: FileVoice) {
        Log.d(TAG, "FileVoiceViewModel: update " + fileVoice.path)
        repository.update(fileVoice)
        fileVoices.value = repository.fileVoices.value
        fileVideos.value = repository.fileVideos.value
    }

    fun updateBG(fileVoice: FileVoice) {
        Log.d(TAG, "FileVoiceViewModel: updateBG " + fileVoice.path)
        repository.update(fileVoice)
        fileVoices.postValue(repository.fileVoicesBG.value)
        fileVideos.postValue(repository.fileVideosBG.value)
    }

    fun delete(fileVoice: FileVoice) {
        Log.d(TAG, "FileVoiceViewModel: delete " + fileVoice.path)
        repository.delete(fileVoice)
        fileVoices.value = repository.fileVoices.value
        fileVideos.value = repository.fileVideos.value
    }

    fun deleteBG(fileVoice: FileVoice) {
        Log.d(TAG, "FileVoiceViewModel: deleteBG " + fileVoice.path)
        repository.delete(fileVoice)
        fileVoices.postValue(repository.fileVoicesBG.value)
        fileVideos.postValue(repository.fileVideosBG.value)
    }

    fun updatePath(path: String, newPath: String) {
        Log.d(TAG, "FileVoiceViewModel: update " + path)
        repository.updatePath(path, newPath)
    }

    fun deleteByPath(path: String) {
        Log.d(TAG, "FileVoiceViewModel: delete " + path)
        repository.deleteByPath(path)
    }
}