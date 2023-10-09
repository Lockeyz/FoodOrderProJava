package com.pro.foodorder.activity.shipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pro.foodorder.R;
import com.pro.foodorder.adapter.AdminViewPagerAdapter;
import com.pro.foodorder.adapter.shipper.ShipperViewPagerAdapter;
import com.pro.foodorder.databinding.ActivityAdminMainBinding;
import com.pro.foodorder.databinding.ActivityShipperMainBinding;

public class ShipperMainActivity extends AppCompatActivity {

    private ActivityShipperMainBinding mActivityShipperMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityShipperMainBinding = ActivityShipperMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityShipperMainBinding.getRoot());

        mActivityShipperMainBinding.viewpager2.setUserInputEnabled(false);
        ShipperViewPagerAdapter shipperViewPagerAdapter = new ShipperViewPagerAdapter(this);
        mActivityShipperMainBinding.viewpager2.setAdapter(shipperViewPagerAdapter);

        mActivityShipperMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mActivityShipperMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;

                    case 1:
                        mActivityShipperMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_feedback).setChecked(true);
                        break;

                    case 2:
                        mActivityShipperMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_order).setChecked(true);
                        break;

                    case 3:
                        mActivityShipperMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_account).setChecked(true);
                        break;
                }
            }
        });

        mActivityShipperMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                mActivityShipperMainBinding.viewpager2.setCurrentItem(0);
            } else if (id == R.id.nav_feedback) {
                mActivityShipperMainBinding.viewpager2.setCurrentItem(1);
            } else if (id == R.id.nav_order) {
                mActivityShipperMainBinding.viewpager2.setCurrentItem(2);
            }  else if (id == R.id.nav_account) {
                mActivityShipperMainBinding.viewpager2.setCurrentItem(3);
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        showConfirmExitApp();
    }

    private void showConfirmExitApp() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_exit_app))
                .positiveText(getString(R.string.action_ok))
                .onPositive((dialog, which) -> finishAffinity())
                .negativeText(getString(R.string.action_cancel))
                .cancelable(false)
                .show();
    }

    public void setToolBar(String title) {
        mActivityShipperMainBinding.toolbar.layoutToolbar.setVisibility(View.VISIBLE);
        mActivityShipperMainBinding.toolbar.tvTitle.setText(title);
    }
}