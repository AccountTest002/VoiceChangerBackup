package com.mtg.app.voicechanger.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtg.app.voicechanger.base.BaseAdapter
import com.mtg.app.voicechanger.data.model.AudioFile
import com.mtg.app.voicechanger.databinding.ItemAudioBinding
import com.mtg.app.voicechanger.databinding.ItemAudioSavedBinding
import com.mtg.app.voicechanger.utils.NumberUtils
import java.io.File

class AudioSavedAdapter(mList: List<AudioFile?>?, context: Context?) :
    BaseAdapter<AudioFile?>(mList!!, context) {
    override fun viewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemAudioSavedBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return AudioViewHolder(binding)
    }

    override fun onBindView(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val itemAudioFile = mList[position]!!
        if (viewHolder is AudioViewHolder) {
            viewHolder.loadData(itemAudioFile)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private inner class AudioViewHolder(private val binding: ItemAudioSavedBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun loadData(itemAudio: AudioFile) {
            binding.root.tag = itemAudio
            var name = File(itemAudio.path).name
            var detail = NumberUtils.formatAsTime(itemAudio.duration) +"   "+itemAudio.size
            binding.tvName.text = name
            binding.tvDetail.text = detail
            binding.btnPlay.setOnClickListener {
                mCallback?.callback("play", binding.root.tag)
            }
            binding.ivMore.setOnClickListener {
                mCallback?.callback("more", binding.root.tag)
            }
        }

        override fun onClick(v: View) {
        }
    }
}