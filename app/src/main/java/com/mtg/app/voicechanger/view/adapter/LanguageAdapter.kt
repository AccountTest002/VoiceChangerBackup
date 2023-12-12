package com.mtg.app.voicechanger.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.databinding.ItemLanguageBinding
import com.mtg.app.voicechanger.base.BaseAdapter
import com.mtg.app.voicechanger.data.model.ItemLanguage
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.utils.setSize

class LanguageAdapter(mList: List<ItemLanguage?>?, context: Context?) :
    BaseAdapter<ItemLanguage?>(mList!!, context) {
    override fun viewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemLanguageBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val itemLanguage = mList[position]!!
        if (viewHolder is LanguageViewHolder) {
            viewHolder.loadData(itemLanguage)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private inner class LanguageViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun loadData(itemLanguage: ItemLanguage) {
            itemView.tag = itemLanguage
            binding.imgFlag.setImageResource(itemLanguage.imageFlag)
            binding.tvLanguage.text = itemLanguage.name
            binding.rbCheck.setImageResource(itemLanguage.imgSelect)
            binding.ctLanguage.setBackgroundResource(itemLanguage.bgSelected)

            if (itemLanguage.bgSelected == R.drawable.bg_item_language_select) {
                binding.tvLanguage.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                binding.tvLanguage.setTextColor(Color.parseColor("#404040"))
            }

            binding.tvLanguage.setSize(16)
        }

        override fun onClick(v: View) {
            mCallback?.run { callback(Constants.KEY_LANGUAGE, itemView.tag) }
        }
    }
}