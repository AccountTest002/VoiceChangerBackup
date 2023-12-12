package com.mtg.app.voicechanger.view.dialog

import android.content.Context
import android.os.Bundle
import com.mtg.app.voicechanger.base.BaseDialog
import com.mtg.app.voicechanger.databinding.DialogRenameBinding
import com.mtg.app.voicechanger.utils.FileUtils

class RenameDialog(context: Context, private val path: String, private val callback: Callback) :
    BaseDialog<DialogRenameBinding>(context, DialogRenameBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        addEvent()
    }

    private fun addEvent() {
        binding.btnSave.setOnClickListener {
            val newPath = path.let { it1 ->
                FileUtils.renameFile(
                    context,
                    it1,
                    binding.edtName.text.toString().trim()
                )
            }
            if (newPath != null) {
                callback.onRename(newPath)
                dismiss()
            }
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initView() {
        binding.edtName.setText(path.let { FileUtils.getName(it) })
    }

    interface Callback {
        fun onRename(newPath: String)
    }
}