package com.mtg.app.voicechanger.view.dialog

import android.content.Context
import android.os.Bundle
import com.mtg.app.voicechanger.base.BaseDialog
import com.mtg.app.voicechanger.databinding.DialogDeleteBinding

class DeleteDialog(context: Context, private val callback: Callback) :
    BaseDialog<DialogDeleteBinding>(context, DialogDeleteBinding::inflate){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        addEvent()
    }

    private fun addEvent() {

    }

    private fun initView() {
        binding.btnDelete.setOnClickListener {
            callback.onDelete()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    interface Callback{
        fun onDelete()
    }
}