package com.mtg.app.voicechanger.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.common.control.interfaces.AdCallback
import com.common.control.manager.AdmobManager
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mtg.app.voicechanger.AdCache
import com.mtg.app.voicechanger.BuildConfig
import com.mtg.app.voicechanger.MyApplication
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.callback.OnItemClickListener
import com.mtg.app.voicechanger.data.model.Effect
import com.mtg.app.voicechanger.data.model.FileVoice
import com.mtg.app.voicechanger.databinding.ActivityChangeVoiceBinding
import com.mtg.app.voicechanger.interfaces.FFmpegExecuteCallback
import com.mtg.app.voicechanger.media.Player
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.FFMPEGUtils
import com.mtg.app.voicechanger.utils.FileUtils
import com.mtg.app.voicechanger.utils.NumberUtils
import com.mtg.app.voicechanger.utils.constant.Constants.CHANGE_TO_RECORD
import com.mtg.app.voicechanger.utils.constant.Constants.PATH_FILE
import com.mtg.app.voicechanger.utils.constant.Constants.STUDENT_EXTRA
import com.mtg.app.voicechanger.view.adapter.EffectAdapter
import com.mtg.app.voicechanger.view.dialog.NameDialog
import com.mtg.app.voicechanger.view.fragment.BasicEffectFragment
import com.mtg.app.voicechanger.view.fragment.CustomEffectFragment
import com.mtg.app.voicechanger.view.fragment.StopRecordFragment
import com.mtg.app.voicechanger.viewmodel.FileVoiceViewModel
import com.mtg.app.voicechanger.viewmodel.VoiceViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import space.siy.waveformview.WaveFormData
import space.siy.waveformview.WaveFormView
import java.io.File
import java.io.IOException
import java.util.Date
import kotlin.math.roundToInt


