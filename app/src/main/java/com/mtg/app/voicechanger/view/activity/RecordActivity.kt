package com.mtg.app.voicechanger.view.activity

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.common.control.base.OnActionCallback
import com.mtg.app.voicechanger.media.ImportAudioFlow
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.base.BaseActivity
import com.mtg.app.voicechanger.databinding.ActivityRecordBinding
import com.mtg.app.voicechanger.utils.PermissionUtils
import com.mtg.app.voicechanger.utils.base.getRealPath
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.view.dialog.DialogReadAudioPms
import com.mtg.app.voicechanger.view.dialog.DialogRecordPms
import com.mtg.app.voicechanger.view.fragment.RecordFragment
import com.mtg.app.voicechanger.view.fragment.StopRecordFragment
import java.io.File


class RecordActivity : BaseActivity<ActivityRecordBinding>(ActivityRecordBinding::inflate),
    RecordFragment.Callback, StopRecordFragment.Callback {


    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, RecordActivity::class.java)
            context.startActivity(starter)
        }

        const val IMPORT_TO_CHANGE_VOICE = "IMPORT_TO_CHANGE_VOICE"
        const val IMPORT_TEXT_TO_SPEECH = "IMPORT_TEXT_TO_SPEECH"

        private const val CLICK_RECORD = 1
        private const val CLICK_SETTING = 2
        private const val CLICK_IMPORT = 3
        private const val CLICK_TEXT_TO_VOICE = 4
        private const val CLICK_FILE = 5
        private const val CLICK_VIDEO = 6
    }

//    private lateinit var textToVoiceDialog: TextToVoiceDialog
//    private lateinit var loadingDialog: LoadingDialog

    private val listFragment = listOf(
        RecordFragment().newInstance(),
        StopRecordFragment().newInstance()
//        SettingFragment().newInstance()
    )

    private var mTts: TextToSpeech? = null
    private var action: Int = 0
    private var textTTS: String = ""

    override fun onDestroy() {
        mTts?.shutdown()
        System.gc()
        super.onDestroy()
    }

    override fun initView() {
//        textToVoiceDialog = TextToVoiceDialog()
////        textToVoiceDialog.setOnDoneListener {
////            textTTS = it
////            try{
////                val checkIntent = Intent()
////                checkIntent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
////                checkTTSLauncher.launch(checkIntent)
////            }catch (e: Exception) {
////                shortToast(R.string.process_error)
////            }
////
////        }
//
////        loadingDialog = LoadingDialog()

        val fragment0 = listFragment[0]
        if (fragment0 is RecordFragment) {
            fragment0.setCallback(this)
        }
        val fragment1 = listFragment[1]
        if (fragment1 is StopRecordFragment) {
            fragment1.setCallback(this)
        }
//        val fragment2 = listFragment[2]
//        if (fragment2 is SettingFragment) {
//            fragment2.setCallback(this)
//        }
        createChildFragment(listFragment[0])

    }

    override fun addEvent() {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PermissionUtils.REQUEST_PERMISSION_RECORD) {
//            if (PermissionUtils.checkPermissionRecord(this)) {
//                if (PermissionUtils.checkPermissionReadWriteFile(this)) {
//                    if (action == CLICK_RECORD) {
//                        createChildFragment(listFragment[1])
//                    }
//                } else {
//                    requestPermissionReadWriteFile()
//                }
//            } else {
//                showDialogGoToSetting(PermissionUtils.REQUEST_PERMISSION_RECORD)
//            }
//        } else if (requestCode == PermissionUtils.REQUEST_PERMISSION_READ_WRITE) {
//            if (PermissionUtils.checkPermissionReadWriteFile(this)) {
//                when (action) {
//                    CLICK_RECORD -> {
//                        createChildFragment(listFragment[1])
//                    }
//
//                    CLICK_IMPORT -> {
//                        selectImportAudio()
//                    }
//
//                    CLICK_TEXT_TO_VOICE -> {
////                        textToVoiceDialog.show(supportFragmentManager, "TextToVoiceDialog")
//                    }
//                }
//            } else {
//                showDialogGoToSetting(PermissionUtils.REQUEST_PERMISSION_READ_WRITE)
//            }
//        }

        if (requestCode == PermissionUtils.REQUEST_PERMISSION_RECORD) {
            if (!PermissionUtils.checkPermissionRecord(this)) {
                showDialogRecordPms()
            } else {
                startCheckStoragePms()
            }
        }

        if (requestCode == PermissionUtils.REQUEST_PERMISSION_READ_AUDIO) {
            if (!PermissionUtils.checkReadAudioPms(this)) {
                showDialogReadAudioPms()
            } else {
                if (action == CLICK_RECORD) {
                    startRecordFlow()
                } else {
                    startImport()
                }
            }
        }
    }

    private fun showDialogRecordPms() {
        DialogRecordPms(this).setCallBack(OnActionCallback { key, data ->
            PermissionUtils.requestRecordPmsSettings(this)
        }).show()
    }

    override fun onRecord() {
        action = CLICK_RECORD
        if (PermissionUtils.checkPermissionRecord(this)) {
            startCheckStoragePms()
        } else {
            PermissionUtils.requestRecordAudioPms(this)
        }
    }

    override fun onSetting() {
        action = CLICK_SETTING
        createChildFragment(listFragment[2])
    }

    override fun onImport() {
        action = CLICK_IMPORT
    }

    override fun onTextToVoice() {
        action = CLICK_TEXT_TO_VOICE
    }

    override fun onFile() {
        action = CLICK_FILE
//        startActivity(Intent(this, FileVoiceActivity::class.java))
//        overridePendingTransition(
//            R.anim.anim_right_left_1,
//            R.anim.anim_right_left_2
//        )
    }

    override fun onVideo() {
        action = CLICK_VIDEO
//        startActivity(Intent(this, FileVideoActivity::class.java))
//        overridePendingTransition(
//            R.anim.anim_right_left_1,
//            R.anim.anim_right_left_2
//        )
    }

    override fun onClose() {
        action = 0
        createChildFragment(listFragment[0])
    }

    private fun createChildFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        transaction.replace(binding.layoutFragment.id, fragment).commit()
    }

    private fun selectImportAudio() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        importLauncher.launch(intent)
