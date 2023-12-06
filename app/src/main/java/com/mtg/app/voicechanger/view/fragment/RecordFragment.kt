package com.mtg.app.voicechanger.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.mtg.app.voicechanger.BuildConfig
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseFragment
import com.mtg.app.voicechanger.databinding.FragmentOnboadingBinding
import com.mtg.app.voicechanger.databinding.FragmentRecordBinding
import com.mtg.app.voicechanger.utils.ActionUtils
import com.mtg.app.voicechanger.utils.CommonUtils
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.FirebaseUtils
import com.mtg.app.voicechanger.utils.hide
import com.mtg.app.voicechanger.view.activity.LanguageActivity
import com.mtg.app.voicechanger.view.activity.PolicyWebViewActivity
import com.proxglobal.proxads.adsv2.ads.ProxAds
import com.proxglobal.proxads.adsv2.callback.AdsCallback
import com.proxglobal.purchase.ProxPurchase

class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {
//    private lateinit var dialog: MoreOptionDialog
    private var callback: Callback? = null

    private val handler = Handler(Looper.getMainLooper())
    private var runnableAnimation: Runnable? = null

    fun newInstance(): RecordFragment {
        val args = Bundle()
        val fragment = RecordFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        System.gc()
    }

    override fun initView() {

        setupDrawerNavigation()
//        val rotate1Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_rotate1)
//        val rotate2Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_rotate2)
//        val rotate3Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_rotate3)
//        val rotate4Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_rotate4)
//        binding.aniRecord.icAniRecord1.startAnimation(rotate2Animation)
//        binding.aniRecord.icAniRecord2.startAnimation(rotate4Animation)
//        binding.aniRecord.icAniRecord3.startAnimation(rotate1Animation)
//        binding.aniRecord.icAniRecord4.startAnimation(rotate2Animation)
//        binding.aniRecord.icAniRecord5.startAnimation(rotate4Animation)
//        binding.aniRecord.icAniRecord6.startAnimation(rotate3Animation)
//        val rocketAnimation = binding.aniRecord.icAniRecord7.background as AnimationDrawable
//        rocketAnimation.start()
//        val translate1Animation =
//            AnimationUtils.loadAnimation(requireContext(), R.anim.anim_translate1)
//        binding.aniRecord.icAniRecord.startAnimation(translate1Animation)
//        handler.postDelayed({
//            binding.btnSetting.visibility = View.VISIBLE
//            binding.btnMoreOption.visibility = View.VISIBLE
//            binding.txtContent.visibility = View.VISIBLE
//            binding.txtContent2.visibility = View.VISIBLE
//            binding.btnRecord.visibility = View.VISIBLE
//            binding.txtMess.txtMess.setText(R.string.mess_start_record)
//            binding.txtMess.root.visibility = View.VISIBLE
//            val translate2Animation =
//                AnimationUtils.loadAnimation(requireContext(), R.anim.anim_translate2)
//            runnableAnimation = object : Runnable {
//                override fun run() {
//                    binding.txtMess.root.startAnimation(translate2Animation)
//                    handler.postDelayed(this, 1000)
//                }
//            }
//            handler.post(runnableAnimation!!)
//        }, 1500)

//        dialog = MoreOptionDialog()

//        ProxAds.instance.showMediumNativeWithShimmerStyle20(
//            requireActivity(),
//            BuildConfig.native_home,
//            binding.adContainer,
//            object : AdsCallback() {})
//        if (ProxPurchase.getInstance().checkPurchased()
//            || !NetworkUtils.isNetworkAvailable(requireContext())
//        ) {
//            binding.adContainer.visibility = View.GONE
//        } else {
//            val marginParams = binding.btnRecord.layoutParams as ViewGroup.MarginLayoutParams
//            marginParams.setMargins(0, 0, 0, 0)
//        }
    }

    override fun addEvent() {
        binding.btnRecord.setOnClickListener {
            FirebaseUtils.sendEvent(requireContext(), "Layout_Home", "Click recoding")
            callback?.onRecord()
        }
        binding.btnDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            EventLogger.getInstance()?.logEvent("click_main_setting")
        }

//        dialog.setOnImportListener {
//            callback?.onImport()
//        }

//        dialog.setOnTextToVoiceListener {
//            callback?.onTextToVoice()
//        }

//        dialog.setOnFileListener {
//            callback?.onFile()
//        }

//        dialog.setOnVideoListener {
//            callback?.onVideo()
//        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    @SuppressLint("SetTextI18n")
    private fun setupDrawerNavigation() {
        binding.navContent.tvVersion.text = "Ver ${BuildConfig.VERSION_NAME}"

        binding.navContent.btnLanguage.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_language")
            startActivity(Intent(context, LanguageActivity::class.java))
        }
        binding.navContent.btnRateNavigation.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_rate")
            ActionUtils.showRateDialog(requireActivity(), false, callback = {
                if (it) hideRate()
            })
        }
        binding.navContent.btnShare.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_share")
            CommonUtils.shareApp(requireActivity())
        }
        binding.navContent.btnFeedback.setOnClickListener {
            ActionUtils.sendFeedback(requireActivity())
        }
        binding.navContent.btnPrivacy.setOnClickListener {
            PolicyWebViewActivity.start(requireActivity())
//            CommonUtils.showPolicy(this)
        }
    }
    private fun hideRate() {
        binding.btnRate.hide()
        binding.navContent.btnRateNavigation.hide()
    }


    interface Callback {
        fun onRecord()
        fun onSetting()
        fun onImport()
        fun onTextToVoice()
        fun onFile()
        fun onVideo()
    }
}