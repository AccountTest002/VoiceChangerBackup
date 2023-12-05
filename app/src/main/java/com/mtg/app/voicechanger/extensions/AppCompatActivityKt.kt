package com.mtg.app.voicechanger.extensions

import android.app.Activity
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mtg.app.voicechanger.utils.PlayerHelper
import kotlin.jvm.internal.Intrinsics


object AppCompatActivityKt {
    fun hideBottomNavigationBar(appCompatActivity: AppCompatActivity?) {
        Intrinsics.checkNotNullParameter(appCompatActivity, "<this>")
    }

    fun getVibrator(context: Context): Vibrator? {
        Intrinsics.checkNotNullParameter(context, "<this>")
        if (Build.VERSION.SDK_INT >= 31) {
            val systemService: Any = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
            Intrinsics.checkNotNull(
                systemService,
                "null cannot be cast to non-null type android.os.VibratorManager"
            )
            return (systemService as VibratorManager).defaultVibrator
        }
        val systemService2: Any = context.getSystemService(Context.VIBRATOR_SERVICE)
        return if (systemService2 is Vibrator) {
            systemService2
        } else null
    }

    fun getVibrator(fragment: Fragment): Vibrator? {
        Intrinsics.checkNotNullParameter(fragment, "<this>")
        val systemService: Any = fragment.requireContext().getSystemService(Context.VIBRATOR_SERVICE)
        return if (systemService is Vibrator) {
            systemService
        } else null
    }

    fun getPlayerHelper(appCompatActivity: AppCompatActivity?): PlayerHelper? {
        Intrinsics.checkNotNullParameter(appCompatActivity, "<this>")
        return appCompatActivity?.let { PlayerHelper(it) }
    }

    fun changeStatusBarColor(appCompatActivity: AppCompatActivity?, activity: Activity, i: Int) {
        Intrinsics.checkNotNullParameter(appCompatActivity, "<this>")
        Intrinsics.checkNotNullParameter(activity, "activity")
        val window = activity.window
        window.clearFlags(67108864)
        window.addFlags(Int.MIN_VALUE)
        window.statusBarColor = ContextCompat.getColor(activity, i)
    }

    fun setFullScreenActivity(appCompatActivity: AppCompatActivity) {
        Intrinsics.checkNotNullParameter(appCompatActivity, "<this>")
        appCompatActivity.window.decorView.systemUiVisibility = 1280
        appCompatActivity.window.statusBarColor = 0
    }

    fun requestNotificationPermission(appCompatActivity: AppCompatActivity?) {
        Intrinsics.checkNotNullParameter(appCompatActivity, "<this>")
        ActivityCompat.requestPermissions(
            appCompatActivity!!,
            arrayOf("android.permission.POST_NOTIFICATIONS"),
            104
        )
    }

