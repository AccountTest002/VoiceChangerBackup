package com.mtg.app.voicechanger.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.common.control.manager.AdmobManager
import com.mtg.app.voicechanger.BuildConfig
import com.mtg.app.voicechanger.media.ImportAudioFlow
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseFragment
import com.mtg.app.voicechanger.databinding.FragmentRecordBinding
import com.mtg.app.voicechanger.utils.ActionUtils
import com.mtg.app.voicechanger.utils.CommonUtils
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.LanguageUtils
import com.mtg.app.voicechanger.utils.PermissionUtils
import com.mtg.app.voicechanger.utils.hide
import com.mtg.app.voicechanger.view.activity.AudioChooserActivity
import com.mtg.app.voicechanger.view.activity.LanguageActivity
import com.mtg.app.voicechanger.view.activity.MyWorkActivity
import com.mtg.app.voicechanger.view.activity.PolicyWebViewActivity
import com.mtg.app.voicechanger.view.dialog.DialogGuide

class RecordFragment : BaseFragment<FragmentRecordBinding>(FragmentRecordBinding::inflate) {
    //    private lateinit var dialog: MoreOptionDialog
    var dialogGuide: DialogGuide? = null
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
        AdmobManager.getInstance().loadNative(
            context, BuildConfig.native_main, binding.adContainer, R.layout.custom_native_constraint_main
        )
//        fullScreen()
        setupDrawerNavigation()

    }

    override fun addEvent() {
        binding.btnRecord.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_main_record&change")
            callback?.onRecord()
        }
        binding.btnDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            EventLogger.getInstance()?.logEvent("click_main_setting")
        }
        binding.btnFileDevice.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_main_edit_sound")
            callback?.onImport()
            ImportAudioFlow(requireActivity(), object : ImportAudioFlow.Callback {
                override fun onNoPms() {
                    PermissionUtils.requestReadAudioPms(requireActivity())
                }

                override fun onNextScreen() {
                    AudioChooserActivity.start(requireActivity())
                }
            }).start()
            requireActivity().overridePendingTransition(
                R.anim.anim_right_left_1,
                R.anim.anim_right_left_2
            )
        }
        binding.btnMyAudio.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_my_works")
            MyWorkActivity.start(requireActivity())
            requireActivity().overridePendingTransition(
                R.anim.anim_right_left_1,
                R.anim.anim_right_left_2
            )
        }

        binding.btnGuide.setOnClickListener {
            val dialogGuide = DialogGuide(requireContext())
            dialogGuide.show()
            EventLogger.getInstance()?.logEvent("click_main_tips")
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
        binding.navContent.imgFlag.setImageResource(LanguageUtils.getFlagResourceID(requireActivity()))
        binding.navContent.btnLanguage.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_language")
            startActivity(Intent(context, LanguageActivity::class.java))
            requireActivity().overridePendingTransition(
                R.anim.anim_right_left_1,
                R.anim.anim_right_left_2
            )
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
            EventLogger.getInstance()?.logEvent("click_set_feedback")
            ActionUtils.sendFeedback(requireActivity())
        }
        binding.navContent.btnPrivacy.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_set_privacy_policy")
            PolicyWebViewActivity.start(requireActivity())
            CommonUtils.showPolicy(requireActivity())
        }
    }

    private fun hideRate() {
        binding.navContent.btnRateNavigation.hide()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun fullScreen() {
        if (isAdded) {
            activity?.window?.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                statusBarColor = Color.TRANSPARENT
                setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_full1
                    )
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                    decorView.setOnApplyWindowInsetsListener { _, insets ->
                        insets.consumeSystemWindowInsets()
                        WindowInsets.CONSUMED
                    }
                }
            }
        }
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