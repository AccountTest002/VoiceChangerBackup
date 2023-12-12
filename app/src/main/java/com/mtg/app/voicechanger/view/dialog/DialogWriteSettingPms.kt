package com.mtg.app.voicechanger.view.dialog

import android.content.Context
import android.os.Bundle
import com.common.control.base.OnActionCallback
import com.mtg.app.voicechanger.base.BaseDialog
import com.mtg.app.voicechanger.databinding.DialogReadAudioPmsBinding
import com.mtg.app.voicechanger.databinding.DialogWriteSettingsPmsBinding

class DialogWriteSettingPms(context: Context) :
    BaseDialog<DialogWriteSettingsPmsBinding>(context, DialogWriteSettingsPmsBinding::inflate) {

    private var callback: OnActionCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEvent()
    }

    public fun setCallBack(callback: OnActionCallback): DialogWriteSettingPms {
        this.callback = callback
        return this
    }

    private fun addEvent() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnAllow.setOnClickListener {
            callback?.callback("")
            dismiss()
        }
    }
}