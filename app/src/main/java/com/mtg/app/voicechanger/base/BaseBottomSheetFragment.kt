package com.mtg.app.voicechanger.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetFragment<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) : BottomSheetDialogFragment() {
    val binding: B by lazy { bindingFactory(layoutInflater) }
    abstract fun initView()

    abstract fun addEvent()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        addEvent()
    }
}