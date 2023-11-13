package com.pro.foodorder.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.MainActivity;
import com.pro.foodorder.activity.PromotionDetailActivity;
import com.pro.foodorder.adapter.PromotionGridAdapter;
import com.pro.foodorder.adapter.PromotionPopularAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentPromotionBinding;
import com.pro.foodorder.model.Promotion;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PromotionFragment extends BaseFragment {

    private FragmentPromotionBinding mFragmentPromotionBinding;

    private List<Promotion> mListPromotion;
    private List<Promotion> mListPromotionPopular;

    private final Handler mHandlerBanner = new Handler();
    private final Runnable mRunnableBanner = new Runnable() {
        @Override
        public void run() {
            if (mListPromotionPopular == null || mListPromotionPopular.isEmpty()) {
                return;
            }
            if (mFragmentPromotionBinding.viewpager2.getCurrentItem() == mListPromotionPopular.size() - 1) {
                mFragmentPromotionBinding.viewpager2.setCurrentItem(0);
                return;
            }
            mFragmentPromotionBinding.viewpager2.setCurrentItem(mFragmentPromotionBinding.viewpager2.getCurrentItem() + 1);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentPromotionBinding = FragmentPromotionBinding.inflate(inflater, container, false);

        getListPromotionFromFirebase("");
        initListener();

        return mFragmentPromotionBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(true, getString(R.string.home));
        }
    }

    private void initListener() {
        mFragmentPromotionBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    if (mListPromotion != null) mListPromotion.clear();
                    getListPromotionFromFirebase("");
                }
            }
        });

        mFragmentPromotionBinding.imgSearch.setOnClickListener(view -> searchPromotion());

        mFragmentPromotionBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchPromotion();
                return true;
            }
            return false;
        });
    }

    private void displayListPromotionPopular() {
        PromotionPopularAdapter mPromotionPopularAdapter = new PromotionPopularAdapter(getListPromotionPopular(), this::goToPromotionDetail);
        mFragmentPromotionBinding.viewpager2.setAdapter(mPromotionPopularAdapter);
        mFragmentPromotionBinding.indicator3.setViewPager(mFragmentPromotionBinding.viewpager2);

        mFragmentPromotionBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandlerBanner.removeCallbacks(mRunnableBanner);
                mHandlerBanner.postDelayed(mRunnableBanner, 3000);
            }
        });
    }

    private void displayListPromotionSuggest() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentPromotionBinding.rcvPromotion.setLayoutManager(gridLayoutManager);

        PromotionGridAdapter mPromotionGridAdapter = new PromotionGridAdapter(mListPromotion, this::goToPromotionDetail);
        mFragmentPromotionBinding.rcvPromotion.setAdapter(mPromotionGridAdapter);
    }

    private List<Promotion> getListPromotionPopular() {
        mListPromotionPopular = new ArrayList<>();
        if (mListPromotion == null || mListPromotion.isEmpty()) {
            return mListPromotionPopular;
        }
        for (Promotion promotion : mListPromotion) {
            if (promotion.isPopular()) {
                mListPromotionPopular.add(promotion);
            }
        }
        return mListPromotionPopular;
    }

    private void getListPromotionFromFirebase(String key) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getAllPromotionDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mFragmentPromotionBinding.layoutContent.setVisibility(View.VISIBLE);
                mListPromotion = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Promotion promotion = dataSnapshot.getValue(Promotion.class);
                    if (promotion == null) {
                        return;
                    }

                    if (StringUtil.isEmpty(key)) {
                        mListPromotion.add(0, promotion);
                    } else {
                        if (GlobalFunction.getTextSearch(promotion.getName()).toLowerCase().trim()
                                .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim())) {
                            mListPromotion.add(0, promotion);
                        }
                    }
                }
                displayListPromotionPopular();
                displayListPromotionSuggest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    private void searchPromotion() {
        String strKey = mFragmentPromotionBinding.edtSearchName.getText().toString().trim();
        if (mListPromotion != null) mListPromotion.clear();
        getListPromotionFromFirebase(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    private void goToPromotionDetail(@NonNull Promotion promotion) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_PROMOTION_OBJECT, promotion);
        GlobalFunction.startActivity(getActivity(), PromotionDetailActivity.class, bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandlerBanner.removeCallbacks(mRunnableBanner);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandlerBanner.postDelayed(mRunnableBanner, 3000);
    }
}