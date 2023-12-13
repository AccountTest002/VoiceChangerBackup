package com.mtg.app.voicechanger.repository

import androidx.lifecycle.MutableLiveData
import com.mtg.app.voicechanger.data.model.FileVoice
import com.mtg.app.voicechanger.data.room.FileVoiceDAO
import javax.inject.Inject

class FileVoiceRepository @Inject constructor(private val dao: FileVoiceDAO) {
    val fileVoices: MutableLiveData<List<FileVoice>> = MutableLiveData()
    val fileVoicesBG: MutableLiveData<List<FileVoice>> = MutableLiveData()
    val fileVideos: MutableLiveData<List<FileVoice>> = MutableLiveData()
    val fileVideosBG: MutableLiveData<List<FileVoice>> = MutableLiveData()

    init {
        fileVoices.postValue(dao.allVoice)
        fileVoicesBG.postValue(dao.allVoice)
        fileVideos.postValue(dao.allVideo)
        fileVideosBG.postValue(dao.allVideo)
    }

    fun check(path: String): FileVoice {
        return dao.check(path)
    }

    fun insert(fileVoice: FileVoice?) {
        dao.insert(fileVoice)
    }

    fun update(fileVoice: FileVoice?) {
        dao.update(fileVoice)
    }

    fun delete(fileVoice: FileVoice?) {
        dao.delete(fileVoice)
    }
    fun getAllAudio(): List<FileVoice>{
        return dao.allVoice
    }

    fun updatePath(path: String, newPath: String) {
        dao.updatePath(path, newPath)
    }

    fun deleteByPath(path: String) {
        dao.deleteByPath(path)
        fileVoices.postValue(getAllAudio())
    }
}
