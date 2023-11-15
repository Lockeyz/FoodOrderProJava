package com.pro.foodorder.fragment.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.AddFoodActivity;
import com.pro.foodorder.activity.AddPromotionActivity;
import com.pro.foodorder.activity.admin.AdminMainActivity;
import com.pro.foodorder.adapter.admin.AdminPromotionAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentAdminPromotionBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.listener.IOnManageFoodListener;
import com.pro.foodorder.listener.IOnManagePromotionListener;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.Promotion;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminPromotionFragment extends BaseFragment {

    private FragmentAdminPromotionBinding mFragmentAdminPromotionBinding;
    private List<Promotion> mListPromotion;
    private AdminPromotionAdapter mAdminPromotionAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentAdminPromotionBinding = FragmentAdminPromotionBinding.inflate(inflater, container, false);

        initView();
        initListener();
        getListPromotion("");

        return mFragmentAdminPromotionBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar("Khuyến mãi");
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminPromotionBinding.rcvPromotion.setLayoutManager(linearLayoutManager);
        mListPromotion = new ArrayList<>();
        mAdminPromotionAdapter = new AdminPromotionAdapter(mListPromotion, new IOnManagePromotionListener() {
            @Override
            public void onClickUpdatePromotion(Promotion promotion) {
                onClickEditPromotion(promotion);
            }

            @Override
            public void onClickDeletePromotion(Promotion promotion) {
                deletePromotionItem(promotion);
            }
        });
        mFragmentAdminPromotionBinding.rcvPromotion.setAdapter(mAdminPromotionAdapter);
    }

    private void initListener() {
        mFragmentAdminPromotionBinding.btnAddFood.setOnClickListener(v -> onClickAddFood());

        mFragmentAdminPromotionBinding.imgSearch.setOnClickListener(view1 -> searchPromotion());

        mFragmentAdminPromotionBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchPromotion();
                return true;
            }
            return false;
        });

        mFragmentAdminPromotionBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    searchPromotion();
                }
            }
        });
    }

    private void onClickAddFood() {
        GlobalFunction.startActivity(getActivity(), AddPromotionActivity.class);
    }

    private void onClickEditPromotion(Promotion promotion) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_PROMOTION_OBJECT, promotion);
        GlobalFunction.startActivity(getActivity(), AddPromotionActivity.class, bundle);
    }

    private void deletePromotionItem(Promotion promotion) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) {
                        return;
                    }
                    ControllerApplication.get(getActivity()).getAllPromotionDatabaseReference()
                            .child(String.valueOf(promotion.getId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            getString(R.string.msg_delete_movie_successfully), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void searchPromotion() {
        String strKey = mFragmentAdminPromotionBinding.edtSearchName.getText().toString().trim();
        if (mListPromotion != null) {
            mListPromotion.clear();
        } else {
            mListPromotion = new ArrayList<>();
        }
        getListPromotion(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    public void getListPromotion(String keyword) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getAllPromotionDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        Promotion promotion = dataSnapshot.getValue(Promotion.class);
                        if (promotion == null || mListPromotion == null || mAdminPromotionAdapter == null) {
                            return;
                        }
                        if (StringUtil.isEmpty(keyword)) {
                            mListPromotion.add(0, promotion);
                        } else {
                            if (GlobalFunction.getTextSearch(promotion.getName()).toLowerCase().trim()
                                    .contains(GlobalFunction.getTextSearch(keyword).toLowerCase().trim())) {
                                mListPromotion.add(0, promotion);
                            }
                        }
                        mAdminPromotionAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        Promotion promotion = dataSnapshot.getValue(Promotion.class);
                        if (promotion == null || mListPromotion == null
                                || mListPromotion.isEmpty() || mAdminPromotionAdapter == null) {
                            return;
                        }
                        for (int i = 0; i < mListPromotion.size(); i++) {
                            if (promotion.getId() == mListPromotion.get(i).getId()) {
                                mListPromotion.set(i, promotion);
                                break;
                            }
                        }
                        mAdminPromotionAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Promotion promotion = dataSnapshot.getValue(Promotion.class);
                        if (promotion == null || mListPromotion == null
                                || mListPromotion.isEmpty() || mAdminPromotionAdapter == null) {
                            return;
                        }
                        for (Promotion promotionObject : mListPromotion) {
                            if (promotion.getId() == promotionObject.getId()) {
                                mListPromotion.remove(promotionObject);
                                break;
                            }
                        }
                        mAdminPromotionAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}