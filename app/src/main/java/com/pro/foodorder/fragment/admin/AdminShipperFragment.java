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
import com.pro.foodorder.activity.AddShipperActivity;
import com.pro.foodorder.activity.admin.AdminMainActivity;
import com.pro.foodorder.adapter.admin.AdminFoodAdapter;
import com.pro.foodorder.adapter.admin.AdminShipperAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentAdminShipperBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.listener.IOnManageFoodListener;
import com.pro.foodorder.listener.IOnManageUserListener;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.User;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminShipperFragment extends BaseFragment {

    private FragmentAdminShipperBinding mFragmentAdminShipperBinding;
    private List<User> mListShipper;
    private AdminShipperAdapter mAdminShipperAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentAdminShipperBinding = FragmentAdminShipperBinding.inflate(inflater, container,false);
        initView();
        initListener();
        getListShipper("");
        return mFragmentAdminShipperBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.shipper));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminShipperBinding.rcvShipper.setLayoutManager(linearLayoutManager);
        mListShipper = new ArrayList<>();
        mAdminShipperAdapter = new AdminShipperAdapter(mListShipper, new IOnManageUserListener() {

            @Override
            public void onClickUpdateUser(User shipper) {
                onClickEditShipper(shipper);
            }

            @Override
            public void onClickDeleteUser(User shipper) {
                deleteShipperItem(shipper);
            }
        });
        mFragmentAdminShipperBinding.rcvShipper.setAdapter(mAdminShipperAdapter);
    }

    private void initListener() {
        mFragmentAdminShipperBinding.btnAddShipper.setOnClickListener(v -> onClickAddShipper());

        mFragmentAdminShipperBinding.imgSearch.setOnClickListener(view1 -> searchShipper());

        mFragmentAdminShipperBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchShipper();
                return true;
            }
            return false;
        });

        mFragmentAdminShipperBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
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
                    searchShipper();
                }
            }
        });
    }

    private void onClickAddShipper() {
        GlobalFunction.startActivity(getActivity(), AddShipperActivity.class);
    }

    private void onClickEditShipper(User shipper) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_SHIPPER_OBJECT, shipper);
        GlobalFunction.startActivity(getActivity(), AddShipperActivity.class, bundle);
    }

    private void deleteShipperItem(User shipper) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) {
                        return;
                    }
                    ControllerApplication.get(getActivity()).getFoodDatabaseReference()
                            .child(String.valueOf(shipper.getUserId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            getString(R.string.msg_delete_user_successfully), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void searchShipper() {
        String strKey = mFragmentAdminShipperBinding.edtSearchName.getText().toString().trim();
        if (mListShipper != null) {
            mListShipper.clear();
        } else {
            mListShipper = new ArrayList<>();
        }
        getListShipper(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    public void getListShipper(String keyword) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getAllShipperDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        User shipper = dataSnapshot.getValue(User.class);
                        if (shipper == null || mListShipper == null || mAdminShipperAdapter == null) {
                            return;
                        }
                        if (StringUtil.isEmpty(keyword)) {
                            mListShipper.add(0, shipper);
                        } else {
                            if (GlobalFunction.getTextSearch(shipper.getName()).toLowerCase().trim()
                                    .contains(GlobalFunction.getTextSearch(keyword).toLowerCase().trim())) {
                                mListShipper.add(0, shipper);
                            }
                        }
                        mAdminShipperAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        User shipper = dataSnapshot.getValue(User.class);
                        if (shipper == null || mListShipper == null
                                || mListShipper.isEmpty() || mAdminShipperAdapter == null) {
                            return;
                        }
                        for (int i = 0; i < mListShipper.size(); i++) {
                            if (shipper.getUserId() == mListShipper.get(i).getUserId()) {
                                mListShipper.set(i, shipper);
                                break;
                            }
                        }
                        mAdminShipperAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        User shipper = dataSnapshot.getValue(User.class);
                        if (shipper == null || mListShipper == null
                                || mListShipper.isEmpty() || mAdminShipperAdapter == null) {
                            return;
                        }
                        for (User shipperObject : mListShipper) {
                            if (shipper.getUserId() == shipperObject.getUserId()) {
                                mListShipper.remove(shipperObject);
                                break;
                            }
                        }
                        mAdminShipperAdapter.notifyDataSetChanged();
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