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
        callback.onNextScreen()
    }

    interface Callback {
        fun onNoPms()
        fun onNextScreen()
    }
}