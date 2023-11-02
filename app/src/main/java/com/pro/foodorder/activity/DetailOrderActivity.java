package com.pro.foodorder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pro.foodorder.R;
import com.pro.foodorder.activity.admin.AdminReportActivity;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityDetailOrderBinding;
import com.pro.foodorder.databinding.ActivityDetailOrderBinding;
import com.pro.foodorder.listener.IOnSingleClickListener;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.model.User;
import com.pro.foodorder.utils.DateTimeUtils;

public class DetailOrderActivity extends AppCompatActivity {

    private ActivityDetailOrderBinding mActivityDetailOrderBinding;
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mActivityDetailOrderBinding = ActivityDetailOrderBinding.inflate(getLayoutInflater());
        setContentView(mActivityDetailOrderBinding.getRoot());

        getDataIntent();
        initToolbar();
        initView();
//        getListRevenue();
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            mOrder = (Order) bundleReceived.get(Constant.KEY_INTENT_ORDER_OBJECT);
        }
    }

    private void initToolbar() {
        mActivityDetailOrderBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityDetailOrderBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityDetailOrderBinding.toolbar.tvTitle.setText(getString(R.string.revenue));

        mActivityDetailOrderBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {

        mActivityDetailOrderBinding.tvId.setText(String.valueOf(mOrder.getId()));
        mActivityDetailOrderBinding.tvEmail.setText(mOrder.getEmail());
        mActivityDetailOrderBinding.tvName.setText(mOrder.getName());
        mActivityDetailOrderBinding.tvPhone.setText(mOrder.getPhone());
        mActivityDetailOrderBinding.tvAddress.setText(mOrder.getAddress());
        mActivityDetailOrderBinding.tvMenu.setText(mOrder.getFoods());
        mActivityDetailOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(mOrder.getId()));

        String strAmount = mOrder.getAmount() + Constant.CURRENCY;
        mActivityDetailOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == mOrder.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        mActivityDetailOrderBinding.tvPayment.setText(paymentMethod);

    }
}