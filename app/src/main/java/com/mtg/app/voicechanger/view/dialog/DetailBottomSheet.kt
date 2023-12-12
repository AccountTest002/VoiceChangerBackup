package com.mtg.app.voicechanger.view.dialog

import com.mtg.app.voicechanger.base.BaseBottomSheetFragment
import com.mtg.app.voicechanger.data.model.AudioFile
import com.mtg.app.voicechanger.databinding.BottomSheetDetailBinding
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
            callback.onDelete()
            dismiss()
        }

        binding.llRename.setOnClickListener {
            callback.onRename()
            dismiss()
        }

        binding.llRingtone.setOnClickListener {
            callback.onRingtone()
            dismiss()
        }

        binding.llShare.setOnClickListener {
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