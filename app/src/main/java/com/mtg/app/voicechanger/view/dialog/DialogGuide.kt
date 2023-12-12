package com.mtg.app.voicechanger.view.dialog

import android.content.Context
import android.os.Bundle
import com.mtg.app.voicechanger.base.BaseDialog
import com.mtg.app.voicechanger.databinding.DialogGuideBinding
import com.mtg.app.voicechanger.databinding.DialogNoInternetBinding
import com.mtg.app.voicechanger.utils.Common

class DialogGuide(context: Context) : BaseDialog<DialogGuideBinding>(context, DialogGuideBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvGotIt.setOnClickListener {
            dismiss()
        }
    }
}