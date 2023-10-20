package com.pro.foodorder.fragment.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pro.foodorder.R;
import com.pro.foodorder.databinding.FragmentAdminShipperBinding;
import com.pro.foodorder.databinding.FragmentShipperAccountBinding;
import com.pro.foodorder.fragment.BaseFragment;

public class AdminShipperFragment extends BaseFragment {

    private FragmentAdminShipperBinding mfragmentAdminShipperBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mfragmentAdminShipperBinding = FragmentAdminShipperBinding.inflate(inflater, container,false);
//        initView();
//        initListener();
//        getListFood("");
        return mfragmentAdminShipperBinding.getRoot();
    }

    @Override
    protected void initToolbar() {

    }
}