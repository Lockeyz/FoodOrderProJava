package com.pro.foodorder.activity;


import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityFeedbackBinding;
import com.pro.foodorder.databinding.ActivityOrderHistoryBinding;
import com.pro.foodorder.model.Feedback;
import com.pro.foodorder.model.User;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.StringUtil;

public class FeedbackActivity extends BaseActivity {

    User mUser;
    private ActivityFeedbackBinding mActivityFeedbackBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityFeedbackBinding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(mActivityFeedbackBinding.getRoot());

        initToolbar();
        initView();


        mActivityFeedbackBinding.tvSendFeedback.setOnClickListener(v -> onClickSendFeedback());
    }

    private void initView() {
        mActivityFeedbackBinding.edtEmail.setText(DataStoreManager.getUser().getEmail());
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        mUser = task.getResult().getValue(User.class);
                        mActivityFeedbackBinding.edtName.setText(mUser.getName());
                        mActivityFeedbackBinding.edtPhone.setText(mUser.getPhone());
                    }
                });
    }

    private void onClickSendFeedback() {

        String strName = mActivityFeedbackBinding.edtName.getText().toString();
        String strPhone = mActivityFeedbackBinding.edtPhone.getText().toString();
        String strEmail = mActivityFeedbackBinding.edtEmail.getText().toString();
        String strComment = mActivityFeedbackBinding.edtComment.getText().toString();

        if (StringUtil.isEmpty(strName)) {
            GlobalFunction.showToastMessage(this, getString(R.string.name_require));
        } else if (StringUtil.isEmpty(strComment)) {
            GlobalFunction.showToastMessage(this, getString(R.string.comment_require));
        } else {
            showProgressDialog(true);
            Feedback feedback = new Feedback(strName, strPhone, strEmail, strComment);
            ControllerApplication.get(this).getALlFeedbackDatabaseReference()
                    .child(String.valueOf(System.currentTimeMillis()))
                    .setValue(feedback, (databaseError, databaseReference) -> {
                        showProgressDialog(false);
                        sendFeedbackSuccess();
                    });
        }
    }

    public void sendFeedbackSuccess() {
        GlobalFunction.hideSoftKeyboard(this);
        GlobalFunction.showToastMessage(this, getString(R.string.send_feedback_success));
        mActivityFeedbackBinding.edtName.setText("");
        mActivityFeedbackBinding.edtPhone.setText("");
        mActivityFeedbackBinding.edtComment.setText("");
    }

    protected void initToolbar() {
        mActivityFeedbackBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityFeedbackBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityFeedbackBinding.toolbar.tvTitle.setText("Phản hồi");
        mActivityFeedbackBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }
}