    fun showNotice(context: Context?, msg: String?) {
        Intrinsics.checkNotNullParameter(context, "<this>")
        Intrinsics.checkNotNullParameter(msg, "msg")
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showNotice(context: Context?, i: Int) {
        Intrinsics.checkNotNullParameter(context, "<this>")
        Toast.makeText(context, i, Toast.LENGTH_SHORT).show()
    }

    fun vibrateOneShot(vibrator: Vibrator, j: Long) {
        Intrinsics.checkNotNullParameter(vibrator, "<this>")
        vibrator.cancel()
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(j, -1))
        } else {
            vibrator.vibrate(j)
        }
    }

    fun setVibrationSettings(context: Context, z: Boolean) {
        Intrinsics.checkNotNullParameter(context, "context")
        if (Build.VERSION.SDK_INT >= 31) {
            val createOneShot = VibrationEffect.createOneShot(1000L, -1)
            val systemService: Any = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
            Intrinsics.checkNotNull(
                systemService,
                "null cannot be cast to non-null type android.os.VibratorManager"
            )
            val vibratorManager = systemService as VibratorManager
            if (z) {
                vibratorManager.defaultVibrator.vibrate(createOneShot)
            } else {
                vibratorManager.defaultVibrator.cancel()
            }
        } else if (Build.VERSION.SDK_INT >= 26) {
            val createOneShot2 = VibrationEffect.createOneShot(1000L, -1)
            val systemService2: Any = context.getSystemService(Context.VIBRATOR_SERVICE)
            Intrinsics.checkNotNull(
                systemService2,
                "null cannot be cast to non-null type android.os.Vibrator"
            )
            val vibrator = systemService2 as Vibrator
            if (z) {
                vibrator.vibrate(createOneShot2)
            } else {
                vibrator.cancel()
            }
        } else {
            val systemService3: Any = context.getSystemService(Context.VIBRATOR_SERVICE)
            Intrinsics.checkNotNull(
                systemService3,
                "null cannot be cast to non-null type android.os.Vibrator"
            )
            val vibrator2 = systemService3 as Vibrator
            if (z) {
                vibrator2.vibrate(1000L)
            } else {
                vibrator2.cancel()
            }
        }
        val unit = Unit
    }

    fun  /* synthetic */`vibrateWaveform$default`(
        vibrator: Vibrator,
        jArr: LongArray?,
        i: Int,
        i2: Int,
        obj: Any?
    ) {
        var i = i
        if (i2 and 2 != 0) {
            i = 1
        }
        vibrateWaveform(vibrator, jArr, i)
    }

    fun vibrateWaveform(vibrator: Vibrator, pattern: LongArray?, i: Int) {
        Intrinsics.checkNotNullParameter(vibrator, "<this>")
        Intrinsics.checkNotNullParameter(pattern, "pattern")
        vibrator.cancel()
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, intArrayOf(0, -1, 0), i))
        } else {
            vibrator.vibrate(pattern, i)
        }
    }

    fun isHasFlashLight(context: Context): Boolean {
        Intrinsics.checkNotNullParameter(context, "<this>")
        return context.packageManager
            .hasSystemFeature("android.hardware.camera.flash") && context.packageManager
            .hasSystemFeature("android.hardware.camera")
    }

    fun turnFlashlightOn(context: Context) {
        Intrinsics.checkNotNullParameter(context, "<this>")
        adjustFlashlight(context, true)
    }


    fun  /* synthetic */`adjustFlashlight$default`(
        context: Context,
        z: Boolean,
        i: Int,
        obj: Any?
    ) {
        var z = z
        if (i and 1 != 0) {
            z = true
        }
        adjustFlashlight(context, z)
    }

    fun adjustFlashlight(context: Context, z: Boolean) {
        Intrinsics.checkNotNullParameter(context, "<this>")
        if (isHasFlashLight(context)) {
            try {
                val systemService: Any = context.getSystemService(Context.CAMERA_SERVICE)
                Intrinsics.checkNotNull(
                    systemService,
                    "null cannot be cast to non-null type android.hardware.camera2.CameraManager"
                )
                val cameraManager = systemService as CameraManager
                val str = cameraManager.cameraIdList[0]
                Intrinsics.checkNotNullExpressionValue(str, "camManager.cameraIdList[0]")
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(str)
                Intrinsics.checkNotNullExpressionValue(
                    cameraCharacteristics,
                    "camManager.getCameraCharacteristics(cameraId)"
                )
                val bool = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                Log.d("5555555666666666", "$str   $bool")
                if (Intrinsics.areEqual(bool as Any?, true as Any)) {
                    cameraManager.setTorchMode(str, z)
                }
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            } catch (e2: Exception) {
                e2.printStackTrace()
            }
        }
    }

    fun turnFlashlightOff(context: Context) {
        Intrinsics.checkNotNullParameter(context, "<this>")
        adjustFlashlight(context, false)
    }

