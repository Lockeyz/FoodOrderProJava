package com.pro.foodorder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.adapter.MyVoucherAdapter;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityMyVoucherBinding;
import com.pro.foodorder.listener.IOnManageMyVoucherListener;
import com.pro.foodorder.model.MyVoucher;
import com.pro.foodorder.model.Voucher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyVoucherActivity extends AppCompatActivity {

    private ActivityMyVoucherBinding mActivityMyVoucherBinding;
    private List<MyVoucher> mListVoucher;
    private MyVoucherAdapter mMyVoucherAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMyVoucherBinding = ActivityMyVoucherBinding.inflate(getLayoutInflater());
        setContentView(mActivityMyVoucherBinding.getRoot());

        initToolbar();
        initView();
        getListVouchers();
    }

    private void initToolbar() {
        mActivityMyVoucherBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityMyVoucherBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityMyVoucherBinding.toolbar.tvTitle.setText("Danh sách voucher của tôi");
        mActivityMyVoucherBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
//        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getUid())
//                .get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()){
//                        User user = task.getResult().getValue(User.class);
//                        mActivityMyVoucherBinding.tvRewardPoint.setText(user.getRewardPoint()+"");
//                    }
//                });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityMyVoucherBinding.rcvMyVoucher.setLayoutManager(linearLayoutManager);
    }

    public void getListVouchers() {
        ControllerApplication.get(this).getAllMyVoucherDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mListVoucher != null) {
                            mListVoucher.clear();
                        } else {
                            mListVoucher = new ArrayList<>();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MyVoucher myVoucher = dataSnapshot.getValue(MyVoucher.class);
                            mListVoucher.add(0, myVoucher);
                        }
                        mMyVoucherAdapter = new MyVoucherAdapter(mListVoucher, new IOnManageMyVoucherListener(){
                            @Override
                            public void onClickUseVoucher(MyVoucher voucher) {
                                useVoucher(voucher);
                            }
                        });
                        mActivityMyVoucherBinding.rcvMyVoucher.setAdapter(mMyVoucherAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void useVoucher(MyVoucher voucher) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận sử dụng")
                .setMessage("Bạn có chắc muốn dùng voucher này với đơn hàng hiện tại?")
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (this == null) {
                        return;
                    }

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("myVoucherId", voucher.getId()+"");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();

                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

}