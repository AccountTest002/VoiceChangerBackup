package com.mtg.app.voicechanger.view.dialog

import android.content.Context
import android.os.Bundle
import com.mtg.app.voicechanger.base.BaseDialog
import com.mtg.app.voicechanger.databinding.DialogDeleteBinding
import com.mtg.app.voicechanger.databinding.DialogRingtoneBinding

class SetRingtoneDialog(context: Context, private val callback: Callback) :
    BaseDialog<DialogRingtoneBinding>(context, DialogRingtoneBinding::inflate){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        addEvent()
    }

    private fun addEvent() {

    }

    private fun initView() {
        binding.btnOk.setOnClickListener {
            callback.onOk()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    interface Callback{
        fun onOk()
    }
}