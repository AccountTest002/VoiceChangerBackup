package com.mtg.app.voicechanger.view.dialog

import android.content.Context
import android.os.Bundle
import com.common.control.base.OnActionCallback
import com.mtg.app.voicechanger.base.BaseDialog
import com.mtg.app.voicechanger.databinding.DialogReadAudioPmsBinding

class DialogReadAudioPms(context: Context) :
    BaseDialog<DialogReadAudioPmsBinding>(context, DialogReadAudioPmsBinding::inflate) {

    private var callback: OnActionCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEvent()
    }

    public fun setCallBack(callback: OnActionCallback): DialogReadAudioPms {
        this.callback = callback
        return this
    }

    private fun addEvent() {
        binding.bt.setOnClickListener {
            callback?.callback("")
        }
    }
}