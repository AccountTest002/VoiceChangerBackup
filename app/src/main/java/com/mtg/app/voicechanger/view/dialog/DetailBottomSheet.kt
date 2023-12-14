package com.mtg.app.voicechanger.view.dialog

import com.mtg.app.voicechanger.base.BaseBottomSheetFragment
import com.mtg.app.voicechanger.data.model.AudioFile
import com.mtg.app.voicechanger.databinding.BottomSheetDetailBinding
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.NumberUtils
import java.io.File

class DetailBottomSheet(private val item :AudioFile, private val callback: Callback): BaseBottomSheetFragment<BottomSheetDetailBinding>(BottomSheetDetailBinding::inflate) {
    override fun initView() {
        var name = File(item.path).name
        var detail = NumberUtils.formatAsTime(item.duration) +"   "+item.size
        binding.main.tvName.text = name
        binding.main.tvDetail.text = detail
    }

    override fun addEvent() {
        binding.llDelete.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_my_works_delete")
            callback.onDelete()
            dismiss()
        }

        binding.llRename.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_my_works_rename")
            callback.onRename()
            dismiss()
        }

        binding.llRingtone.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_my_works_ringphone")
            callback.onRingtone()
            dismiss()
        }

        binding.llShare.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_my_works_share")
            callback.onShare()
            dismiss()
        }
    }

    interface Callback{
        fun onDelete()
        fun onRename()
        fun onRingtone()
        fun onShare()
    }
}