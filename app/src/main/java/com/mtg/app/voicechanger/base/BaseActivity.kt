package com.mtg.app.voicechanger.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.consent_dialog.ConsentDialogManager
import com.mtg.app.voicechanger.utils.Common
import com.mtg.app.voicechanger.utils.LanguageUtils
import com.mtg.app.voicechanger.utils.app.AppPreferences
import com.mtg.app.voicechanger.view.activity.SplashActivity
import com.mtg.app.voicechanger.view.dialog.DialogNoInternet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.Locale
import kotlin.coroutines.CoroutineContext


abstract class BaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) :
    LocalizationActivity(), CoroutineScope {
    val TAG = "~~~"
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var job: Job

    private var firebaseAnalytics: FirebaseAnalytics? = null

    private val internetReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context, p1: Intent?) {
            if (isRegisterReceiver)
                onRefreshNetWork()
        }
    }

    open fun onRefreshNetWork() {
        if (Common.isNetworkAvailable(this)) {
            dialogNoInternet?.run {
                if (this.isShowing)
                    dismiss()
            }
        } else {
            dialogNoInternet?.run {
                if (!isShowing)
                    show()
            }
        }
    }

    var dialogNoInternet: DialogNoInternet? = null

    /**
     * set isFullscreen
     */
    var isFullScreen = false

    var isRegisterReceiver = false

    val binding: B by lazy { bindingFactory(layoutInflater) }

    lateinit var mContext: Context

    open fun binding() {
        if (isFullScreen)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        changeStatusBar(ContextCompat.getColor(this, R.color.white))
        setContentView(binding.root)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (this is SplashActivity) {
            return super.dispatchTouchEvent(ev)
        }
        return if (ev.action == MotionEvent.ACTION_DOWN) {
            if (ConsentDialogManager.instance?.showConsentDialogOnButtonClick(this) == true) {
                true
            } else {
                super.dispatchTouchEvent(ev)
            }
        } else {
            super.dispatchTouchEvent(ev)
        }
    }

    protected open fun initLanguage() {
        if (AppPreferences.instance.isChooseLanguage) {
            val locale = Locale(AppPreferences.instance.currentLanguage)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        } else {
            val locale = Locale(LanguageUtils.getDefaultLanguage())
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }

    override fun onResume() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
        super.onResume()
    }

    open fun loadAds() {}

    /**
     * to set size of view (TextView,..etc) by screen width
     */
    abstract fun initView()

    protected abstract fun addEvent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLanguage()
        job = Job()
        mContext = this
        binding()
        loadAds()
        initView()
        addEvent()

        //firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
        dialogNoInternet = DialogNoInternet((this))
        registerReceiver(internetReceiver, IntentFilter().apply {
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        unregisterReceiver(internetReceiver)
    }

    protected open fun changeStatusBar(color: String?) {
        window.statusBarColor = Color.parseColor(color)
    }

    protected open fun changeStatusBar(color: Int) {
        window.statusBarColor = color
    }

    /**
     * Log event to firebase
     */
    open fun logEvent(value: String) {
        if (firebaseAnalytics == null) {
            return
        }
        try {
            Log.d("android_log", "logEvent: $value")
            val bundle = Bundle()
            bundle.putString("EVENT", value)
            firebaseAnalytics!!.logEvent(value, bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}