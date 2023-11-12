package com.pro.foodorder.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pro.foodorder.fragment.AccountFragment;
import com.pro.foodorder.fragment.CartFragment;
import com.pro.foodorder.fragment.ContactFragment;
import com.pro.foodorder.fragment.FeedbackFragment;
import com.pro.foodorder.fragment.HomeFragment;
import com.pro.foodorder.fragment.OrderFragment;
import com.pro.foodorder.fragment.PromotionFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();

            case 1:
                return new PromotionFragment();

            case 2:
                return new CartFragment();

            case 3:
                return new OrderFragment();

            case 4:
                return new AccountFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
