package com.mtg.app.voicechanger.view.activity

import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.common.control.utils.AppUtils
import com.mtg.app.voicechanger.MyApplication
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.data.model.FileVoice
import com.mtg.app.voicechanger.databinding.ActivitySavedBinding
import com.mtg.app.voicechanger.media.Player
import com.mtg.app.voicechanger.utils.FileUtils
import com.mtg.app.voicechanger.utils.NumberUtils
import com.mtg.app.voicechanger.utils.PermissionUtils.requestWriteSetting
import com.mtg.app.voicechanger.utils.constant.Constants.STUDENT_EXTRA
import com.mtg.app.voicechanger.view.dialog.RenameDialog
import com.mtg.app.voicechanger.viewmodel.FileVoiceViewModel
import com.mtg.app.voicechanger.viewmodel.VoiceViewModelFactory
import java.io.File

class SavedActivity :
    BaseActivity<ActivitySavedBinding>(ActivitySavedBinding::inflate) {
    private var player: Player? = null
    private var fileVoice: FileVoice? = null
    private val model: FileVoiceViewModel by viewModels {
        VoiceViewModelFactory((application as MyApplication).repository)
    }

    override fun initView() {
        fullScreen()
        dataItem()
    }

    override fun addEvent() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this, RecordActivity::class.java))
        }

        binding.llShare.setOnClickListener {
            AppUtils.getInstance().shareFile(this, fileVoice?.path?.let { it1 -> File(it1) })
        }

        binding.llRing.setOnClickListener {
            setRingAudio()
        }

        binding.llRename.setOnClickListener {
            renameAudio()
        }

        binding.llDelete.setOnClickListener {
            deleteAudio()
        }

        binding.tvPlay.setOnClickListener {
            deleteAudio()
        }
    }

//    fun onPlayPauseClick() {
//
//        if (fileVoice.isPlaying) {
//            // Đang phát, dừng lại và đổi văn bản thành "Play"
//            // Thực hiện logic dừng audio ở đây
//            binding.tvPlay.text = "Play"
//            fileVoice.isPlaying = false
//        } else {
//            // Không phát, bắt đầu phát và đổi văn bản thành "Pause"
//            // Thực hiện logic bắt đầu phát audio ở đây
//            binding.tvPlay.text = "Pause"
//            fileVoice.isPlaying = true
//        }
//    }

//    private fun startPlayer() {
//        player?.start()
//        binding.layoutPlayer.btnPauseOrResume.setImageResource(R.drawable.ic_pause)
//        updateTime()
//    }
//
//    private fun stopPlayer() {
//        player?.stop()
//        binding.layoutPlayer.btnPauseOrResume.setImageResource(R.drawable.ic_play)
//        handler.removeCallbacksAndMessages(null)
//    }
//
//    private fun pausePlayer() {
//        player?.pause()
//        binding.layoutPlayer.btnPauseOrResume.setImageResource(R.drawable.ic_play)
//        handler.removeCallbacksAndMessages(null)
//    }

    private fun setRingAudio() {
        if (Settings.System.canWrite(this)) {
            if (FileUtils.setAsRingtoneOrNotification(
                    this,
                    fileVoice?.path,
                    RingtoneManager.TYPE_RINGTONE
                )
            ) {
                Toast.makeText(this, R.string.setting_success, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.request_write_setting, Toast.LENGTH_LONG).show();
            requestWriteSetting(this)
        }
    }

    private fun renameAudio() {
        val dialogRename = fileVoice?.let { RenameDialog(it) }
        dialogRename?.setOnSaveListener { newPath ->
            fileVoice?.path = newPath
            fileVoice?.name = FileUtils.getName(newPath)
            fileVoice?.let { model.update(it) }
        }
        dialogRename?.show(supportFragmentManager, "RenameDialog")
    }

    private fun deleteAudio(){
        fileVoice?.path?.let { FileUtils.deleteFile(this, it) }
        fileVoice?.let { model.delete(it) }
    }

    private fun dataItem() {
        fileVoice = intent.getParcelableExtra(STUDENT_EXTRA)

        if (fileVoice != null) {
            binding.tvTitle.text = fileVoice!!.name
            binding.tvTime.text = NumberUtils.formatAsTime(fileVoice!!.duration)
            binding.tvSize.text = fileVoice!!.size.let { NumberUtils.formatAsSize(it) }
        }

    }


    private fun fullScreen() {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val statusBackground = ContextCompat.getDrawable(this, R.drawable.bg_head_bar)
        window.statusBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(statusBackground)
    }

}