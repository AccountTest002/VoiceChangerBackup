package com.mtg.app.voicechanger.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.control.base.OnActionCallback
import com.common.control.manager.AdmobManager
import com.common.control.manager.AppOpenManager
import com.mtg.app.voicechanger.BuildConfig
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.data.model.ItemLanguage
import com.mtg.app.voicechanger.data.preferences.SharedPrefs
import com.mtg.app.voicechanger.databinding.ActivityLanguageBinding
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.LanguageUtils
import com.mtg.app.voicechanger.utils.LanguageUtils.listCountry
import com.mtg.app.voicechanger.utils.app.AppPreferences
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.view.adapter.LanguageAdapter

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(ActivityLanguageBinding::inflate) {
    private var mList: List<ItemLanguage> = ArrayList()
    private var languageAdapter: LanguageAdapter? = null
    private var itemLanguage: ItemLanguage? = null
    private var appPreferences = AppPreferences.instance
    override fun binding() {
        isFullScreen = false
        super.binding()
    }
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, LanguageActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun initView() {
        AdmobManager.getInstance().loadNative(
            this, BuildConfig.native_language, binding.frAd, R.layout.custom_native_language
        )
        AppOpenManager.getInstance().hideNativeOrBannerWhenShowOpenApp(this, binding.frAd)
        setStatusBarColor()
        initListLanguage()
        initRCLanguage()
        handleButtonBack()

//        if (Common.screenWidth / Common.screenHeight > 108 / 216) {
//            binding.imgBackground.layoutParams.height = Common.screenHeight
//        } else {
//            binding.imgBackground.layoutParams.width = Common.screenWidth
//        }
    }

    private fun setStatusBarColor() {
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white)
    }

    private fun handleButtonBack() {
        if ((!appPreferences.isChooseLanguage)) {
            binding.btBack.visibility = View.GONE
        } else {
            binding.btBack.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRCLanguage() {
        languageAdapter = LanguageAdapter(mList, this).apply {
            mCallback = OnActionCallback { key, data ->
                for (item in mList) {
                    item?.let {
                        it.imgSelect = (R.drawable.ic_disable)
                        it.bgSelected = R.drawable.bg_item_language
                    }
                }
                if (key == Constants.KEY_LANGUAGE) {
                    itemLanguage = data[0] as ItemLanguage?
                    itemLanguage?.let {
                        it.imgSelect = (R.drawable.ic_checked)
                        it.bgSelected = R.drawable.bg_item_language_select
                    }
                    this.notifyDataSetChanged()
                }
            }
        }
        binding.rcLanguage.layoutManager = LinearLayoutManager(this)
        binding.rcLanguage.adapter = languageAdapter
    }

    private fun initListLanguage() {
        mList = listCountry
//        val currentCode = SharedPrefs.getString(this, Const.SHARE_PREF_LANGUAGE, "English (US)")
        for (i in mList.indices) {
            if (mList[i].languageToLoad == appPreferences.currentLanguage) {
                mList[i].imgSelect = (R.drawable.ic_checked)
                mList[i].bgSelected = (R.drawable.bg_item_language_select)
                return
            }
        }
    }

    override fun addEvent() {
        binding.btBack.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_ins_back")
            finish()
        }
        binding.ivDone.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_language_tick")
            //Intent intent = new Intent(this, MainActivity.class);
            if (itemLanguage == null) {
                itemLanguage = LanguageUtils.getDefaultItemLanguage()
            }
            appPreferences.currentLanguage = itemLanguage!!.languageToLoad
            appPreferences.isChooseLanguage = true

//            setLanguage(itemLanguage!!.languageToLoad)
            setLanguageWithoutNotification(itemLanguage!!.languageToLoad)

            if (!SharedPrefs.getBoolean(this, Constants.KEY_FIRST_INTRO)) {
                OnBoardActivity.start(this)
                return@setOnClickListener
            }

            SharedPrefs.put(this, Constants.SHARE_PREF_LANGUAGE, itemLanguage!!.name)
            SharedPrefs.setLanguageConfig(this)
            //todo go to next
            startActivity(Intent(this, RecordActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
            finish()
        }
    }

}