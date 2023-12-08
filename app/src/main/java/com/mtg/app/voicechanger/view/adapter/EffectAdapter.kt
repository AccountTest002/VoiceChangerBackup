package com.mtg.app.voicechanger.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.data.model.Effect
import com.mtg.app.voicechanger.databinding.ItemEffectBinding
import com.mtg.app.voicechanger.view.activity.ChangeVoiceActivity
import java.util.ArrayList

class EffectAdapter(
    val context: Context,
    private var effects: List<Effect> = listOf()
) : RecyclerView.Adapter<EffectAdapter.EffectViewHolder>() {

    companion object {
        var isExecuting = false
    }

    private var imgSelect: ConstraintLayout? = null

    private var addEffectListener: ((Effect) -> Unit)? = null

    fun setAddEffectListener(listener: (Effect) -> Unit) {
        addEffectListener = listener
    }

    inner class EffectViewHolder(binding: ItemEffectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val binding: ItemEffectBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EffectViewHolder {
        val binding = ItemEffectBinding.inflate(LayoutInflater.from(context), parent, false)
        return EffectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EffectViewHolder, position: Int) {
        val button = holder.binding.btnAddEffect


//        button.text = effects[position].title
        holder.binding.tvName.text = effects[position].title
        button.setImageResource(effects[position].src)
        val rocketAnimation = holder.binding.imgSelect.background
//        rocketAnimation.start()
        if (effects[position].title == ChangeVoiceActivity.effectSelected.title) {
            holder.binding.imgSelect.visibility = View.VISIBLE
            imgSelect = holder.binding.imgSelect
        } else {
            holder.binding.imgSelect.visibility = View.GONE
        }

        button.setOnClickListener {
            if (isExecuting) {
                Toast.makeText(context, R.string.processing_in_progress, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            isExecuting = true
            imgSelect!!.visibility = View.GONE
            holder.binding.imgSelect.visibility = View.VISIBLE
            imgSelect = holder.binding.imgSelect
            addEffectListener?.let {
                it(effects[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return effects.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEffects(effects: ArrayList<Effect>) {
        this.effects = effects
        notifyDataSetChanged()
    }
}