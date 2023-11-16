package com.pro.foodorder.fragment.shipper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.ChangePasswordActivity;
import com.pro.foodorder.activity.admin.AdminReportActivity;
import com.pro.foodorder.activity.shipper.ShipperMainActivity;
import com.pro.foodorder.activity.SignInActivity;
import com.pro.foodorder.activity.shipper.ShipperReportActivity;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentShipperAccountBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.prefs.DataStoreManager;

public class ShipperAccountFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentShipperAccountBinding fragmentShipperAccountBinding =
                FragmentShipperAccountBinding.inflate(inflater, container, false);


        fragmentShipperAccountBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());

        fragmentShipperAccountBinding.layoutOrderHistory.setOnClickListener(v -> onClickReport());

        fragmentShipperAccountBinding.layoutReport.setOnClickListener(v -> onClickReport());

        fragmentShipperAccountBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());
        fragmentShipperAccountBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());

        return fragmentShipperAccountBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((ShipperMainActivity) getActivity()).setToolBar(getString(R.string.account));
        }
    }

    private void onClickReport() {
        GlobalFunction.startActivity(getActivity(), ShipperReportActivity.class);
    }

    private void onClickChangePassword() {
        GlobalFunction.startActivity(getActivity(), ChangePasswordActivity.class);
    }

    private void onClickSignOut() {
        if (getActivity() == null) {
            return;
        }
        FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseAuth.getInstance().signOut();
                DataStoreManager.setUser(null);
                GlobalFunction.startActivity(getActivity(), SignInActivity.class);
                getActivity().finishAffinity();
            }
        });
    }
}