package com.mtg.app.voicechanger.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.data.model.AudioFile
import com.mtg.app.voicechanger.databinding.ActivityAudioChooserBinding
import com.mtg.app.voicechanger.utils.LoadDataUtils
import com.mtg.app.voicechanger.view.adapter.AudioAdapter

class AudioChooserActivity :
    BaseActivity<ActivityAudioChooserBinding>(ActivityAudioChooserBinding::inflate) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, AudioChooserActivity::class.java)
            context.startActivity(starter)
        }
    }

    private val loadFileReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context, p1: Intent?) {
            loadFile()
        }
    }
    private val audioList = arrayListOf<AudioFile>()
    private lateinit var adapter: AudioAdapter
    override fun initView() {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val statusBackground = ContextCompat.getDrawable(this, R.drawable.bg_head_bar)
        window.statusBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(statusBackground)
        binding.ctHeader.background = null

        registerReceiver(loadFileReceiver, IntentFilter(LoadDataUtils.LOAD_SUCCESSFUL))
        loadFile()
        adapter = AudioAdapter(audioList, this)
        binding.rcv.layoutManager = LinearLayoutManager(this)
        binding.rcv.adapter = adapter

    }

    private fun loadFile() {
        LoadDataUtils.loadAudio(this) { list ->
            audioList.clear()
            audioList.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }

    override fun addEvent() {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(loadFileReceiver)

    }
}