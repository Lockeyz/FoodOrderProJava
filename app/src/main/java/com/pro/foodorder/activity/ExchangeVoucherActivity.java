package com.pro.foodorder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.admin.AdminReportActivity;
import com.pro.foodorder.adapter.VoucherAdapter;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityExchangeVoucherBinding;
import com.pro.foodorder.listener.IOnManageVoucherListener;
import com.pro.foodorder.model.MyVoucher;
import com.pro.foodorder.model.User;
import com.pro.foodorder.model.Voucher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeVoucherActivity extends AppCompatActivity {

    private ActivityExchangeVoucherBinding mActivityExchangeVoucherBinding;
    private List<Voucher> mListVoucher;
    private VoucherAdapter mVoucherAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityExchangeVoucherBinding = ActivityExchangeVoucherBinding.inflate(getLayoutInflater());
        setContentView(mActivityExchangeVoucherBinding.getRoot());

        initToolbar();
        initView();
        getListVouchers();
    }

    private void initToolbar() {
        mActivityExchangeVoucherBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityExchangeVoucherBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityExchangeVoucherBinding.toolbar.tvTitle.setText("Quy đổi điểm thưởng sang voucher");
        mActivityExchangeVoucherBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       User user = task.getResult().getValue(User.class);
                       mActivityExchangeVoucherBinding.tvRewardPoint.setText(user.getRewardPoint()+"");
                   }
                });

        mActivityExchangeVoucherBinding.tvMyVoucher.setOnClickListener(v -> goToMyVoucher());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityExchangeVoucherBinding.rcvVoucher.setLayoutManager(linearLayoutManager);
    }

    public void getListVouchers() {
        ControllerApplication.get(this).getAllVoucherDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mListVoucher != null) {
                            mListVoucher.clear();
                        } else {
                            mListVoucher = new ArrayList<>();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Voucher voucher = dataSnapshot.getValue(Voucher.class);
                            mListVoucher.add(0, voucher);
                        }
                        mVoucherAdapter = new VoucherAdapter(mListVoucher, new IOnManageVoucherListener(){
                            @Override
                            public void onClickUpdateVoucher(Voucher voucher) {

                            }
                            @Override
                            public void onClickDeleteVoucher(Voucher voucher) {

                            }

                            @Override
                            public void onClickExchangeVoucher(Voucher voucher) {
                                exchangeVoucher(voucher);
                            }

                        });
                        mActivityExchangeVoucherBinding.rcvVoucher.setAdapter(mVoucherAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void exchangeVoucher(Voucher voucher) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đổi")
                .setMessage("Bạn có chắc muốn đổi lấy voucher này?")
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (this == null) {
                        return;
                    }

                    int myRewardPoint = Integer.parseInt(mActivityExchangeVoucherBinding.tvRewardPoint.getText().toString());
                    if (voucher.getConversionPoint() > myRewardPoint){
                        Toast.makeText(this, "Không đủ điểm thưởng để đổi!", Toast.LENGTH_SHORT).show();
                    } else {
                        int conversionPoint = voucher.getConversionPoint();
                        myRewardPoint = myRewardPoint - conversionPoint;
                        Map<String, Object> map = new HashMap<>();
                        map.put("rewardPoint", myRewardPoint);
                        ControllerApplication.get(this).getAllUserDatabaseReference()
                                .child(FirebaseAuth.getInstance().getUid()).updateChildren(map);

                        long myVoucherId = System.currentTimeMillis();
                        MyVoucher myVoucher = new MyVoucher(myVoucherId, voucher.getId()+"", FirebaseAuth.getInstance().getUid(),
                                voucher.getName(), voucher.getImage(), voucher.getValue(),
                                voucher.getCondition(), voucher.getConversionPoint());

                        ControllerApplication.get(this).getAllMyVoucherDatabaseReference()
                                .child(String.valueOf(myVoucherId)).setValue(myVoucher)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()){
                                        Toast.makeText(this, "Quy đổi voucher thành công!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "Không thể quy đổi voucher!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void goToMyVoucher() {
        GlobalFunction.startActivity(this, MyVoucherActivity.class);
    }
}