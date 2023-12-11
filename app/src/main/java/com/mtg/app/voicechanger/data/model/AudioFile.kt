package com.mtg.app.voicechanger.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration

data class AudioFile(val path: String, val duration: Long, var date: String, var size: String)