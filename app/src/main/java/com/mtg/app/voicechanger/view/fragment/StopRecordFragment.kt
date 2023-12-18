package com.mtg.app.voicechanger.view.fragment

import android.os.Bundle
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseFragment
import com.mtg.app.voicechanger.databinding.FragmentStopRecordBinding
import com.mtg.app.voicechanger.media.Recorder
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.FileUtils
import com.mtg.app.voicechanger.utils.NumberUtils
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.view.dialog.NameDialog
import com.mtg.app.voicechanger.view.activity.ChangeVoiceActivity

class StopRecordFragment :
    BaseFragment<FragmentStopRecordBinding>(FragmentStopRecordBinding::inflate) {
    private var countDownTimer: CountDownTimer? = null
    private var callback: Callback? = null

    private var recorder: Recorder? = null
    private var isStop = false
    private val handler = Handler(Looper.getMainLooper())
    private val runnableTime: Runnable = object : Runnable {
        override fun run() {
            binding.timelineTextView.text = NumberUtils.formatAsTimeHours(
                recorder!!.currentTime
            )
            binding.visualizer.addAmp(recorder!!.maxAmplitude, recorder!!.tickDuration)
            handler.post(this)
        }
    }

    fun newInstance(): StopRecordFragment {
        val args = Bundle()
        val fragment = StopRecordFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onStart() {
        super.onStart()
//        if (isStop) {
//            back()
//        }
    }

    override fun onStop() {
        super.onStop()
        isStop = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopRecord()
        isStop = false
    }


    override fun initView() {

        binding.clVisual.visibility = View.VISIBLE
        binding.timelineTextView.visibility = View.VISIBLE
    }

    override fun addEvent() {
        binding.btnBack.setOnClickListener { back() }
        binding.btnStart.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_record_start")
            recording()
            binding.btnStart.visibility = View.GONE
            binding.tvStart.visibility = View.GONE
            binding.btnStop.visibility = View.VISIBLE
            binding.tvStop.visibility = View.VISIBLE
        }
        binding.btnStop.setOnClickListener {
            EventLogger.getInstance()?.logEvent("click_record_stop")
            stopRecord()
            binding.btnStart.visibility = View.VISIBLE
            binding.tvStart.visibility = View.VISIBLE
            binding.btnStop.visibility = View.GONE
            binding.tvStop.visibility = View.GONE
            val intent = Intent(requireActivity(), ChangeVoiceActivity::class.java)
            intent.action = NameDialog.RECORD_TO_CHANGE_VOICE
            intent.putExtra(
                Constants.PATH_FILE,
                FileUtils.getTempRecordingFilePath(requireContext())
            )
            startActivity(intent)
            requireActivity().overridePendingTransition(
                R.anim.anim_right_left_1,
                R.anim.anim_right_left_2
            )
        }
    }


    private fun recording() {
        recorder = Recorder(requireContext())
        recorder?.start()
        handler.post(runnableTime)
    }

    private fun stopRecord() {
        handler.removeCallbacksAndMessages(null)
        countDownTimer?.cancel()
        recorder?.stop()
        recorder?.release()
    }

    private fun back() {
        stopRecord()
        callback?.onClose()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onClose()
    }
}