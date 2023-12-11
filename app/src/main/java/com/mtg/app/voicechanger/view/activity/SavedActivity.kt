package com.mtg.app.voicechanger.view.activity

import android.content.Intent
import android.graphics.Color
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.data.model.FileVoice
import com.mtg.app.voicechanger.databinding.ActivitySavedBinding
import com.mtg.app.voicechanger.utils.NumberUtils
import com.mtg.app.voicechanger.utils.constant.Constants.STUDENT_EXTRA
import java.io.File

class SavedActivity :
    BaseActivity<ActivitySavedBinding>(ActivitySavedBinding::inflate) {

    override fun initView() {
        fullScreen()
        dataItem()


    }

    override fun addEvent() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher
        }

        binding.btnHome.setOnClickListener {
            startActivity(Intent(this, RecordActivity::class.java))
        }
    }

    private fun dataItem() {
        val fileVoice = intent.getParcelableExtra<FileVoice>(STUDENT_EXTRA)

        if (fileVoice != null) {
            binding.tvTitle.text = fileVoice.name
//            binding.tvTime.text = fileVoice.date.toString()
            binding.tvTime.text = NumberUtils.formatAsTime(fileVoice.duration)
//            binding.tvSize.text = fileVoice.size.toString()
            binding.tvSize.text = fileVoice.size.let { NumberUtils.formatAsSize(it) }
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