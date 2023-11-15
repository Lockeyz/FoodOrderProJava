package com.pro.foodorder.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.MainActivity;
import com.pro.foodorder.activity.admin.AdminMainActivity;
import com.pro.foodorder.adapter.UserOrderAdapter;
import com.pro.foodorder.databinding.FragmentOrderBinding;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.prefs.DataStoreManager;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends BaseFragment {

    private FragmentOrderBinding mFragmentOrderBinding;
    private List<Order> mListOrder;
    private UserOrderAdapter mUserOrderAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        
        mFragmentOrderBinding = FragmentOrderBinding.inflate(inflater, container, false);
        initView();
        getListOrders();
        return mFragmentOrderBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false,getString(R.string.order));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentOrderBinding.rcvOrder.setLayoutManager(linearLayoutManager);
        mListOrder = new ArrayList<>();
        mUserOrderAdapter = new UserOrderAdapter(getActivity(), mListOrder,
                this::handleCancelOrder);
        mFragmentOrderBinding.rcvOrder.setAdapter(mUserOrderAdapter);
    }

    public void getListOrders() {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getAllBookingDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null || mUserOrderAdapter == null) {
                            return;
                        }
                        String userId = FirebaseAuth.getInstance().getUid();
                        if (order.isCompleted() == false && order.getUserId().contains(userId)){
                            mListOrder.add(0, order);
                            mUserOrderAdapter.notifyDataSetChanged();
                        }
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null
                                || mListOrder.isEmpty() || mUserOrderAdapter == null) {
                            return;
                        }
                        for (int i = 0; i < mListOrder.size(); i++) {
                            if (order.getState() == 3 && order.isCompleted() == true){

                                mListOrder.remove(i);
                                break;
                            }
                            if (order.getId() == mListOrder.get(i).getId()) {
                                mListOrder.set(i, order);
                                break;
                            }
                        }
                        mUserOrderAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null
                                || mListOrder.isEmpty() || mUserOrderAdapter == null) {
                            return;
                        }
                        for (Order orderObject : mListOrder) {
                            if (order.getId() == orderObject.getId()) {
                                mListOrder.remove(orderObject);
                                break;
                            }
                        }
                        mUserOrderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void handleCancelOrder(Order order) {
        if (getActivity() == null) {
            return;
        }
        new AlertDialog.Builder(getActivity())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này này không?")
                .setPositiveButton("Đồng ý", (dialogInterface, i) -> {
                    if (getActivity() == null) {
                        return;
                    }
                    ControllerApplication.get(getActivity()).getAllOrderDatabaseReference()
                            .child(String.valueOf(order.getId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy bỏ", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUserOrderAdapter != null) {
            mUserOrderAdapter.release();
        }
    }
}