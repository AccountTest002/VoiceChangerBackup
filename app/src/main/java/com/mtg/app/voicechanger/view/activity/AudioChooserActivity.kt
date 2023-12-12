package com.mtg.app.voicechanger.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.control.base.OnActionCallback
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.data.model.AudioFile
import com.mtg.app.voicechanger.databinding.ActivityAudioChooserBinding
import com.mtg.app.voicechanger.utils.FileUtils
import com.mtg.app.voicechanger.utils.ListAudioManager
import com.mtg.app.voicechanger.utils.LoadDataUtils
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.view.adapter.AudioAdapter
import com.mtg.app.voicechanger.view.dialog.NameDialog

class AudioChooserActivity :
    BaseActivity<ActivityAudioChooserBinding>(ActivityAudioChooserBinding::inflate) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, AudioChooserActivity::class.java)
            context.startActivity(starter)
        }
    }

    private val loadFileReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context, p1: Intent?) {
            loadFile()
        }
    }
    private val audioList = arrayListOf<AudioFile>()
    private val listAudioManger by lazy {
        ListAudioManager(audioList, object : ListAudioManager.CallBack {
            override fun onEmpty(it: Boolean) {
                if (it) {
                    binding.llEmptySearch.visibility = View.VISIBLE
                } else {
                    binding.llEmptySearch.visibility = View.GONE
                }
            }
        })
    }
    private lateinit var adapter: AudioAdapter
    override fun initView() {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val statusBackground = ContextCompat.getDrawable(this, R.drawable.bg_head_bar)
        window.statusBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(statusBackground)
        binding.ctHeader.background = null

        registerReceiver(loadFileReceiver, IntentFilter(LoadDataUtils.LOAD_SUCCESSFUL))
        adapter = AudioAdapter(audioList, this)
        adapter.mCallback = object : OnActionCallback {
            override fun callback(key: String?, vararg data: Any?) {
                var item = data[0] as AudioFile
                val intent = Intent(this@AudioChooserActivity, ChangeVoiceActivity::class.java)
                intent.action = NameDialog.RECORD_TO_CHANGE_VOICE
                intent.putExtra(
                    Constants.PATH_FILE,
                    item.path
                )
                intent.putExtra(ChangeVoiceActivity.IS_FROM_IMPORT_FLOW, true)
                startActivity(intent)
            }
        }
        binding.rcv.layoutManager = LinearLayoutManager(this)
        binding.rcv.adapter = adapter
        loadFile()

    }

    private fun loadFile() {
        LoadDataUtils.loadAudio(this, object : LoadDataUtils.CallBack {
            override fun onSuccess(list: List<AudioFile>) {
                audioList.clear()
                audioList.addAll(list)

                adapter.notifyDataSetChanged()
                if (audioList.isEmpty()) {
                    binding.llEmpty.visibility = View.VISIBLE
                    binding.ivSearch.visibility = View.GONE
                }
            }
        })
    }

    override fun addEvent() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivBackSearch.setOnClickListener {
            binding.ctMainTop.visibility = View.VISIBLE
            binding.ctSearchTop.visibility = View.GONE
            binding.edtSearch.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.edtSearch.windowToken, 0)
        }
        binding.ivSearch.setOnClickListener {
            binding.ctMainTop.visibility = View.GONE
            binding.ctSearchTop.visibility = View.VISIBLE
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.edtSearch.text!!.isEmpty()) {
                    binding.ivCancelSearch.visibility = View.GONE
                } else {
                    binding.ivCancelSearch.visibility = View.VISIBLE
                }
                listAudioManger.filterList(binding.edtSearch.text.toString())
                adapter.notifyDataSetChanged()
            }

        })

        binding.ivCancelSearch.setOnClickListener {
            binding.edtSearch.setText("")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(loadFileReceiver)

    }
}