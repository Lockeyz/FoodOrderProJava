package com.pro.foodorder.activity.shipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.activity.admin.AdminOrderDetailActivity;
import com.pro.foodorder.adapter.admin.RevenueAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityShipperReportBinding;
import com.pro.foodorder.listener.IOnManageOrderListener;
import com.pro.foodorder.listener.IOnSingleClickListener;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.utils.DateTimeUtils;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ShipperReportActivity extends AppCompatActivity {

    private ActivityShipperReportBinding mActivityShipperReportBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityShipperReportBinding = ActivityShipperReportBinding.inflate(getLayoutInflater());
        setContentView(mActivityShipperReportBinding.getRoot());


        initToolbar();
        initListener();
        getListRevenue();
    }

    private void initToolbar() {
        mActivityShipperReportBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityShipperReportBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityShipperReportBinding.toolbar.tvTitle.setText("Đơn hàng đã giao");
        mActivityShipperReportBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());

    }

    private void initListener() {
        mActivityShipperReportBinding.tvDateFrom.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                GlobalFunction.showDatePicker(ShipperReportActivity.this,
                        mActivityShipperReportBinding.tvDateFrom.getText().toString(), date -> {
                            mActivityShipperReportBinding.tvDateFrom.setText(date);
                            getListRevenue();
                        });
            }
        });

        mActivityShipperReportBinding.tvDateTo.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                GlobalFunction.showDatePicker(ShipperReportActivity.this,
                        mActivityShipperReportBinding.tvDateTo.getText().toString(), date -> {
                            mActivityShipperReportBinding.tvDateTo.setText(date);
                            getListRevenue();
                        });
            }
        });
    }

    private void getListRevenue() {
        ControllerApplication.get(this).getAllBookingDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Order> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (canAddOrder(order)) {
                        list.add(0, order);
                    }
                }
                handleDataHistories(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private boolean canAddOrder(@Nullable Order order) {
        if (order == null) {
            return false;
        }
        if (!order.isCompleted()) {
            return false;
        }
        if (order.getShipperId() == null
                || !order.getShipperId().contains(FirebaseAuth.getInstance().getUid())){
            return false;
        }
        String strDateFrom = mActivityShipperReportBinding.tvDateFrom.getText().toString();
        String strDateTo = mActivityShipperReportBinding.tvDateTo.getText().toString();
        if (StringUtil.isEmpty(strDateFrom) && StringUtil.isEmpty(strDateTo)) {
            return true;
        }
        String strDateOrder = DateTimeUtils.convertTimeStampToDate_2(order.getId());
        long longOrder = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateOrder));

        if (StringUtil.isEmpty(strDateFrom) && !StringUtil.isEmpty(strDateTo)) {
            long longDateTo = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateTo));
            return longOrder <= longDateTo;
        }
        if (!StringUtil.isEmpty(strDateFrom) && StringUtil.isEmpty(strDateTo)) {
            long longDateFrom = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateFrom));
            return longOrder >= longDateFrom;
        }
        long longDateTo = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateTo));
        long longDateFrom = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateFrom));
        return longOrder >= longDateFrom && longOrder <= longDateTo;
    }

    private void handleDataHistories(List<Order> list) {
        if (list == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityShipperReportBinding.rcvOrderHistory.setLayoutManager(linearLayoutManager);
        RevenueAdapter revenueAdapter = new RevenueAdapter(list, new IOnManageOrderListener() {
            @Override
            public void onClickDetailOrder(Order order) {
                detailOrderItem(order);
            }
        });
        mActivityShipperReportBinding.rcvOrderHistory.setAdapter(revenueAdapter);

        // Calculate total
        String strTotalValue = getTotalValues(list) + Constant.CURRENCY;
        mActivityShipperReportBinding.tvTotalValue.setText(strTotalValue);
    }

    private void detailOrderItem(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_ORDER_OBJECT, order);
        GlobalFunction.startActivity(getApplicationContext(), AdminOrderDetailActivity.class, bundle);
    }

    private int getTotalValues(List<Order> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int total = list.size()*10;

        return total;
    }
}