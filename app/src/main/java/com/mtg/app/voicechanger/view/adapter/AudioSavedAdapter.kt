package com.mtg.app.voicechanger.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseAdapter
import com.mtg.app.voicechanger.data.model.AudioFile
import com.mtg.app.voicechanger.databinding.ItemAudioBinding
import com.mtg.app.voicechanger.databinding.ItemAudioSavedBinding
import com.mtg.app.voicechanger.utils.EventLogger
import com.mtg.app.voicechanger.utils.NumberUtils
import java.io.File

class AudioSavedAdapter(mList: List<AudioFile?>?, context: Context?) :
    BaseAdapter<AudioFile?>(mList!!, context) {
    private var itemPlay: AudioFile? = null
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

    @SuppressLint("NotifyDataSetChanged")
    fun pauseAllAudio() {
        itemPlay = null
        notifyDataSetChanged()
    }

    private inner class AudioViewHolder(private val binding: ItemAudioSavedBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("NotifyDataSetChanged")
        fun loadData(itemAudio: AudioFile) {
            binding.root.tag = itemAudio
            val name = File(itemAudio.path).name
            val detail = NumberUtils.formatAsTime(itemAudio.duration) + "   " + itemAudio.size
            if (itemAudio == itemPlay) {
                binding.btnPlay.setText(R.string.pause);
            } else {
                binding.btnPlay.setText(R.string.play);
            }
            binding.tvName.text = name
            binding.tvDetail.text = detail
            binding.btnPlay.setOnClickListener {
                val item = binding.root.tag as AudioFile
                if (item != itemPlay) {
                    mCallback?.callback("play", binding.root.tag)
                    EventLogger.getInstance()?.logEvent("click_my_works_play")
                    itemPlay = item
                } else {
                    mCallback?.callback("pause", binding.root.tag)
                    EventLogger.getInstance()?.logEvent("click_my_works_pause")
                    itemPlay = null
                }
                notifyDataSetChanged()
            }
            binding.ivMore.setOnClickListener {
                mCallback?.callback("more", binding.root.tag)
            }
        }

        override fun onClick(v: View) {
        }
    }
}