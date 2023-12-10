package com.pro.foodorder.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.pro.foodorder.R;
import com.pro.foodorder.activity.ReviewActivity;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityAdminDetailOrderBinding;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.DateTimeUtils;

public class AdminOrderDetailActivity extends AppCompatActivity {

    private ActivityAdminDetailOrderBinding mActivityAdminDetailOrderBinding;
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mActivityAdminDetailOrderBinding = ActivityAdminDetailOrderBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminDetailOrderBinding.getRoot());

        getDataIntent();
        initToolbar();
        initView();

        mActivityAdminDetailOrderBinding.tvReview.setOnClickListener(v -> {
            reviewOrderList(mOrder);
        });
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            mOrder = (Order) bundleReceived.get(Constant.KEY_INTENT_ORDER_OBJECT);
        }
    }

    private void initToolbar() {
        mActivityAdminDetailOrderBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAdminDetailOrderBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityAdminDetailOrderBinding.toolbar.tvTitle.setText(getString(R.string.revenue));

        mActivityAdminDetailOrderBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {

        mActivityAdminDetailOrderBinding.tvId.setText(String.valueOf(mOrder.getId()));
        mActivityAdminDetailOrderBinding.tvEmail.setText(mOrder.getEmail());
        mActivityAdminDetailOrderBinding.tvName.setText(mOrder.getName());
        mActivityAdminDetailOrderBinding.tvPhone.setText(mOrder.getPhone());
        mActivityAdminDetailOrderBinding.tvAddress.setText(mOrder.getAddress());
        mActivityAdminDetailOrderBinding.tvMenu.setText(mOrder.getFoods());
        mActivityAdminDetailOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(mOrder.getId()));

        if(DataStoreManager.getUser().isAdmin()){
            mActivityAdminDetailOrderBinding.layoutReview.setVisibility(View.VISIBLE);
        } else {
            mActivityAdminDetailOrderBinding.layoutReview.setVisibility(View.GONE);
        }

        String strAmount = mOrder.getAmount() + Constant.CURRENCY;
        mActivityAdminDetailOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == mOrder.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        mActivityAdminDetailOrderBinding.tvPayment.setText(paymentMethod);

    }

    private void reviewOrderList(Order mOrder) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_ORDER_OBJECT, mOrder);
        GlobalFunction.startActivity(getApplicationContext(), ReviewActivity.class, bundle);
    }
}