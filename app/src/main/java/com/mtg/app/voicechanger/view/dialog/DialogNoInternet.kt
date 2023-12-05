package com.mtg.app.voicechanger.view.dialog

import android.content.Context
import android.os.Bundle
import com.mtg.app.voicechanger.base.BaseDialog
import com.mtg.app.voicechanger.databinding.DialogNoInternetBinding
import com.mtg.app.voicechanger.utils.Common

class DialogNoInternet(context: Context) : BaseDialog<DialogNoInternetBinding>(context, DialogNoInternetBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        binding.parent.layoutParams.width = Common.screenWidth * 9 / 10
    }
}