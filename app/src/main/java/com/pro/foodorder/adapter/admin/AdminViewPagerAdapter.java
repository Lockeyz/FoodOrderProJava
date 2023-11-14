package com.pro.foodorder.adapter.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.pro.foodorder.fragment.admin.AdminAccountFragment;
import com.pro.foodorder.fragment.admin.AdminHomeFragment;
import com.pro.foodorder.fragment.admin.AdminOrderFragment;
import com.pro.foodorder.fragment.admin.AdminPromotionFragment;
import com.pro.foodorder.fragment.admin.AdminShipperFragment;

public class AdminViewPagerAdapter extends FragmentStateAdapter {
    public AdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AdminHomeFragment();

            case 1:
                return new AdminPromotionFragment();

            case 2:
                return new AdminOrderFragment();

            case 3:
                return new AdminShipperFragment();

            case 4:
                return new AdminAccountFragment();

            default:
                return new AdminHomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
