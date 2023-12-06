//package com.mtg.app.voicechanger.base
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.databinding.ViewDataBinding
//import com.documentmaster.documentscan.OnActionCallback
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.mtg.app.voicechanger.R
//
//abstract class BaseBottomSheet<B : ViewDataBinding?> : BottomSheetDialogFragment() {
//    protected var binding: B? = null
//    private var baseContext: Context? = null
//    protected var callback: OnActionCallback? = null
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        this.baseContext = context
//    }
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
//        binding!!.root.background =
//            requireActivity().getDrawable(R.drawable.arch_corner_24)
//        return binding!!.root
//    }
//
//    @SuppressLint("ResourceAsColor")
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initView()
//        addEvents()
//    }
//
//    override fun getTheme(): Int {
//        return R.style.CustomBottomSheetDialogTheme
//    }
//
//    protected abstract fun initView()
//    protected abstract fun addEvents()
//    protected abstract val layoutId: Int
//    fun setCallBack(callback: OnActionCallback?) {
//        this.callback = callback
//    }
//}