//        overridePendingTransition(
//            R.anim.anim_right_left_1,
//            R.anim.anim_right_left_2
//        )
    }

    private val importLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (it.data == null) return@registerForActivityResult

                val path = it.data!!.data?.getRealPath(this)
                if (path == null
                    || path.isEmpty()
                    || !File(path).exists()
                ) {
                    Toast.makeText(this, R.string.file_not_exist, Toast.LENGTH_LONG).show()
                    return@registerForActivityResult
                }

                goToChangeVoice(IMPORT_TO_CHANGE_VOICE, path)
            } else if (it.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.cancel, Toast.LENGTH_LONG).show()
            }
        }
    private fun goToChangeVoice(action: String, path: String) {
        val intent = Intent(this, ChangeVoiceActivity::class.java)
        intent.action = action
        intent.putExtra(Constants.PATH_FILE, path)
        startActivity(intent)
        overridePendingTransition(
            R.anim.anim_right_left_1,
            R.anim.anim_right_left_2
        )
    }


    private fun showDialogReadAudioPms() {
        DialogReadAudioPms(this).setCallBack(OnActionCallback { key, data ->
            PermissionUtils.requestReadAudioPmsSettings(this)
        }).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PermissionUtils.REQUEST_PERMISSION_READ_AUDIO) {
            if (!PermissionUtils.checkReadAudioPms(this)) {
                showDialogReadAudioPms()
            } else {
                if (action == CLICK_RECORD) {
                    startRecordFlow()
                } else {
                    startImport()
                }
            }
        }

        if (requestCode == PermissionUtils.REQUEST_PERMISSION_RECORD) {
            if (!PermissionUtils.checkPermissionRecord(this)) {
                showDialogRecordPms()
            } else {
                startCheckStoragePms()
            }
        }
    }

    private fun startCheckStoragePms() {
        if (!PermissionUtils.checkReadAudioPms(this)) {
            PermissionUtils.requestReadAudioPms(this)
        } else {
            startRecordFlow()
        }
    }

    private fun startRecordFlow() {
        val fragment1 = StopRecordFragment().newInstance()
        fragment1.setCallback(this)
        createChildFragment(fragment1)
    }

    private fun startImport() {
        ImportAudioFlow(this, object : ImportAudioFlow.Callback {
            override fun onNoPms() {
                PermissionUtils.requestReadAudioPms(this@RecordActivity)
            }

            override fun onNextScreen() {
                AudioChooserActivity.start(this@RecordActivity)
            }
        }).start()
    }

}