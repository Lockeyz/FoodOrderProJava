package com.pro.foodorder.adapter.shipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pro.foodorder.fragment.admin.AdminAccountFragment;
import com.pro.foodorder.fragment.admin.AdminFeedbackFragment;
import com.pro.foodorder.fragment.admin.AdminHomeFragment;
import com.pro.foodorder.fragment.admin.AdminOrderFragment;
import com.pro.foodorder.fragment.shipper.ShipperAccountFragment;
import com.pro.foodorder.fragment.shipper.ShipperFeedbackFragment;
import com.pro.foodorder.fragment.shipper.ShipperHomeFragment;
import com.pro.foodorder.fragment.shipper.ShipperOrderFragment;

public class ShipperViewPagerAdapter extends FragmentStateAdapter {
    public ShipperViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ShipperOrderFragment();
            case 1:
                return new ShipperHomeFragment();
            case 2:
                return new ShipperFeedbackFragment();
            case 3:
                return new ShipperAccountFragment();
            default:
                return new ShipperOrderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