//    fun getAllGuns(): List<RealGun> {
//        val arrayList = mutableListOf<RealGun>()
//        arrayList.add(
//            RealGun(
//                1,
//                Constants.AK47,
//                30,
//                "gun1.webp",
//                R.raw.sound_gun_ak47,
//                "",
//                "",
//                "",
//                "3.47 kg",
//                "415mm",
//                0.5f,
//                true,
//                R.drawable.im_ak47,
//                "#2DA89B",
//                "#00FFE5",
//                R.drawable.im_big_ak47
//            )
//        )
//        arrayList.add(
//            RealGun(
//                2,
//                Constants.AK47_DINO,
//                30,
//                "gun2.webp",
//                R.raw.sound_gun_ak47,
//                "",
//                "",
//                "",
//                "3.47 kg",
//                "415mm",
//                0.5f,
//                true,
//                R.drawable.im_ak47_dino,
//                "#A88D2D",
//                "#FFB800",
//                R.drawable.im_big_ak47_dino
//            )
//        )
//        arrayList.add(
//            RealGun(
//                3,
//                Constants.REVOLVER,
//                6,
//                "gun3.webp",
//                R.raw.sound_gun_coltpython357,
//                "",
//                "",
//                "",
//                "1.4 kg",
//                "4 - 8 inches",
//                0.5f,
//                true,
//                R.drawable.im_revolver,
//                "#2DA848",
//                "#FFEE97",
//                R.drawable.im_big_revolver
//            )
//        )
//        arrayList.add(
//            RealGun(
//                4,
//                Constants.RIFLES,
//                6,
//                "gun4.webp",
//                R.raw.sound_gun_scarl,
//                "",
//                "",
//                "",
//                "1.4 kg",
//                "4 - 8 inches",
//                0.5f,
//                true,
//                R.drawable.im_rifles,
//                "#E88BFF",
//                "#7C4FFF",
//                R.drawable.im_big_rifles
//            )
//        )
//        arrayList.add(
//            RealGun(
//                5,
//                Constants.MACHINE_GUN,
//                6,
//                "gun4.webp",
//                R.raw.sound_gun_m24,
//                "",
//                "",
//                "",
//                "1.4 kg",
//                "4 - 8 inches",
//                0.5f,
//                true,
//                R.drawable.im_machine_gun,
//                "#FF8B8B",
//                "#A52C2C",
//                R.drawable.im_big_machine_gun
//            )
//        )
//        arrayList.add(
//            RealGun(
//                6,
//                Constants.BLASTER,
//                6,
//                "gun4.webp",
//                R.raw.sound_gun_m416,
//                "",
//                "",
//                "",
//                "1.4 kg",
//                "4 - 8 inches",
//                0.5f,
//                true,
//                R.drawable.im_blaster,
//                "#FFFFFF",
//                "#FFEE97",
//                R.drawable.im_big_blaster
//            )
//        )
//        arrayList.add(
//            RealGun(
//                7,
//                Constants.ACR,
//                6,
//                "gun4.webp",
//                R.raw.sound_gun_m416,
//                "",
//                "",
//                "",
//                "1.4 kg",
//                "4 - 8 inches",
//                0.5f,
//                true,
//                R.drawable.im_acr,
//                "#2DA89B",
//                "#00FFE5",
//                R.drawable.im_big_acr
//            )
//        )
//        arrayList.add(
//            RealGun(
//                8,
//                Constants.HK_416,
//                6,
//                "gun4.webp",
//                R.raw.sound_gun_groza,
//                "",
//                "",
//                "",
//                "1.4 kg",
//                "4 - 8 inches",
//                0.5f,
//                true,
//                R.drawable.im_hk_416,
//                "#A88D2D",
//                "#FFB800",
//                R.drawable.im_big_hk_416
//            )
//        )
//        arrayList.add(
//            RealGun(
//                9,
//                Constants.AWM,
//                6,
//                "gun4.webp",
//                R.raw.sound_gun_awm,
//                "",
//                "",
//                "",
//                "1.4 kg",
//                "4 - 8 inches",
//                0.5f,
//                true,
//                R.drawable.im_remington,
//                "#2DA848",
//                "#FFEE97",
//                R.drawable.im_big_remington
//            )
//        )
//        arrayList.add(
//            RealGun(
//                10,
//                Constants.M24,
//                6,
//                "gun4.webp",
//                R.raw.sound_gun_m24,
//                "",
//                "",
//                "",
//                "1.4 kg",
//                "4 - 8 inches",
//                0.5f,
//                true,
//                R.drawable.im_m98,
//                "#E88BFF",
//                "#7C4FFF",
//                R.drawable.im_big_m98
//            )
//        )
//        return arrayList
//    }

}