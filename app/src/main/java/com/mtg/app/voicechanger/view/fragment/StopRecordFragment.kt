package com.mtg.app.voicechanger.view.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseFragment
import com.mtg.app.voicechanger.databinding.FragmentRecordBinding
import com.mtg.app.voicechanger.databinding.FragmentStopRecordBinding
import com.mtg.app.voicechanger.media.Recorder
import com.mtg.app.voicechanger.utils.NumberUtils
import kotlin.math.roundToInt

class StopRecordFragment : BaseFragment<FragmentStopRecordBinding>(FragmentStopRecordBinding::inflate) {
    private var countDownTimer: CountDownTimer? = null
    private var callback: Callback? = null

    private var recorder: Recorder? = null
    private var isStop = false
    private val handler = Handler(Looper.getMainLooper())
    private var runnableAnimation: Runnable? = null
    private val runnableTime: Runnable = object : Runnable {
        override fun run() {
            binding.timelineTextView.text = NumberUtils.formatAsTime(
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
        if (isStop) {
            back()
        }
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
//        countDownTimer = object : CountDownTimer(5000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                binding.txtCountDown.text =
//                    (millisUntilFinished.toFloat() / 1000).roundToInt().toString()
//            }
//
//            override fun onFinish() {
                recording()

//                binding.txtCountDown.visibility = View.GONE

                binding.clVisual.visibility = View.VISIBLE
                binding.timelineTextView.visibility = View.VISIBLE
                runnableAnimation = object : Runnable {
                    override fun run() {
                        handler.postDelayed(this, 1000)
                    }
                }
                handler.post(runnableAnimation!!)
//            }
//        }
//        countDownTimer?.start()
    }

    override fun addEvent() {
        binding.btnBack.setOnClickListener { back() }
        binding.btnStop.setOnClickListener {
            stopRecord()
//            val intent = Intent(requireActivity(), ChangeVoiceActivity::class.java)
//            intent.action = NameDialog.RECORD_TO_CHANGE_VOICE
//            intent.putExtra(
//                ChangeVoiceActivity.PATH_FILE,
//                FileUtils.getTempRecordingFilePath(requireContext())
//            )
//            startActivity(intent)
//            requireActivity().overridePendingTransition(
//                R.anim.anim_right_left_1,
//                R.anim.anim_right_left_2
//            )
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