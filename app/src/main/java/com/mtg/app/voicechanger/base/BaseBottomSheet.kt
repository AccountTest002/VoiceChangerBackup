package com.mtg.app.voicechanger.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.documentmaster.documentscan.OnActionCallback;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mtg.app.voicechanger.R;

public abstract class BaseBottomSheet<B extends ViewDataBinding> extends BottomSheetDialogFragment {
    protected B binding;
    protected Context context;
    protected OnActionCallback callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        binding.getRoot().setBackground(getActivity().getDrawable(R.drawable.arch_corner_24));
        return binding.getRoot();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        addEvents();
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialogTheme;
    }

    protected abstract void initView();

    protected abstract void addEvents();

    protected abstract int getLayoutId();

    public void setCallBack(OnActionCallback callback){
        this.callback = callback;
    }
}
