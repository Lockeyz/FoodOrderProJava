package com.pro.foodorder.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.admin.AdminMainActivity;
import com.pro.foodorder.adapter.admin.AdminFeedbackAdapter;
import com.pro.foodorder.databinding.FragmentAdminFeedbackBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.model.Feedback;

import java.util.ArrayList;
import java.util.List;

public class AdminFeedbackFragment extends BaseFragment {

    private FragmentAdminFeedbackBinding mFragmentAdminFeedbackBinding;
    private List<Feedback> mListFeedback;
    private AdminFeedbackAdapter mAdminFeedbackAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminFeedbackBinding = FragmentAdminFeedbackBinding.inflate(inflater, container, false);

        initView();
        getListFeedback();
        return mFragmentAdminFeedbackBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.feedback));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminFeedbackBinding.rcvFeedback.setLayoutManager(linearLayoutManager);
    }

    public void getListFeedback() {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getALlFeedbackDatabaseReference()
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
                        mFragmentAdminFeedbackBinding.rcvFeedback.setAdapter(mAdminFeedbackAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}
