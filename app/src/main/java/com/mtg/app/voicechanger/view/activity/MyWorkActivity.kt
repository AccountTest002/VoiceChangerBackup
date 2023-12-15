package com.mtg.app.voicechanger.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.control.base.OnActionCallback
import com.common.control.utils.AppUtils
import com.mtg.app.voicechanger.MyApplication
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.data.model.AudioFile
import com.mtg.app.voicechanger.data.model.FileVoice
import com.mtg.app.voicechanger.databinding.ActivityAudioChooserBinding
import com.mtg.app.voicechanger.databinding.ActivityMyWorkBinding
import com.mtg.app.voicechanger.utils.FileUtils
import com.mtg.app.voicechanger.utils.ListAudioManager
import com.mtg.app.voicechanger.utils.LoadDataUtils
import com.mtg.app.voicechanger.utils.NumberUtils
import com.mtg.app.voicechanger.utils.PermissionUtils
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.view.adapter.AudioAdapter
import com.mtg.app.voicechanger.view.adapter.AudioSavedAdapter
import com.mtg.app.voicechanger.view.dialog.DeleteDialog
import com.mtg.app.voicechanger.view.dialog.DetailBottomSheet
import com.mtg.app.voicechanger.view.dialog.DialogReadAudioPms
import com.mtg.app.voicechanger.view.dialog.NameDialog
import com.mtg.app.voicechanger.view.dialog.RenameDialog
import com.mtg.app.voicechanger.view.dialog.SetRingtoneDialog
import com.mtg.app.voicechanger.viewmodel.FileVoiceViewModel
import com.mtg.app.voicechanger.viewmodel.VoiceViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class MyWorkActivity :
    BaseActivity<ActivityMyWorkBinding>(ActivityMyWorkBinding::inflate) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MyWorkActivity::class.java)
            context.startActivity(starter)
        }
    }

    private val model: FileVoiceViewModel by viewModels {
        VoiceViewModelFactory((application as MyApplication).repository)
    }
    private var mediaPlayer = MediaPlayer()
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
    private lateinit var adapter: AudioSavedAdapter
    override fun initView() {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val statusBackground = ContextCompat.getDrawable(this, R.drawable.bg_head_bar)
        window.statusBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(statusBackground)
        binding.ctHeader.background = null

        adapter = AudioSavedAdapter(audioList, this)
        adapter.mCallback = object : OnActionCallback {
            override fun callback(key: String?, vararg data: Any?) {
                val item = data[0] as AudioFile
                if (key.equals("play")) {
                    playAudio(item.path)
                } else if (key.equals("more")) {
                    DetailBottomSheet(item, object: DetailBottomSheet.Callback{
                        override fun onDelete() {
                            DeleteDialog(this@MyWorkActivity, object: DeleteDialog.Callback{
                                override fun onDelete() {
                                    FileUtils.deleteFile(this@MyWorkActivity, item.path)
                                    LoadDataUtils.remove(item.path)
                                    audioList.remove(item)
                                    adapter.notifyDataSetChanged()
                                    model.deleteByPath(item.path)
                                }
                            }).show()
                        }

                        override fun onRename() {
                            RenameDialog(this@MyWorkActivity, item.path, object: RenameDialog.Callback{
                                override fun onRename(newPath: String) {
                                    Log.e("android_log_app: ", newPath)
                                    model.updatePath(item.path, newPath)
                                    item.path = newPath
                                    adapter.notifyDataSetChanged()
                                }
                            }).show()
                        }

                        override fun onRingtone() {
                            if (Settings.System.canWrite(this@MyWorkActivity)) {
                                SetRingtoneDialog(this@MyWorkActivity, object: SetRingtoneDialog.Callback{
                                    override fun onOk() {
                                        if (FileUtils.setAsRingtoneOrNotification(
                                                this@MyWorkActivity,
                                                item.path,
                                                RingtoneManager.TYPE_RINGTONE
                                            ) && FileUtils.setAsRingtoneOrNotification(
                                                this@MyWorkActivity,
                                                item.path,
                                                RingtoneManager.TYPE_NOTIFICATION
                                            )
                                        ) {
                                            Toast.makeText(this@MyWorkActivity, R.string.setting_success, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }).show()

                            } else {
                                DialogReadAudioPms(this@MyWorkActivity).setCallBack(OnActionCallback { key, data ->
                                    PermissionUtils.requestWriteSetting(this@MyWorkActivity)
                                }).show()
                            }
                        }

                        override fun onShare() {
                            AppUtils.getInstance().shareFile(this@MyWorkActivity, File(item.path))
                        }

                    }).show(supportFragmentManager, "")
                } else if (key.equals("pause")) {
                    stopAudio()
                }
            }
        }
        binding.rcv.layoutManager = LinearLayoutManager(this)
        binding.rcv.adapter = adapter
        model.getFileVoices().observe(this, Observer {
            it?.let { it ->
                audioList.clear()
                audioList.addAll(it.map {
                    AudioFile(it.path!!, it.duration, NumberUtils.formatAsDate(it.date), NumberUtils.formatAsSize(
                        File(it.path!!).length()
                    ))
                })
                audioList.reverse()
                adapter.notifyDataSetChanged()
                checkState()
            }
        })
    }

    private fun checkState() {
        if (audioList.isEmpty()) {
            binding.llEmpty.visibility = View.VISIBLE
            binding.ivSearch.visibility = View.GONE
        } else {
            binding.llEmpty.visibility = View.GONE
            binding.ivSearch.visibility = View.VISIBLE
        }
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

    private fun playAudio(path: String) {
        try {
            stopAudio()
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(applicationContext, Uri.parse(path))
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener { mp ->
                mp.start()
            }
            mediaPlayer.setOnCompletionListener { mp ->
                adapter.pauseAllAudio()
                mp.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopAudio(){
        try {
            mediaPlayer.pause()
            mediaPlayer.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        stopAudio()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}