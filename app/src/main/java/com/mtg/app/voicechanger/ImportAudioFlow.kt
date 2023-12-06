package com.mtg.app.voicechanger

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import com.mtg.app.voicechanger.utils.PermissionUtils


class ImportAudioFlow(private val act: Activity, private val callback: Callback) {
    companion object {
        const val REQUEST_CODE_IMPORT_AUDIO = 489732
    }

    class Output {
        var path: String = ""
    }

    fun start() {
        if (!PermissionUtils.checkReadAudioPms(act)) {
            callback.onNoPms()
        } else {
            importAudio(act)
        }
    }

    private fun importAudio(act: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("audio/*")
        intent.putExtra(
            Intent.EXTRA_MIME_TYPES,
            arrayOf("audio/mp3", "audio/wav")
        )
        act.startActivityForResult(intent, REQUEST_CODE_IMPORT_AUDIO)
    }

    interface Callback {
        fun onNoPms();
    }
}