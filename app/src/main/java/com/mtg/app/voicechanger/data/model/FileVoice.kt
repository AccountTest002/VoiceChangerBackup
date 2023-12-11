package com.mtg.app.voicechanger.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filevoice")
class FileVoice {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var src = 0
    var name: String? = null
    var path: String? = null
    var duration: Long = 0
    var size: Long = 0
    var date: Long = 0

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null
}
