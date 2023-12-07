package com.pro.foodorder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.ChangePasswordActivity;
import com.pro.foodorder.activity.ContactActivity;
import com.pro.foodorder.activity.ExchangeVoucherActivity;
import com.pro.foodorder.activity.FeedbackActivity;
import com.pro.foodorder.activity.InformationActivity;
import com.pro.foodorder.activity.MainActivity;
import com.pro.foodorder.activity.OrderHistoryActivity;
import com.pro.foodorder.activity.SignInActivity;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentAccountBinding;
import com.pro.foodorder.model.User;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.GlideUtils;

public class AccountFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAccountBinding mFragmentAccountBinding = FragmentAccountBinding.inflate(inflater, container, false);

        FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        User user = task.getResult().getValue(User.class);
                        GlideUtils.loadUrl(user.getAvatar(), mFragmentAccountBinding.imageAvatar);
                    }
                });

        mFragmentAccountBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());

        mFragmentAccountBinding.layoutInformation.setOnClickListener(v -> onClickInformation());
        mFragmentAccountBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());
        mFragmentAccountBinding.layoutExchangePoint.setOnClickListener(v -> onClickExchangePoint());
        mFragmentAccountBinding.layoutOrderHistory.setOnClickListener(v -> onClickOrderHistory());
        mFragmentAccountBinding.layoutFeedback.setOnClickListener(v -> onClickFeedback());
        mFragmentAccountBinding.layoutContact.setOnClickListener(v -> onClickContact());
        mFragmentAccountBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());

        return mFragmentAccountBinding.getRoot();
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
    private void onClickExchangePoint() {
        GlobalFunction.startActivity(getActivity(), ExchangeVoucherActivity.class);
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
