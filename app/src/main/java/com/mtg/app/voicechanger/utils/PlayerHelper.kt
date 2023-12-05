package com.mtg.app.voicechanger.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import java.io.File
import kotlin.jvm.internal.Intrinsics


class PlayerHelper(context: Context) {
    private val context: Context
    private var myPlay: MediaPlayer? = null
    private var myRecorder: MediaRecorder? = null
    private var outputFile: String

    init {
        Intrinsics.checkNotNullParameter(context, "context")
        this.context = context
        outputFile = ""
    }

    private fun init() {
        val context: Context = context
        if (context.getExternalFilesDir(null) == null) {
            return
        }
        val sb = StringBuilder()
        val externalFilesDir: File? = this.context.getExternalFilesDir(null)
        Intrinsics.checkNotNull(externalFilesDir)
        if (externalFilesDir != null) {
            sb.append(externalFilesDir.absoluteFile.toString())
        }
        sb.append('/')
        sb.append(System.currentTimeMillis())
        sb.append(".3gp")
        outputFile = sb.toString()
        val mediaRecorder = MediaRecorder()
        myRecorder = mediaRecorder
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setAudioSamplingRate(44100)
        mediaRecorder.setOutputFile(outputFile)
    }

    fun playSound(i: Int, z: Boolean) {
        try {
            val create = MediaPlayer.create(context, i)
            myPlay = create
            if (create != null) {
                create.setVolume(1.0f, 1.0f)
                create.isLooping = z
                create.setOnCompletionListener { mediaPlayer ->
                    stopMediaPlayer(mediaPlayer)
                }
                create.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playSound(context: Context, assetPath: String?, loop: Boolean) {
        try {
            val mediaPlayer = MediaPlayer()
            myPlay = mediaPlayer

            // Mở tài nguyên từ assets
            val assetFileDescriptor = context.assets.openFd(assetPath ?: "")
            mediaPlayer.setDataSource(
                assetFileDescriptor.fileDescriptor,
                assetFileDescriptor.startOffset,
                assetFileDescriptor.length
            )

            mediaPlayer.setVolume(1.0f, 1.0f)
            mediaPlayer.isLooping = loop
            mediaPlayer.prepare()
            mediaPlayer.start()
            assetFileDescriptor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopPlay() {
        try {
            val mediaPlayer = myPlay
            if (mediaPlayer != null) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
            myPlay = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pausePlay() {
        try {
            val mediaPlayer = myPlay
            mediaPlayer?.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var currentPosition: Int
        get() {
            val mediaPlayer = myPlay
            if (mediaPlayer != null) {
                return mediaPlayer.currentPosition
            }
            return 0
        }
        set(i) {
            val mediaPlayer = myPlay
            mediaPlayer?.seekTo(i)
        }

    fun resumePlay() {
        try {
            val mediaPlayer = myPlay
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val duration: Int
        get() {
            val mediaPlayer = myPlay
            return mediaPlayer?.duration ?: 0
        }
    val isPlay: Boolean
        get() {
            val mediaPlayer = myPlay
            return mediaPlayer?.isPlaying ?: false
        }

    companion object {
        fun playInt(
            playerHelper: PlayerHelper,
            i: Int,
            z: Boolean,
            i2: Int,
            obj: Any?
        ) {
            var z = z
            if (i2 and 2 != 0) {
                z = false
            }
            playerHelper.playSound(i, z)
        }

        fun stopMediaPlayer(mediaPlayer: MediaPlayer) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }

        fun playString(playerHelper: PlayerHelper, context: Context, str: String?, z: Boolean) {
            playerHelper.playSound(context, str, z)
        }
    }
}