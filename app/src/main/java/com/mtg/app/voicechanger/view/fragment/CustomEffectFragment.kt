package com.mtg.app.voicechanger.view.fragment

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.databinding.FragmentCustomEffectBinding
import com.mtg.app.voicechanger.utils.FFMPEGUtils
import com.mtg.app.voicechanger.utils.FileUtils
import com.mtg.app.voicechanger.utils.FirebaseUtils
import com.mtg.app.voicechanger.view.adapter.EffectAdapter


class CustomEffectFragment : Fragment() {
    private lateinit var binding: FragmentCustomEffectBinding
    private var hzSelect: String = "500"

    private var callback: Callback? = null

    fun newInstance(): CustomEffectFragment {
        val args = Bundle()
        val fragment = CustomEffectFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomEffectBinding.inflate(inflater)
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
        binding.btnResetBasic.isEnabled = false
        binding.btnResetEqualizer.isEnabled = false
        binding.btnResetReverb.isEnabled = false
        setEnableCustom(false)
        setGradientThumbSlider()
    }

    private fun setGradientThumbSlider(){
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                getColor(requireContext(), R.color._5d77f0),
                getColor(requireContext(), R.color._4bdfff)
            )
        )
        gradientDrawable.cornerRadius = resources.getDimension(R.dimen.icon_size)
        val thumbTintList =
            ColorStateList.valueOf(getColor(requireContext(), android.R.color.transparent))

        //Basic
        binding.layoutBasic.seekTempoPitch.thumbTintList = thumbTintList
        binding.layoutBasic.seekTempoPitch.setCustomThumbDrawable(gradientDrawable)
        binding.layoutBasic.seekTempoRate.thumbTintList = thumbTintList
        binding.layoutBasic.seekTempoRate.setCustomThumbDrawable(gradientDrawable)
        binding.layoutBasic.seekPanning.thumbTintList = thumbTintList
        binding.layoutBasic.seekPanning.setCustomThumbDrawable(gradientDrawable)

        //Equalizer
        binding.layoutEqualizer.seekBandwidth.thumbTintList = thumbTintList
        binding.layoutEqualizer.seekBandwidth.setCustomThumbDrawable(gradientDrawable)
        binding.layoutEqualizer.seekGain.thumbTintList = thumbTintList
        binding.layoutEqualizer.seekGain.setCustomThumbDrawable(gradientDrawable)

        //Reverb
        binding.layoutReverb.seekInGain.thumbTintList = thumbTintList
        binding.layoutReverb.seekInGain.setCustomThumbDrawable(gradientDrawable)
        binding.layoutReverb.seekOutGain.thumbTintList = thumbTintList
        binding.layoutReverb.seekOutGain.setCustomThumbDrawable(gradientDrawable)
        binding.layoutReverb.seekDelay.thumbTintList = thumbTintList
        binding.layoutReverb.seekDelay.setCustomThumbDrawable(gradientDrawable)
        binding.layoutReverb.seekDecay.thumbTintList = thumbTintList
        binding.layoutReverb.seekDecay.setCustomThumbDrawable(gradientDrawable)
    }

    fun isCustom(): Boolean {
        return binding.btnResetBasic.isEnabled || binding.btnResetEqualizer.isEnabled || binding.btnResetReverb.isEnabled
    }

    private fun initEvent() {
        actionCustomBasic()
        actionCustomEqualizer()
        actionCustomReverb()
    }

    fun setEnableCustom(isEnable: Boolean) {
        binding.switchBasic.isEnabled = isEnable
        binding.switchEqualizer.isEnabled = isEnable
        binding.switchReverb.isEnabled = isEnable
//        binding.layoutEffect.layoutCustom.btnResetBasic.setEnabled(isEnable);
//        binding.layoutEffect.layoutCustom.btnResetEqualizer.setEnabled(isEnable);
//        binding.layoutEffect.layoutCustom.btnResetReverb.setEnabled(isEnable);
        binding.layoutBasic.seekTempoPitch.isEnabled = isEnable
        binding.layoutBasic.seekTempoRate.isEnabled = isEnable
        binding.layoutBasic.seekPanning.isEnabled = isEnable
        binding.layoutEqualizer.radio500.isEnabled = isEnable
        binding.layoutEqualizer.seekBandwidth.isEnabled = isEnable
        binding.layoutEqualizer.seekGain.isEnabled = isEnable
        binding.layoutReverb.seekInGain.isEnabled = isEnable
        binding.layoutReverb.seekOutGain.isEnabled = isEnable
        binding.layoutReverb.seekDelay.isEnabled = isEnable
        binding.layoutReverb.seekDecay.isEnabled = isEnable
        binding.layoutEqualizer.radGroupHz.isEnabled = isEnable
        binding.layoutEqualizer.radio500.isEnabled = isEnable
        binding.layoutEqualizer.radio1000.isEnabled = isEnable
        binding.layoutEqualizer.radio2000.isEnabled = isEnable
        binding.layoutEqualizer.radio3000.isEnabled = isEnable
        binding.layoutEqualizer.radio4000.isEnabled = isEnable
        binding.layoutEqualizer.radio5000.isEnabled = isEnable
        binding.layoutEqualizer.radio6000.isEnabled = isEnable
        binding.layoutEqualizer.radio7000.isEnabled = isEnable
        binding.layoutEqualizer.radio8000.isEnabled = isEnable
    }

    private fun actionCustomBasic() {
        binding.switchBasic.setOnCheckedChangeListener { _: CompoundButton?, b: Boolean ->
            if (b) {
                FirebaseUtils.sendEvent(requireContext(), "Layout_Effect", "Click Custom Basic")
                binding.switchBasic.setTrackResource(R.drawable.ic_track_enable)
                binding.layoutBasic.root.visibility = View.VISIBLE
                binding.clBasic.setBackgroundResource(R.drawable.bg_custom_sound_big)
                binding.btnResetBasic.visibility = View.VISIBLE
                binding.switchBasic.setThumbResource(R.drawable.ic_thumb2)
            } else {
                binding.switchBasic.setTrackResource(R.drawable.ic_track_disable)
                binding.layoutBasic.root.visibility = View.GONE
                binding.btnResetBasic.visibility = View.INVISIBLE
                binding.clBasic.setBackgroundResource(R.drawable.bg_custom_sound)
                binding.switchBasic.setThumbResource(R.drawable.ic_thumb)
                if (binding.btnResetBasic.isEnabled) {
                    binding.btnResetBasic.setImageResource(R.drawable.ic_reset_disable)
                    binding.btnResetBasic.isEnabled = false
                    resetCustomBasic()
                    selectCustom()
                }
            }
        }

        binding.btnResetBasic.setOnClickListener {
            if (EffectAdapter.isExecuting) {
                Toast.makeText(activity, R.string.processing_in_progress, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            resetCustomBasic()
            binding.btnResetBasic.setImageResource(R.drawable.ic_reset_disable)
            binding.btnResetBasic.isEnabled = false
            selectCustom()
        }

        binding.layoutBasic.seekTempoPitch.addOnSliderTouchListener(onSliderTouchListener)
        binding.layoutBasic.seekTempoRate.addOnSliderTouchListener(onSliderTouchListener)
        binding.layoutBasic.seekPanning.addOnSliderTouchListener(onSliderTouchListener)
    }

    private fun actionCustomEqualizer() {
        binding.switchEqualizer.setOnCheckedChangeListener { _: CompoundButton?, b: Boolean ->
            if (b) {
                FirebaseUtils.sendEvent(requireContext(), "Layout_Effect", "Click Custom Equalizer")
                binding.switchEqualizer.setTrackResource(R.drawable.ic_track_enable)
                binding.layoutEqualizer.root.visibility = View.VISIBLE
                binding.clEqualizer.setBackgroundResource(R.drawable.bg_custom_sound_big)
                binding.btnResetEqualizer.visibility = View.VISIBLE
                binding.switchEqualizer.setThumbResource(R.drawable.ic_thumb2)
            } else {
                binding.switchEqualizer.setTrackResource(R.drawable.ic_track_disable)
                binding.layoutEqualizer.root.visibility = View.GONE
                binding.btnResetEqualizer.visibility = View.INVISIBLE
                binding.clEqualizer.setBackgroundResource(R.drawable.bg_custom_sound)
                binding.switchEqualizer.setThumbResource(R.drawable.ic_thumb)
                if (binding.btnResetEqualizer.isEnabled) {
                    binding.btnResetEqualizer.setImageResource(R.drawable.ic_reset_disable)
                    binding.btnResetEqualizer.isEnabled = false
                    resetCustomEqualizer()
                    selectCustom()
                }
            }
        }

        binding.btnResetEqualizer.setOnClickListener {
            if (EffectAdapter.isExecuting) {
                Toast.makeText(activity, R.string.processing_in_progress, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            resetCustomEqualizer()
            binding.btnResetEqualizer.setImageResource(R.drawable.ic_reset_disable)
            binding.btnResetEqualizer.isEnabled = false
            selectCustom()
        }
        checkRadio()
        binding.layoutEqualizer.seekBandwidth.addOnSliderTouchListener(onSliderTouchListener)
        binding.layoutEqualizer.seekGain.addOnSliderTouchListener(onSliderTouchListener)
    }

    private fun actionCustomReverb() {
        binding.switchReverb.setOnCheckedChangeListener { _: CompoundButton?, b: Boolean ->
            if (b) {
                FirebaseUtils.sendEvent(requireContext(), "Layout_Effect", "Click Custom Reverb")
                binding.switchReverb.setTrackResource(R.drawable.ic_track_enable)
                binding.layoutReverb.root.visibility = View.VISIBLE
                binding.clReverb.setBackgroundResource(R.drawable.bg_custom_sound_big)
                binding.btnResetReverb.visibility = View.VISIBLE
                binding.switchReverb.setThumbResource(R.drawable.ic_thumb2)
            } else {
                binding.switchReverb.setTrackResource(R.drawable.ic_track_disable)
                binding.layoutReverb.root.visibility = View.GONE
                binding.btnResetReverb.visibility = View.INVISIBLE
                binding.clReverb.setBackgroundResource(R.drawable.bg_custom_sound)
                binding.switchReverb.setThumbResource(R.drawable.ic_thumb)
                if (binding.btnResetReverb.isEnabled) {
                    binding.btnResetReverb.setImageResource(R.drawable.ic_reset_disable)
                    binding.btnResetReverb.isEnabled = false
                    resetCustomReverb()
                    selectCustom()
                }
            }
        }

        binding.btnResetReverb.setOnClickListener {
            if (EffectAdapter.isExecuting) {
                Toast.makeText(activity, R.string.processing_in_progress, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            resetCustomReverb()
            binding.btnResetReverb.setImageResource(R.drawable.ic_reset_disable)
            binding.btnResetReverb.isEnabled = false
            selectCustom()
        }
        binding.layoutReverb.seekInGain.addOnSliderTouchListener(onSliderTouchListener)
        binding.layoutReverb.seekOutGain.addOnSliderTouchListener(onSliderTouchListener)
        binding.layoutReverb.seekDelay.addOnSliderTouchListener(onSliderTouchListener)
        binding.layoutReverb.seekDecay.addOnSliderTouchListener(onSliderTouchListener)
    }

    private fun checkRadio() {
        binding.layoutEqualizer.radGroupHz.setOnCheckedChangeListener(radGrpOnCheckedChangeListener)
        binding.layoutEqualizer.radio500.setOnCheckedChangeListener(radBtnOnCheckedChangeListener)
        binding.layoutEqualizer.radio1000.setOnCheckedChangeListener(radBtnOnCheckedChangeListener)
        binding.layoutEqualizer.radio2000.setOnCheckedChangeListener(radBtnOnCheckedChangeListener)
        binding.layoutEqualizer.radio3000.setOnCheckedChangeListener(radBtnOnCheckedChangeListener)
        binding.layoutEqualizer.radio4000.setOnCheckedChangeListener(radBtnOnCheckedChangeListener)
        binding.layoutEqualizer.radio5000.setOnCheckedChangeListener(radBtnOnCheckedChangeListener)
        binding.layoutEqualizer.radio6000.setOnCheckedChangeListener(radBtnOnCheckedChangeListener)
        binding.layoutEqualizer.radio7000.setOnCheckedChangeListener(radBtnOnCheckedChangeListener)
        binding.layoutEqualizer.radio8000.setOnCheckedChangeListener(radBtnOnCheckedChangeListener)
    }

    fun resetCustomEffect() {
        binding.switchBasic.isChecked = false
        binding.switchEqualizer.isChecked = false
        binding.switchReverb.isChecked = false
        binding.switchBasic.setTrackResource(R.drawable.ic_track_disable)
        binding.switchEqualizer.setTrackResource(R.drawable.ic_track_disable)
        binding.switchReverb.setTrackResource(R.drawable.ic_track_disable)
        binding.layoutBasic.root.visibility = View.GONE
        binding.layoutEqualizer.root.visibility = View.GONE
        binding.layoutReverb.root.visibility = View.GONE
        resetCustomBasic()
        resetCustomEqualizer()
        resetCustomReverb()
    }

    private fun resetCustomBasic() {
        binding.layoutBasic.seekTempoPitch.value = 16000f
        binding.layoutBasic.seekTempoRate.value = 1f
        binding.layoutBasic.seekPanning.value = 1f
    }

    private fun resetCustomEqualizer() {
        binding.layoutEqualizer.radio500.isChecked = true
        hzSelect = "500"
        binding.layoutEqualizer.seekBandwidth.value = 100f
        binding.layoutEqualizer.seekGain.value = 0f
    }

    private fun resetCustomReverb() {
        binding.layoutReverb.seekInGain.value = 1f
        binding.layoutReverb.seekOutGain.value = 1f
        binding.layoutReverb.seekDelay.value = 0f
        binding.layoutReverb.seekDecay.value = 1f
    }

    private fun enableReset() {
        if (binding.layoutBasic.seekTempoPitch.value == 16000f
            && binding.layoutBasic.seekTempoRate.value == 1f
            && binding.layoutBasic.seekPanning.value == 1f
        ) {
            binding.btnResetBasic.setImageResource(R.drawable.ic_reset_disable)
            binding.btnResetBasic.isEnabled = false
        } else {
            binding.btnResetBasic.setImageResource(R.drawable.ic_reset_enable)
            binding.btnResetBasic.isEnabled = true
        }
        if (binding.layoutEqualizer.radio500.isChecked
            && binding.layoutEqualizer.seekBandwidth.value == 100f
            && binding.layoutEqualizer.seekGain.value == 0f
        ) {
            binding.btnResetEqualizer.setImageResource(R.drawable.ic_reset_disable)
            binding.btnResetEqualizer.isEnabled = false
        } else {
            binding.btnResetEqualizer.setImageResource(R.drawable.ic_reset_enable)
            binding.btnResetEqualizer.isEnabled = true
        }
        if (binding.layoutReverb.seekInGain.value == 1f
            && binding.layoutReverb.seekOutGain.value == 1f
            && binding.layoutReverb.seekDelay.value == 0f
            && binding.layoutReverb.seekDecay.value == 1f
        ) {
            binding.btnResetReverb.setImageResource(R.drawable.ic_reset_disable)
            binding.btnResetReverb.isEnabled = false
        } else {
            binding.btnResetReverb.setImageResource(R.drawable.ic_reset_enable)
            binding.btnResetReverb.isEnabled = true
        }
    }

    private val onSliderTouchListener: Slider.OnSliderTouchListener =
        object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}

            override fun onStopTrackingTouch(slider: Slider) {
                enableReset()
                selectCustom()
            }
        }

    private val radBtnOnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                compoundButton.setTextColor(resources.getColor(R.color._5f5f5f))
                hzSelect =
                    compoundButton.text.toString().substring(0, compoundButton.text.length - 2)
                selectCustom()
            } else {
                compoundButton.setTextColor(resources.getColor(R.color._805F5F5F))
            }
        }

    private val radGrpOnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { _: RadioGroup?, _: Int ->
            if (binding.layoutEqualizer.radio500.isChecked
                && binding.layoutEqualizer.seekBandwidth.value == 100f
                && binding.layoutEqualizer.seekGain.value == 0f
            ) {
                binding.btnResetEqualizer.setImageResource(R.drawable.ic_reset_disable)
                binding.btnResetEqualizer.isEnabled = false
            } else {
                binding.btnResetEqualizer.setImageResource(R.drawable.ic_reset_enable)
                binding.btnResetEqualizer.isEnabled = true
            }
        }

    private fun selectCustom() {
        val hzNumber: Double = try {
            hzSelect.toDouble()
        } catch (e: Exception) {
            500.0
        }
        val cmd = FFMPEGUtils.getCMDCustomEffect(
            FileUtils.getTempEffectFilePath(requireContext()),
            FileUtils.getTempCustomFilePath(requireContext()),
            (binding.layoutBasic.seekTempoPitch.value / 16000).toDouble(),
            binding.layoutBasic.seekTempoRate.value.toDouble(),
            binding.layoutBasic.seekPanning.value.toDouble(),
            hzNumber,
            binding.layoutEqualizer.seekBandwidth.value.toDouble(),
            binding.layoutEqualizer.seekGain.value.toDouble(),
            binding.layoutReverb.seekInGain.value.toDouble(),
            binding.layoutReverb.seekOutGain.value.toDouble(),
            if (binding.layoutReverb.seekDelay.value == 0f) 1.0 else binding.layoutReverb.seekDelay.value.toDouble(),
            if (binding.layoutReverb.seekDecay.value == 0f) 0.01 else binding.layoutReverb.seekDecay.value.toDouble()
        )
        callback?.addCustom(cmd)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun addCustom(cmd: String)
    }
}