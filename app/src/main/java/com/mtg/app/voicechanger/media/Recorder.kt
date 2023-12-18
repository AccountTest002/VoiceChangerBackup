package com.mtg.app.voicechanger.media

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mtg.app.voicechanger.interfaces.RecorderListener
import com.mtg.app.voicechanger.utils.FileUtils
import com.mtg.app.voicechanger.utils.constant.Constants.TAG
import java.io.IOException

class Recorder(context: Context) : RecorderListener {
    private var recorder: MediaRecorder? = null
    var path: String? = null
        private set
    private var startTime: Long = 0
    private var isRecording: Boolean = false

    init {
        path = context.let { FileUtils.getTempRecordingFilePath(it) }
        Log.d(TAG, "Recorder: create $path")
        initializeMediaRecorder()
    }

    private fun initializeMediaRecorder() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioChannels(2)
            setAudioEncodingBitRate(320000)
            setAudioSamplingRate(44100)
            setOutputFile(path)
        }
    }

    override fun start() {
        if (recorder == null) {
            Log.d(TAG, "Recorder: start null")
            return
        }
        if (!isRecording) {
            try {
                recorder!!.prepare()
                recorder!!.start()
                startTime = System.currentTimeMillis()
                isRecording = true
                Log.d(TAG, "Recorder: start")
            } catch (e: IOException) {
                Log.d(TAG, "Recorder: error start - " + e.message)
            }
        }
    }

    override fun stop() {
        if (recorder == null) {
            Log.d(TAG, "Recorder: stop null")
            return
        }
        if (isRecording) {
            recorder!!.stop()
            recorder!!.release()
            startTime = 0
            isRecording = false
            initializeMediaRecorder()
            Log.d(TAG, "Recorder: stop")
        }
    }

    override fun release() {
        if (recorder == null) {
            Log.d(TAG, "Recorder: release null")
            return
        }
        if (isRecording) {
            recorder!!.stop()
        }
        recorder!!.release()
        recorder = null
        path = null
        startTime = 0
        isRecording = false
        Log.d(TAG, "Recorder: release")
    }

    val currentTime: Long
        get() = System.currentTimeMillis() - startTime
    val tickDuration: Int
        get() = (BUFFER_SIZE.toDouble() * 500 / BYTE_RATE).toInt()
    val maxAmplitude: Int
        get() = if (recorder != null) {
            recorder!!.maxAmplitude
        } else 0

    companion object {
        private const val SAMPLING_RATE_IN_HZ = 44100
        private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
        private const val CHANNEL_COUNT = 1
        private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT
        private const val BIT_PER_SAMPLE = 16
        private const val BYTE_RATE =
            (BIT_PER_SAMPLE * SAMPLING_RATE_IN_HZ * CHANNEL_COUNT / 8).toLong()
        private val BUFFER_SIZE =
            AudioRecord.getMinBufferSize(SAMPLING_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT)
    }
}
