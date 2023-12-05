package com.mtg.app.voicechanger.view.fragment

import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseFragment
import com.mtg.app.voicechanger.databinding.FragmentOnboadingBinding
import com.mtg.app.voicechanger.utils.setSize


class OnBoardFragment(
    var idImage: Int = R.drawable.img_inside_1,
    var idText: Int = R.string.text_on_boarding_1
) : BaseFragment<FragmentOnboadingBinding>(FragmentOnboadingBinding::inflate) {


    override fun initView() {
        binding.imgInside.setImageResource(idImage)
        binding.tvInside.text = getString(idText)
        binding.tvInside.setSize(20)
    }

    override fun addEvent() {

    }

}