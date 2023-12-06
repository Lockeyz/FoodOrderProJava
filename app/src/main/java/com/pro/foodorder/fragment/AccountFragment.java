package com.pro.foodorder.fragment;

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
import com.pro.foodorder.activity.ContactActivity;
import com.pro.foodorder.activity.FeedbackActivity;
import com.pro.foodorder.activity.InformationActivity;
import com.pro.foodorder.activity.MainActivity;
import com.pro.foodorder.activity.OrderHistoryActivity;
import com.pro.foodorder.activity.SignInActivity;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentAccountBinding;
import com.pro.foodorder.prefs.DataStoreManager;

public class AccountFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAccountBinding fragmentAccountBinding = FragmentAccountBinding.inflate(inflater, container, false);

        fragmentAccountBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());

        fragmentAccountBinding.layoutInformation.setOnClickListener(v -> onClickInformation());
        fragmentAccountBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());
        fragmentAccountBinding.layoutOrderHistory.setOnClickListener(v -> onClickOrderHistory());
        fragmentAccountBinding.layoutFeedback.setOnClickListener(v -> onClickFeedback());
        fragmentAccountBinding.layoutContact.setOnClickListener(v -> onClickContact());
        fragmentAccountBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());

        return fragmentAccountBinding.getRoot();
    }




    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.account));
        }
    }

    private void onClickInformation() {
        GlobalFunction.startActivity(getActivity(), InformationActivity.class);
    }

    private void onClickOrderHistory() {
        GlobalFunction.startActivity(getActivity(), OrderHistoryActivity.class);
    }

    private void onClickChangePassword() {
        GlobalFunction.startActivity(getActivity(), ChangePasswordActivity.class);
    }

    private void onClickFeedback() {
        GlobalFunction.startActivity(getActivity(), FeedbackActivity.class);
    }

    private void onClickContact() {
        GlobalFunction.startActivity(getActivity(), ContactActivity.class);
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
