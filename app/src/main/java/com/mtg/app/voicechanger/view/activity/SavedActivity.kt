package com.mtg.app.voicechanger.view.activity

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.common.control.base.OnActionCallback
import com.common.control.utils.AppUtils
import com.mtg.app.voicechanger.MyApplication
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.data.model.FileVoice
import com.mtg.app.voicechanger.databinding.ActivitySavedBinding
import com.mtg.app.voicechanger.media.Player
import com.mtg.app.voicechanger.utils.FileUtils
import com.mtg.app.voicechanger.utils.LoadDataUtils
import com.mtg.app.voicechanger.utils.NumberUtils
import com.mtg.app.voicechanger.utils.PermissionUtils
import com.mtg.app.voicechanger.utils.PermissionUtils.requestWriteSetting
import com.mtg.app.voicechanger.utils.constant.Constants.STUDENT_EXTRA
import com.mtg.app.voicechanger.view.dialog.DeleteDialog
import com.mtg.app.voicechanger.view.dialog.DialogReadAudioPms
import com.mtg.app.voicechanger.view.dialog.RenameDialog
import com.mtg.app.voicechanger.view.dialog.SetRingtoneDialog
import com.mtg.app.voicechanger.viewmodel.FileVoiceViewModel
import com.mtg.app.voicechanger.viewmodel.VoiceViewModelFactory
import java.io.File
import java.io.IOException

class SavedActivity :
    BaseActivity<ActivitySavedBinding>(ActivitySavedBinding::inflate) {
    private var fileVoice: FileVoice? = null
    private var isPlaying: Boolean = true
    private val model: FileVoiceViewModel by viewModels {
        VoiceViewModelFactory((application as MyApplication).repository)
    }
    private var mediaPlayer: MediaPlayer? = null

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
            onPlayClick()
        }
    }

    private fun onPlayClick() {

        isPlaying = if (isPlaying) {
            fileVoice?.path?.let { startPlayer(it) }
            false
        } else {
            stopPlayer()
            true
        }
    }

    private fun startPlayer(filePath: String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            try {
                mediaPlayer?.setDataSource(filePath)
                mediaPlayer?.prepare()
                mediaPlayer?.setOnCompletionListener { mp ->
                    binding.tvPlay.text = getString(R.string.play)
                    isPlaying = true
                    mediaPlayer = null
                    mp.release()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mediaPlayer?.start()
            binding.tvPlay.text = getString(R.string.pause)
        }
    }

    private fun stopPlayer() {
        try {
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null
            binding.tvPlay.text = getString(R.string.play)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    private fun setRingAudio() {
        if (Settings.System.canWrite(this)) {
            SetRingtoneDialog(this, object: SetRingtoneDialog.Callback{
                override fun onOk() {
                    if (FileUtils.setAsRingtoneOrNotification(
                            this@SavedActivity,
                            fileVoice?.path,
                            RingtoneManager.TYPE_RINGTONE
                        ) && FileUtils.setAsRingtoneOrNotification(
                            this@SavedActivity,
                            fileVoice?.path,
                            RingtoneManager.TYPE_NOTIFICATION
                        )
                    ) {
                        Toast.makeText(this@SavedActivity, R.string.setting_success, Toast.LENGTH_LONG).show();
                    }
                }
            }).show()

        } else {
            DialogReadAudioPms(this).setCallBack(OnActionCallback { key, data ->
                requestWriteSetting(this)
            }).show()
        }
    }

    private fun renameAudio() {
        fileVoice?.path?.let {
            RenameDialog(this, it, object : RenameDialog.Callback {
                override fun onRename(newPath: String) {
                    model.updatePath(it, newPath)
                    fileVoice?.path = newPath
                    fileVoice?.name = FileUtils.getName(newPath)
                    binding.tvTitle.text = fileVoice!!.name
                }
            }).show()
        }
    }

    private fun deleteAudio() {
        fileVoice?.path?.let {
            DeleteDialog(this@SavedActivity, object: DeleteDialog.Callback{
                override fun onDelete() {
                    LoadDataUtils.remove(it)
                    FileUtils.deleteFile(this@SavedActivity, it)
                    model.deleteByPath(it)
                    finish()
                }
            }).show()
        }

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