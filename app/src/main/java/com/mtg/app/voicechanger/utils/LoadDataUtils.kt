package com.mtg.app.voicechanger.utils

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.MediaStore
import com.mtg.app.voicechanger.data.model.AudioFile
import java.io.File

object LoadDataUtils {
    private val audiosStorage: MutableList<AudioFile> = arrayListOf()
    const val LOAD_SUCCESSFUL = "LOAD_SUCCESSFUL"

    private var isLoading = false
    private var isLoaded = false
    fun loadAudio(context: Context, callBack: CallBack) {
        if (!isLoaded) {
            isLoaded = true
            isLoading = true
            Thread {
                val list = arrayListOf<AudioFile>()
                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                var cursor: Cursor? = null
                try {
                    val orderBy = MediaStore.Audio.Media.DATE_TAKEN
                    cursor = context.contentResolver.query(uri, null, null, null, "$orderBy DESC")

                    val colId = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                    val columnData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                    val columnDuration =
                        cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION)
                    val columnArtist =
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.ARTIST)
                    while (cursor.moveToNext()) {
                        val mediaId = cursor.getInt(colId)
                        val path = cursor.getString(columnData)
                        val duration = cursor.getLong(columnDuration)
                        val artist = cursor.getString(columnArtist)
                        if (!File(path).exists()) {
                            continue
                        }
                        val media = AudioFile(
                            path,
                            duration,
                            NumberUtils.formatAsDate(File(path).lastModified()),
                            NumberUtils.formatAsSize(File(path).length())
                        )
                        list.add(media)
                    }
                    audiosStorage.addAll(list)
                    context.sendBroadcast(Intent(LOAD_SUCCESSFUL))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                isLoading = false
            }.start()
        } else {
            if (!isLoading) {
                callBack.onSuccess(audiosStorage)
            }
        }
    }


    fun interface CallBack {
        fun onSuccess(list: List<AudioFile>)
    }
}