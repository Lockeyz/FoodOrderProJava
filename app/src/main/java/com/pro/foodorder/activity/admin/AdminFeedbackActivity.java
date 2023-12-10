package com.pro.foodorder.activity.admin;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.BaseActivity;
import com.pro.foodorder.adapter.admin.AdminFeedbackAdapter;
import com.pro.foodorder.databinding.ActivityAdminFeedbackBinding;
import com.pro.foodorder.model.Feedback;

import java.util.ArrayList;
import java.util.List;

public class AdminFeedbackActivity extends BaseActivity {

    private ActivityAdminFeedbackBinding mActivityAdminFeedbackBinding;
    private List<Feedback> mListFeedback;
    private AdminFeedbackAdapter mAdminFeedbackAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mActivityAdminFeedbackBinding = ActivityAdminFeedbackBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminFeedbackBinding.getRoot());

        initToolbar();
        initView();
        getListFeedback();
    }

    private void initToolbar() {
        mActivityAdminFeedbackBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAdminFeedbackBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityAdminFeedbackBinding.toolbar.tvTitle.setText(getString(R.string.order_history));
        mActivityAdminFeedbackBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityAdminFeedbackBinding.rcvFeedback.setLayoutManager(linearLayoutManager);
    }

    public void getListFeedback() {

        ControllerApplication.get(this).getALlFeedbackDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mListFeedback != null) {
                            mListFeedback.clear();
                        } else {
                            mListFeedback = new ArrayList<>();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Feedback feedback = dataSnapshot.getValue(Feedback.class);
                            if (feedback != null) {
                                mListFeedback.add(0, feedback);
                            }
                        }
                        mAdminFeedbackAdapter = new AdminFeedbackAdapter(mListFeedback);
                        mActivityAdminFeedbackBinding.rcvFeedback.setAdapter(mAdminFeedbackAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
    
}