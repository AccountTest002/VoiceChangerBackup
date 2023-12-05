package com.mtg.app.voicechanger.repository

import androidx.lifecycle.MutableLiveData
import com.mtg.app.voicechanger.data.model.FileVoice
import com.mtg.app.voicechanger.data.room.FileVoiceDAO
import javax.inject.Inject

class FileVoiceRepository @Inject constructor(private val dao: FileVoiceDAO) {
    val fileVoices: MutableLiveData<List<FileVoice?>?>
        get() {
            val data = MutableLiveData<List<FileVoice?>?>()
            data.setValue(dao.allVoice)
            return data
        }
    val fileVoicesBG: MutableLiveData<List<FileVoice?>?>
        get() {
            val data = MutableLiveData<List<FileVoice?>?>()
            data.postValue(dao.allVoice)
            return data
        }
    val fileVideos: MutableLiveData<List<FileVoice?>?>
        get() {
            val data = MutableLiveData<List<FileVoice?>?>()
            data.setValue(dao.allVideo)
            return data
        }
    val fileVideosBG: MutableLiveData<List<FileVoice?>?>
        get() {
            val data = MutableLiveData<List<FileVoice?>?>()
            data.postValue(dao.allVideo)
            return data
        }

    fun check(path: String?): FileVoice? {
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
}
