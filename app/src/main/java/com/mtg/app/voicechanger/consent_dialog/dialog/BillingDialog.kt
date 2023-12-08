package com.mtg.app.voicechanger.consent_dialog.dialog

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.common.control.interfaces.PurchaseCallback
import com.common.control.manager.PurchaseManager
import com.mtg.app.voicechanger.R
import com.mtg.app.voicechanger.consent_dialog.base.BaseDialog
import com.mtg.app.voicechanger.databinding.DialogBillingBinding
import com.mtg.app.voicechanger.utils.EventLogger.Companion.getInstance
import com.mtg.app.voicechanger.utils.constant.Constants
import com.mtg.app.voicechanger.view.activity.RecordActivity
import java.util.Objects

class BillingDialog : BaseDialog<DialogBillingBinding?>(), PurchaseCallback {
    override val layoutId: Int
        protected get() = R.layout.dialog_billing

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayRectangle = Rect()
        Objects.requireNonNull(
            Objects.requireNonNull(
                dialog
            )?.window
        )?.decorView?.getWindowVisibleDisplayFrame(displayRectangle)
        binding!!.ctContainer.minWidth =
            Resources.getSystem().displayMetrics.widthPixels
        binding!!.ctContainer.minHeight =
            Resources.getSystem().displayMetrics.heightPixels
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun initView() {
        if (!isChooseNoAds) {
            isChooseNoAds = true
        }
        PurchaseManager.getInstance().setCallback(this)
        isCancelable = false
        binding!!.tvPriceForever.text =
            PurchaseManager.getInstance().getPrice(PRODUCT_ID)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun addEvent() {
//        binding.tvPrice.setText(PurchaseManager.getInstance().getPrice(PRODUCT_LIFETIME));
//
//        binding.btBuy.setOnClickListener(v -> {
//            PurchaseManager.getInstance().launchPurchase(requireActivity(), PRODUCT_LIFETIME);
//        });
//
//        binding.btCancel.setOnClickListener(v -> {
//            callback.callback(KEY_CANCEL, null);
//        });
//        binding.btClose.setOnClickListener(v -> {
//            callback.callback(KEY_CANCEL, null);
//        });
        binding!!.ll7day.setOnClickListener { v: View? ->
            Objects.requireNonNull(getInstance())?.logEvent("click_intro_include_ads")
            isChooseNoAds = false
            binding!!.ll7day.background = requireContext().getDrawable(R.drawable.bg_choose_billing)
            binding!!.llForever.background = requireContext().getDrawable(R.drawable.bg_radius_8)
            binding!!.top7day.background =
                requireContext().getDrawable(R.drawable.bg_selected_billing)
            binding!!.topForever.background =
                requireContext().getDrawable(R.drawable.bg_select_billing)
            binding!!.tv7day.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding!!.tvForever.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.c4c4c4
                )
            )
        }
        binding!!.llForever.setOnClickListener { v: View? ->
            Objects.requireNonNull(getInstance())?.logEvent("click_intro_remove_ads")
            isChooseNoAds = true
            binding!!.llForever.background =
                requireContext().getDrawable(R.drawable.bg_choose_billing)
            binding!!.ll7day.background = requireContext().getDrawable(R.drawable.bg_radius_8)
            binding!!.topForever.background =
                requireContext().getDrawable(R.drawable.bg_selected_billing)
            binding!!.top7day.background =
                requireContext().getDrawable(R.drawable.bg_select_billing)
            binding!!.tvForever.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding!!.tv7day.setTextColor(ContextCompat.getColor(requireContext(), R.color.c4c4c4))
        }
        binding!!.ivClose.setOnClickListener { v: View? ->
//            if (Common.INSTANCE.isNetworkAvailable(requireContext())) {
//                MainActivity.start(requireContext());
//                dismiss();
//                callback.callback(KEY_CANCEL, null);
//            } else {
//                Toast.makeText(requireContext(), getString(R.string.please_check_the_internet_or_vpn_and_try_again), Toast.LENGTH_SHORT).show();
//                callback.callback(KEY_CANCEL, null);
//            }
            callback!!.callback(Constants.KEY_CANCEL, null)
        }
        binding!!.btnContinue.setOnClickListener { v: View? ->
            Objects.requireNonNull(getInstance())?.logEvent("click_intro_continue")
            if (isChooseNoAds) {
//                if (Common.INSTANCE.isNetworkAvailable(requireContext())) {
//                    MainActivity.start(requireContext());
//                    dismiss();
//                } else {
//                    Toast.makeText(requireContext(), getString(R.string.please_check_the_internet_or_vpn_and_try_again), Toast.LENGTH_SHORT).show();
//                }
                startPurchaseForever()
            } else {
                callback!!.callback(Constants.KEY_CANCEL, null as Any?)
            }
        }
    }

    private fun startPurchaseForever() {
        PurchaseManager.getInstance().launchPurchase(requireActivity(), PRODUCT_ID)
    }

    //    private void startPurchase7Day() {
    //        PurchaseManager.getInstance().launchPurchase(requireActivity(), PRODUCT_SUBS);
    //    }
    override fun dismiss() {
        super.dismiss()
        INSTANCE = null
    }

    override fun purchaseSuccess() {
        val progressBar = ProgressDialog(requireContext())
        progressBar.setTitle(getString(R.string.please_wait))
        progressBar.setMessage(getString(R.string.processing_your_pack))
        progressBar.show()
        Handler().postDelayed({
            requireActivity().finishAffinity()
            startActivity(Intent(requireContext(), RecordActivity::class.java))
        }, 2000)
    }

    override fun purchaseFail() {}
    fun show(activity: AppCompatActivity) {
        try {
            show(activity.supportFragmentManager, javaClass.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val PRODUCT_ID = "prank.forever"
        const val PRODUCT_SUBS = "prank.premium.7days"
        private var INSTANCE: BillingDialog? = null
        private var isChooseNoAds = true
        @JvmStatic
        val instance: BillingDialog?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = BillingDialog()
                }
                return INSTANCE
            }
    }
}