class ChangeVoiceActivity :
    BaseActivity<ActivityChangeVoiceBinding>(ActivityChangeVoiceBinding::inflate),
    BasicEffectFragment.Callback,
    WaveFormView.Callback, CustomEffectFragment.Callback, OnItemClickListener {

    companion object {
        var effectSelected = FFMPEGUtils.effects[0]
        const val IS_FROM_IMPORT_FLOW = "IS_FROM_IMPORT_FLOW"

        @JvmStatic
        fun start(context: Context, isFromImportFlow: Boolean) {
            val starter = Intent(context, ChangeVoiceActivity::class.java)
                .putExtra(IS_FROM_IMPORT_FLOW, isFromImportFlow)
            context.startActivity(starter)
        }
    }

    private val model: FileVoiceViewModel by viewModels {
        VoiceViewModelFactory((application as MyApplication).repository)
    }
    private val myScop = CoroutineScope(Job() + Dispatchers.Main)

    private var isCustom = false
    private var isCreatedCustomEffectFragment = false

    private val listFragment = listOf(
        BasicEffectFragment().newInstance(),
        CustomEffectFragment().newInstance()
    )

    private var player: Player? = null
    private var isPlaying = false
    private var isPlayingConvert = false
    private var current = 0.0
    private var nameFile = ""
    private val handler = Handler(Looper.getMainLooper())
    private val updateTime: Runnable = object : Runnable {
        override fun run() {
            if (player != null) {
                current = player!!.currentPosition.toDouble() / player!!.duration
                binding.layoutPlayer.visualizer.position = player!!.currentPosition.toLong()
                binding.layoutPlayer.txtCurrentTime.text = NumberUtils.formatAsTime(
                    player!!.currentPosition.toLong()
                )
                handler.post(this)
            }
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    fun fullScreen() {
//        val window = window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        val statusBackground = ContextCompat.getDrawable(this, R.drawable.bg_head_bar)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = Color.TRANSPARENT
//        }
//        window.setBackgroundDrawable(statusBackground)
//    }

    override fun onStart() {
        super.onStart()
        if (isPlaying) {
            resumePlayer()
        }
        isPlayingConvert = true

    }

    override fun onStop() {
        super.onStop()
        if (isPlaying) {
            pausePlayer()
        }
        isPlayingConvert = false
    }

    override fun onDestroy() {
        myScop.cancel()
        if (player != null) {
            if (player!!.isPlaying) {
                stopPlayer()
            }
            player!!.release()
        }
        System.gc()
        super.onDestroy()
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun initData() {
        val intent = intent
        if (intent == null || intent.action == null) {
            goToRecord()
            return
        }

        val path = intent.getStringExtra(PATH_FILE)
        if (path == null || !File(path).exists()) {
            goToRecord()
            return
        }

        nameFile =
            if (intent.action == NameDialog.RECORD_TO_CHANGE_VOICE || intent.action == RecordActivity.IMPORT_TEXT_TO_SPEECH) {
                FileUtils.getName(FileUtils.recordingFileName)
            } else {
                FileUtils.getName(path)
            }

        val cmd =
            FFMPEGUtils.getCMDConvertRecording(path, FileUtils.getTempRecording2FilePath(this))
        myScop.launch(Dispatchers.IO) {
            FFMPEGUtils.executeFFMPEG(cmd, object : FFmpegExecuteCallback {
                override fun onSuccess() {
                    myScop.launch(Dispatchers.Main) {
                        selectEffect(effectSelected)
                    }
                }

                override fun onFailed() {
                    myScop.launch(Dispatchers.Main) {
                        loadingPlayer(null)
                    }
                }
            })
        }
    }

    private fun loadingPlayer(isLoading: Boolean?) {
        if (isLoading == null) {
            EffectAdapter.isExecuting = false

            binding.layoutLoading.txtProcessing.setText(R.string.process_error)
            binding.layoutLoading.txtProcessing.setTextColor(resources.getColor(R.color.color_274577))
        } else if (isLoading) {
            EffectAdapter.isExecuting = true

            binding.layoutPlayer.root.visibility = View.INVISIBLE
            binding.layoutLoading.root.visibility = View.VISIBLE
//            binding.layoutLoading.txtProcessing.setText(R.string.process_error)
            binding.layoutLoading.txtProcessing.setTextColor(resources.getColor(R.color.white))
            binding.btnSave.isEnabled = false
        } else {
            binding.layoutPlayer.root.visibility = View.VISIBLE
            binding.layoutLoading.root.visibility = View.GONE
            binding.btnSave.isEnabled = true
        }
    }

    private fun loadInterAds() {
        if (AdCache.getInstance().interSaved == null) {
            AdmobManager.getInstance()
                .loadInterAds(this, BuildConfig.inter_saved, object : AdCallback() {
                    override fun onResultInterstitialAd(interstitialAd: InterstitialAd?) {
                        super.onResultInterstitialAd(interstitialAd)
                        AdCache.getInstance().interSaved = interstitialAd
                    }
                })
        }
    }

    override fun initView() {
        initData()

//        fullScreen()
        player = Player()
        isPlaying = true

        val fragment0 = listFragment[0]
        if (fragment0 is BasicEffectFragment) {
            fragment0.setCallback(this)
        }

        val fragment1 = listFragment[1]
        if (fragment1 is CustomEffectFragment) {
            fragment1.setCallback(this)
        }

        loadingPlayer(true)
        clickBasicEffect(true)


    }

    private fun addStopRecordFragment() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        val stopRecordFragment = StopRecordFragment()

        transaction.replace(binding.layoutEffect.layoutFragment.id, stopRecordFragment)
        transaction.addToBackStack(null) // Thêm Fragment hiện tại vào back stack
        transaction.commit()
    }

    override fun addEvent() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_save")
            pausePlayer()
            val name = binding.layoutPlayer.txtName2.text.toString()
            val fragment = listFragment[1]
            if (isCreatedCustomEffectFragment) {
                if (fragment is CustomEffectFragment) {
                    if (fragment.isCustom()) {
                        isCustom = true
                    }
                }
            }

            val nameDialog = NameDialog().newInstance(name)
            nameDialog.setOnSaveListener {
                myScop.launch(Dispatchers.IO) {
                    val cmd: String = if (!isCustom) {
                        FFMPEGUtils.getCMDConvertRecording(
                            FileUtils.getTempEffectFilePath(this@ChangeVoiceActivity),
                            it
                        )
                    } else {
                        FFMPEGUtils.getCMDConvertRecording(
                            FileUtils.getTempCustomFilePath(this@ChangeVoiceActivity),
                            it
                        )
                    }

                    FFMPEGUtils.executeFFMPEG(cmd, object : FFmpegExecuteCallback {
                        override fun onSuccess() {
                            myScop.launch(Dispatchers.IO) {
                                insertEffectToDB(it)
                            }
                        }

                        override fun onFailed() {

                        }
                    })
                }
            }
            nameDialog.show(supportFragmentManager, "NameDialog")

        }

        binding.layoutPlayer.visualizer.callback = this

        binding.layoutPlayer.btnPauseOrResume.setOnClickListener {
            isPlaying = if (isPlaying) {
                pausePlayer()
                false
            } else {
                resumePlayer()
                true
            }
        }

        binding.layoutEffect.btnEffect.setOnClickListener {
            val fragment = listFragment[1]
            if (fragment is CustomEffectFragment) {
                if (fragment.isCustom()) {
                    isCustom = true
                }
            }

            if (isCustom) {
                if (EffectAdapter.isExecuting) {
                    Toast.makeText(this, R.string.processing_in_progress, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                EffectAdapter.isExecuting = true
                loadingPlayer(true)
                setNewPlayer(FileUtils.getTempEffectFilePath(this))
            }

            val fragment1 = listFragment[1]
            if (fragment1 is CustomEffectFragment) {
                fragment1.resetCustomEffect()
            }

            clickBasicEffect(true)
        }

        binding.layoutEffect.btnCustom.setOnClickListener {
            clickBasicEffect(false)
        }
    }

    private fun insertEffectToDB(path: String) {
        val fileVoice = FileVoice()
        fileVoice.src = effectSelected.src
        fileVoice.name = FileUtils.getName(path)
        fileVoice.path = path
        val playerEffect = MediaPlayer()
        try {
            playerEffect.setDataSource(path)
            playerEffect.prepare()
        } catch (e: IOException) {
            return
        }
        fileVoice.duration = playerEffect.duration.toLong()
        fileVoice.size = File(path).length()
        fileVoice.date = Date().time
        model.insertBG(fileVoice)
        onItemClick(fileVoice)
    }


    private fun selectEffect(effect: Effect) {
        effectSelected = effect
        stopPlayer()
        loadingPlayer(true)
        binding.layoutPlayer.txtName2.text = nameFile + "-" + effect.title
        val cmd = FFMPEGUtils.getCMDAddEffect(
            FileUtils.getTempRecording2FilePath(this),
            FileUtils.getTempEffectFilePath(this),
            effect
        )
        myScop.launch(Dispatchers.IO) {
            FFMPEGUtils.executeFFMPEG(cmd, object : FFmpegExecuteCallback {
                override fun onSuccess() {
                    myScop.launch(Dispatchers.Main) {
                        setNewPlayer(FileUtils.getTempEffectFilePath(this@ChangeVoiceActivity))
                    }
                }

                override fun onFailed() {
                    myScop.launch(Dispatchers.Main) {
                        loadingPlayer(null)
                    }
                }
            })
        }
    }

    private fun setNewPlayer(path: String) {
        WaveFormData.Factory(path).build(object : WaveFormData.Factory.Callback {
            override fun onComplete(waveFormData: WaveFormData) {
                myScop.launch(Dispatchers.Main) {
                    binding.layoutPlayer.visualizer.data = waveFormData

                    loadingPlayer(false)
                    if (player == null) {
                        player = Player()
                    }
                    player!!.setNewPath(path)
                    startPlayer()
                    player!!.seekTo((current * player!!.duration).toLong())
                    binding.layoutPlayer.visualizer.position = player!!.currentPosition.toLong()
                    binding.layoutPlayer.txtCurrentTime.text = NumberUtils.formatAsTime(
                        player!!.currentPosition.toLong()
                    )
                    if (!isPlaying || !isPlayingConvert) {
                        pausePlayer()
                    }

//                    binding.layoutLoading.progressProcessing.progress = 0f.roundToInt()
                    if (isCreatedCustomEffectFragment) {
                        val fragment = listFragment[1]
                        if (fragment is CustomEffectFragment) {
                            fragment.setEnableCustom(true)
                        }
                    }
                    EffectAdapter.isExecuting = false
                }
            }

            override fun onProgress(v: Float) {
                myScop.launch(Dispatchers.Main) {
//                    binding.layoutLoading.progressProcessing.progress = (v * 10).roundToInt()
                }
            }
        })
    }

    private fun startPlayer() {
        player?.start()
        binding.layoutPlayer.btnPauseOrResume.setImageResource(R.drawable.ic_pause)
        updateTime()
    }

    private fun stopPlayer() {
        player?.stop()
        binding.layoutPlayer.btnPauseOrResume.setImageResource(R.drawable.ic_play)
        handler.removeCallbacksAndMessages(null)
    }

    private fun pausePlayer() {
        player?.pause()
        binding.layoutPlayer.btnPauseOrResume.setImageResource(R.drawable.ic_play)
        handler.removeCallbacksAndMessages(null)
    }

    private fun resumePlayer() {
        player?.resume()
        binding.layoutPlayer.btnPauseOrResume.setImageResource(R.drawable.ic_pause)
        updateTime()
    }

    private fun updateTime() {
        if (player != null) {
            binding.layoutPlayer.txtTotalTime.text = NumberUtils.formatAsTime(
                player!!.duration.toLong()
            )
            handler.post(updateTime)
        }
    }

    private fun goToRecord() {
        val goToRecord = Intent(this, RecordActivity::class.java)
        goToRecord.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        goToRecord.action = CHANGE_TO_RECORD
        startActivity(goToRecord)
        Log.d(TAG, "ChangeVoiceActivity: To RecordActivity")
        finish()
    }

    private fun clickBasicEffect(basicEffect: Boolean) {
        if (basicEffect) {
            EventLogger.getInstance()?.logEvent("click_change_voice")
            isCustom = false

            binding.layoutPlayer.txtName2.text = nameFile + "-" + effectSelected.title

            binding.layoutEffect.btnEffect.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color._5d77f0
                )
            )
            binding.layoutEffect.cvCV.visibility = View.VISIBLE
            binding.layoutEffect.btnEffect.isEnabled = false
            binding.layoutEffect.btnCustom.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color._5f5f5f
                )
            )
            binding.layoutEffect.cvBS.visibility = View.GONE
            binding.layoutEffect.btnCustom.isEnabled = true

            createChildFragment(listFragment[0])
            isCreatedCustomEffectFragment = false
        } else {
            EventLogger.getInstance()?.logEvent("click_sound effects")
            binding.layoutPlayer.txtName2.text =
                binding.layoutPlayer.txtName2.text.toString() + "-Custom"

            binding.layoutEffect.btnEffect.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color._5f5f5f
                )
            )
            binding.layoutEffect.cvCV.visibility = View.GONE
            binding.layoutEffect.btnEffect.isEnabled = true
            binding.layoutEffect.btnCustom.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color._5d77f0
                )
            )
            binding.layoutEffect.cvBS.visibility = View.VISIBLE
            binding.layoutEffect.btnCustom.isEnabled = false

            createChildFragment(listFragment[1])
            isCreatedCustomEffectFragment = true

            handler.postDelayed({
                if (!EffectAdapter.isExecuting) {
                    val fragment = listFragment[1]
                    if (fragment is CustomEffectFragment) {
                        fragment.setEnableCustom(true)
                    }
                }
            }, 500)
        }
    }

    private fun createChildFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.layoutEffect.layoutFragment.id, fragment).commit()
    }

    override fun addEffect(effect: Effect) {
        selectEffect(effect)
    }

    override fun onPlayPause() {}

    override fun onSeek(pos: Long) {
        player!!.seekTo(pos)
        binding.layoutPlayer.txtCurrentTime.text = NumberUtils.formatAsTime(
            player!!.currentPosition.toLong()
        )
    }

    override fun addCustom(cmd: String) {
        if (EffectAdapter.isExecuting) {
            return
        }
        val fragment = listFragment[1]
        if (fragment is CustomEffectFragment) {
            fragment.setEnableCustom(false)
        }
        stopPlayer()
        loadingPlayer(true)

        myScop.launch(Dispatchers.IO) {
            FFMPEGUtils.executeFFMPEG(cmd, object : FFmpegExecuteCallback {
                override fun onSuccess() {
                    myScop.launch(Dispatchers.Main) {
                        setNewPlayer(FileUtils.getTempEffectFilePath(this@ChangeVoiceActivity))
                    }
                }

                override fun onFailed() {
                    myScop.launch(Dispatchers.Main) {
                        loadingPlayer(null)
                    }
                }
            })
        }
    }

    override fun onItemClick(fileVoice: FileVoice) {
        val intent = Intent(this, SavedActivity::class.java)
        intent.putExtra(STUDENT_EXTRA, fileVoice)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        loadInterAds()
    }
}