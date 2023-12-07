package com.pro.foodorder.activity.admin;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.BaseActivity;
import com.pro.foodorder.activity.OrderHistoryActivity;
import com.pro.foodorder.adapter.OrderAdapter;
import com.pro.foodorder.adapter.admin.AdminVoucherAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityAdminVoucherBinding;
import com.pro.foodorder.listener.IOnManageVoucherListener;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.model.Voucher;
import com.pro.foodorder.prefs.DataStoreManager;

import java.util.ArrayList;
import java.util.List;

public class AdminVoucherActivity extends BaseActivity {

    private ActivityAdminVoucherBinding mActivityAdminVoucherBinding;
    private List<Voucher> mListVoucher;
    private AdminVoucherAdapter mAdminVoucherAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminVoucherBinding = ActivityAdminVoucherBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminVoucherBinding.getRoot());

        initToolbar();
        initView();
        getListVouchers();

        mActivityAdminVoucherBinding.btnAddVoucher.setOnClickListener(v -> onClickAddVoucher());
    }

    private void initToolbar() {
        mActivityAdminVoucherBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAdminVoucherBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityAdminVoucherBinding.toolbar.tvTitle.setText("Danh sách voucher");
        mActivityAdminVoucherBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityAdminVoucherBinding.rcvVoucher.setLayoutManager(linearLayoutManager);
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
                        mAdminVoucherAdapter = new AdminVoucherAdapter(mListVoucher, new IOnManageVoucherListener(){
                            @Override
                            public void onClickUpdateVoucher(Voucher voucher) {
                                onClickEditVoucher(voucher);
                            }
                            @Override
                            public void onClickDeleteVoucher(Voucher voucher) {
                                deleteVoucherItem(voucher);
                            }

                            @Override
                            public void onClickExchangeVoucher(Voucher voucher) {

                            }
                        });
                        mActivityAdminVoucherBinding.rcvVoucher.setAdapter(mAdminVoucherAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void onClickEditVoucher(Voucher voucher) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_VOUCHER_OBJECT, voucher);
        GlobalFunction.startActivity(this, AddVoucherActivity.class, bundle);
    }

    private void deleteVoucherItem(Voucher voucher) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (this == null) {
                        return;
                    }
                    ControllerApplication.get(this).getAllFoodDatabaseReference()
                            .child(String.valueOf(voucher.getId())).removeValue((error, ref) ->
                                    Toast.makeText(this,
                                            "Xóa voucher thành công!", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void onClickAddVoucher() {
        GlobalFunction.startActivity(this, AddVoucherActivity.class);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mAdminVoucherAdapter != null) {
//            mAdminVoucherAdapter.release();
//        }
//    }
}