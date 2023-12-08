package com.mtg.app.voicechanger.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.viewpager2.widget.ViewPager2
import com.common.control.manager.AdmobManager
import com.common.control.manager.AppOpenManager
import com.mtg.app.voicechanger.databinding.ActivityOnboardingBinding
import com.mtg.app.voicechanger.BuildConfig
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.base.ViewPagerAddFragmentsAdapter
import com.mtg.app.voicechanger.data.preferences.SharedPrefs
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.utils.setSize
import com.mtg.app.voicechanger.view.fragment.OnBoardFragment

class OnBoardActivity :
    BaseActivity<ActivityOnboardingBinding>(ActivityOnboardingBinding::inflate) {
    override fun binding() {
        isFullScreen = true
//        if (SharedPrefs.getBoolean(this, "is_skip_onboard")) {
//            startActivity(Intent(this, MainActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            })
//            finish()
//        } else {
//            SharedPrefs.put(this, "is_skip_onboard", true)
//        }
        super.binding()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, OnBoardActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun initView() {
        initViewPager()
        binding.tvNext.setSize(18)
        binding.tvNext.paintFlags = Paint.UNDERLINE_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG

        AdmobManager.getInstance().loadNative(
            this, BuildConfig.native_guide, binding.frAd, R.layout.custom_native_ads_onboard
        )
        AppOpenManager.getInstance().hideNativeOrBannerWhenShowOpenApp(this, binding.frAd)

    }

    private fun initViewPager() {
        binding.viewpagerOnboard.adapter =
            ViewPagerAddFragmentsAdapter(supportFragmentManager, lifecycle).apply {
                addFrag(OnBoardFragment(R.drawable.img_inside_1, R.string.text_on_boarding_1))
                addFrag(OnBoardFragment(R.drawable.img_inside_2, R.string.text_on_boarding_2))
                addFrag(OnBoardFragment(R.drawable.img_inside_3, R.string.text_on_boarding_3))
            }
        binding.viewpagerOnboard.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.indicatorView.selection = position
                if (position == (binding.viewpagerOnboard.adapter as ViewPagerAddFragmentsAdapter).itemCount - 1) {
                    binding.tvNext.text = getString(R.string.get_started)
                } else binding.tvNext.text = getString(R.string.next)
            }
        })

        binding.tvNext.setOnClickListener {
            when (binding.viewpagerOnboard.currentItem) {
                0 -> EventLogger.getInstance()?.logEvent("click_guide_1")
                1 -> EventLogger.getInstance()?.logEvent("click_guide_2")
                2 -> EventLogger.getInstance()?.logEvent("click_guide_3")
            }
            if (binding.viewpagerOnboard.currentItem == (binding.viewpagerOnboard.adapter as ViewPagerAddFragmentsAdapter).itemCount - 1) {
                SharedPrefs.put(this, Constants.KEY_FIRST_INTRO, true)
                startActivity(Intent(this@OnBoardActivity, RecordActivity::class.java))

            } else {
                binding.viewpagerOnboard.currentItem++
            }

        }
    }

    override fun addEvent() {

    }
}