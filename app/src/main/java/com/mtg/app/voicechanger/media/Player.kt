package com.mtg.app.voicechanger.media

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
//import com.mtg.app.voicechanger.VoiceChangerApp.Companion.TAG
import com.mtg.app.voicechanger.interfaces.PlayerListener
import com.mtg.app.voicechanger.utils.constant.Constants.TAG
import java.io.IOException

class Player : PlayerListener {
    private var player: MediaPlayer?

    init {
        player = MediaPlayer()
    }

    val isPlaying: Boolean
        get() = player!!.isPlaying
    val currentPosition: Int
        get() = player!!.currentPosition
    val duration: Int
        get() = player!!.duration

    fun seekTo(i: Long) {
        player!!.seekTo(i.toInt())
    }

    override fun setNewPath(path: String?) {
        Log.d(TAG, "Player: setNewPath $path")
        if (player == null) {
            Log.d(TAG, "Player: player null")
            return
        }
        try {
            player!!.reset()
            player!!.setDataSource(path)
            player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player!!.isLooping = true
            player!!.prepare()
        } catch (e: IOException) {
            Log.d(TAG, "Player: setNewPath error " + e.message)
        }
    }


    override fun start() {
        if (player == null) {
            Log.d(TAG, "Player: player null")
            return
        }
        player!!.start()
    }

    override fun pause() {
        if (player == null) {
            Log.d(TAG, "Player: player null")
            return
        }
        if (player!!.isPlaying) {
            player!!.pause()
        }
    }

    override fun resume() {
        if (player == null) {
            Log.d(TAG, "Player: player null")
            return
        }
        if (!player!!.isPlaying) {
            player!!.start()
        }
    }

    override fun stop() {
        if (player == null) {
            Log.d(TAG, "Player: player null")
            return
        }
        if (player!!.isPlaying) {
            player!!.stop()
        }
    }

    override fun release() {
        if (player == null) {
            Log.d(TAG, "Player: player null")
            return
        }
        if (player!!.isPlaying) {
            player!!.stop()
        }
        player!!.release()
        player = null
        Log.d(TAG, "Player: release")
    }
}
