package com.mtg.app.voicechanger.callback

import com.mtg.app.voicechanger.data.model.FileVoice

interface OnItemClickListener {
    fun onItemClick(fileVoice: FileVoice)
}