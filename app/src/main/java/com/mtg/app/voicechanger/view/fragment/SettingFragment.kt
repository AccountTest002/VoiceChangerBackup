package com.mtg.app.voicechanger.view.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mtg.app.voicechanger.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    companion object {
        private const val URI_POLICY = ""
    }

    private lateinit var binding: FragmentSettingBinding
    private var callback: Callback? = null

    fun newInstance(): SettingFragment {
        val args = Bundle()
        val fragment = SettingFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initView()
        initEvent()
    }

    private fun initView() {
//        val shader1 = ColorUtils.textShader(
//            Color.parseColor("#4B5DFC"),
//            Color.parseColor("#F7277E"),
//            binding.layoutPremium.txtPremium1.textSize
//        )
//        val shader2 = ColorUtils.textShader(
//            Color.parseColor("#4B5DFC"),
//            Color.parseColor("#F7277E"),
//            binding.layoutPremium.txtPremium2.textSize
//        )
//        binding.layoutPremium.txtPremium1.paint.shader = shader1
//        binding.layoutPremium.txtPremium2.paint.shader = shader2
    }

    private fun initEvent() {
//        binding.btnClose.setOnClickListener {
//            callback?.onClose()
//        }

//        binding.btnRate.setOnClickListener {
//            if (RateDialog.isRated(requireContext())) {
//                requireContext().shortToast(R.string.rated)
//            } else {
//                val dialog = RateDialog(
//                    requireContext(),
//                    DialogRateBinding.inflate(requireActivity().layoutInflater)
//                ) {}
//                dialog.show()
//            }
//        }

//        binding.btnShareApp.setOnClickListener {
//            requireContext().shareText(
//                subject = resources.getString(R.string.app_name),
//                text = "https://play.google.com/store/apps/details?id=" + requireActivity().applicationContext.packageName
//            )
//        }

//        binding.btnPolicy.setOnClickListener {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URI_POLICY)))
//            requireActivity().overridePendingTransition(
//                R.anim.anim_right_left_1,
//                R.anim.anim_right_left_2
//            )
//        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onClose()
    }
}