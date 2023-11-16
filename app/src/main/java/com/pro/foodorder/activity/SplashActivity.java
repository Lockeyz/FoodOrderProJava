package com.pro.foodorder.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.controls.Control;

import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.admin.AdminMainActivity;
import com.pro.foodorder.activity.shipper.ShipperMainActivity;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentAdminOrderBinding;
import com.pro.foodorder.model.User;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.StringUtil;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (DataStoreManager.getUser() != null && !StringUtil.isEmpty(DataStoreManager.getUser().getEmail())
                && getIntent().getExtras()!=null){
            // from notification
            String userId = getIntent().getExtras().getString("orderId");
            User currentUser = DataStoreManager.getUser();

            if (currentUser.isAdmin()){
                Intent intent = new Intent(this, AdminMainActivity.class);
                intent.putExtra("orderId", userId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            } else if (currentUser.isShipper()) {
                Intent intent = new Intent(this, ShipperMainActivity.class);
                intent.putExtra("orderId", userId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("orderId", userId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }

        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this::goToNextActivity, 2000);
        }

    }

    private void goToNextActivity() {
        if (DataStoreManager.getUser() != null && !StringUtil.isEmpty(DataStoreManager.getUser().getEmail())) {
            GlobalFunction.gotoMainActivity(this);
        } else {
            GlobalFunction.startActivity(this, SignInActivity.class);
        }
        finish();
    }
}