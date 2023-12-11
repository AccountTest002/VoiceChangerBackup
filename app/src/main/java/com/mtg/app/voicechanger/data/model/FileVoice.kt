package com.mtg.app.voicechanger.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import android.os.Parcel
import android.os.Parcelable

@Entity(tableName = "filevoice")
class FileVoice() : Parcelable {
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

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        src = parcel.readInt()
        name = parcel.readString()
        path = parcel.readString()
        duration = parcel.readLong()
        size = parcel.readLong()
        date = parcel.readLong()
        image = parcel.createByteArray()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(src)
        parcel.writeString(name)
        parcel.writeString(path)
        parcel.writeLong(duration)
        parcel.writeLong(size)
        parcel.writeLong(date)
        parcel.writeByteArray(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FileVoice> {
        override fun createFromParcel(parcel: Parcel): FileVoice {
            return FileVoice(parcel)
        }

        override fun newArray(size: Int): Array<FileVoice?> {
            return arrayOfNulls(size)
        }
    }
}

