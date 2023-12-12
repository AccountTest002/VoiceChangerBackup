package com.mtg.app.voicechanger.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mtg.app.voicechanger.data.model.FileVoice

@Dao
interface FileVoiceDAO {
    @Insert
    fun insert(fileVoice: FileVoice?)

    @Update
    fun update(fileVoice: FileVoice?)

    @Delete
    fun delete(fileVoice: FileVoice?)

    @get:Query("SELECT * FROM filevoice WHERE path LIKE '%mp3'")
    val allVoice: List<FileVoice>

    @get:Query("SELECT * FROM filevoice WHERE path LIKE '%mp4'")
    val allVideo: List<FileVoice>

    @Query("SELECT * FROM filevoice WHERE path= :path")
    fun check(path: String): FileVoice
    @Query("UPDATE filevoice SET path = :newPath WHERE path = :path")
    fun updatePath(path: String, newPath: String)

    @Query("DELETE FROM filevoice WHERE path = :path")
    fun deleteByPath(path: String)
}
