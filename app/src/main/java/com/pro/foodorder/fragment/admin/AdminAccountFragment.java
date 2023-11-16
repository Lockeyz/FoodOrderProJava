package com.pro.foodorder.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.FirebaseExceptionMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.admin.AdminFeedbackActivity;
import com.pro.foodorder.activity.admin.AdminMainActivity;
import com.pro.foodorder.activity.admin.AdminReportActivity;
import com.pro.foodorder.activity.ChangePasswordActivity;
import com.pro.foodorder.activity.SignInActivity;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentAdminAccountBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.prefs.DataStoreManager;

public class AdminAccountFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAdminAccountBinding fragmentAdminAccountBinding = FragmentAdminAccountBinding.inflate(inflater, container, false);

        fragmentAdminAccountBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());

        fragmentAdminAccountBinding.layoutReport.setOnClickListener(v -> onClickReport());
        fragmentAdminAccountBinding.layoutFeedback.setOnClickListener(v -> onClickFeedback());

        fragmentAdminAccountBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());
        fragmentAdminAccountBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());

        return fragmentAdminAccountBinding.getRoot();
    }



    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.account));
        }
    }

    private void onClickReport() {
        GlobalFunction.startActivity(getActivity(), AdminReportActivity.class);
    }

    private void onClickFeedback() {
        GlobalFunction.startActivity(getActivity(), AdminFeedbackActivity.class);
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
