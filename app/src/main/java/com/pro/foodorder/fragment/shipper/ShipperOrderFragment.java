package com.pro.foodorder.fragment.shipper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;

import com.pro.foodorder.activity.shipper.ShipperMainActivity;
import com.pro.foodorder.adapter.admin.AdminOrderAdapter;
import com.pro.foodorder.databinding.FragmentShipperOrderBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.model.Order;

import java.util.ArrayList;
import java.util.List;


public class ShipperOrderFragment extends BaseFragment {

    private FragmentShipperOrderBinding mFragmentShipperOrderBinding;
    private List<Order> mListOrder;
    private AdminOrderAdapter mAdminOrderAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentShipperOrderBinding = FragmentShipperOrderBinding.inflate(inflater, container, false);
        initView();
        getListOrders();

        return mFragmentShipperOrderBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((ShipperMainActivity) getActivity()).setToolBar(getString(R.string.order));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentShipperOrderBinding.rcvOrder.setLayoutManager(linearLayoutManager);
        mListOrder = new ArrayList<>();
        mAdminOrderAdapter = new AdminOrderAdapter(getActivity(), mListOrder,
                this::handleUpdateStatusOrder);
        mFragmentShipperOrderBinding.rcvOrder.setAdapter(mAdminOrderAdapter);
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
                        if (order == null || mListOrder == null || mAdminOrderAdapter == null) {
                            return;
                        }
                        mListOrder.add(0, order);
                        mAdminOrderAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null
                                || mListOrder.isEmpty() || mAdminOrderAdapter == null) {
                            return;
                        }
                        for (int i = 0; i < mListOrder.size(); i++) {
                            if (order.getId() == mListOrder.get(i).getId()) {
                                mListOrder.set(i, order);
                                break;
                            }
                        }
                        mAdminOrderAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null
                                || mListOrder.isEmpty() || mAdminOrderAdapter == null) {
                            return;
                        }
                        for (Order orderObject : mListOrder) {
                            if (order.getId() == orderObject.getId()) {
                                mListOrder.remove(orderObject);
                                break;
                            }
                        }
                        mAdminOrderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    private void handleUpdateStatusOrder(Order order) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getAllBookingDatabaseReference()
                .child(String.valueOf(order.getId())).child("completed").setValue(!order.isCompleted());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdminOrderAdapter != null) {
            mAdminOrderAdapter.release();
        }
    }
}