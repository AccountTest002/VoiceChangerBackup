package com.mtg.app.voicechanger.utils

import com.mtg.app.voicechanger.data.model.AudioFile

class ListAudioManager(private val currentList: MutableList<AudioFile>, private val callback: CallBack) {
    private val orgList = arrayListOf<AudioFile>()

    init {
        orgList.addAll(currentList)
    }

    fun filterList(text: String) {
        currentList.clear()
        currentList.addAll(orgList.filter { it.path.contains(text) })
        callback.onEmpty(currentList.isEmpty())
    }

    interface CallBack {
        fun onEmpty(it: Boolean)
    }
}