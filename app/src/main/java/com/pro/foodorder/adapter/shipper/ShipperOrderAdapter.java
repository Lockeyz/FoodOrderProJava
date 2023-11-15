package com.pro.foodorder.adapter.shipper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.adapter.admin.AdminOrderAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemShipperOrderBinding;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.utils.DateTimeUtils;

import java.util.List;

public class ShipperOrderAdapter extends RecyclerView.Adapter<ShipperOrderAdapter.ShipperOrderViewHolder> {

    private Context mContext;
    private final List<Order> mListOrder;
    private final IUpdateStatusListener mIUpdateStatusListener;

    public interface IUpdateStatusListener {
        void updateStatus(Order order);
    }

    public ShipperOrderAdapter(Context mContext, List<Order> mListOrder, IUpdateStatusListener listener) {
        this.mContext = mContext;
        this.mListOrder = mListOrder;
        this.mIUpdateStatusListener = listener;
    }

    @NonNull
    @Override
    public ShipperOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShipperOrderBinding mItemShipperOrderBinding = ItemShipperOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ShipperOrderViewHolder(mItemShipperOrderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShipperOrderViewHolder holder, int position) {
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }

        holder.mItemShipperOrderBinding.btnConfirmOrder.setOnClickListener(v -> {
            ControllerApplication.get(mContext).getAllBookingDatabaseReference()
                    .child(String.valueOf(order.getId())).child("state").setValue(2);
            ControllerApplication.get(mContext).getAllBookingDatabaseReference()
                    .child(String.valueOf(order.getId())).child("shipperId")
                    .setValue(FirebaseAuth.getInstance().getUid());
        });

        if (order.getState() == 0){
            holder.mItemShipperOrderBinding.tvState.setText("Đơn hàng mới chờ được xác nhận.");
            holder.mItemShipperOrderBinding.chbStatus.setVisibility(View.GONE);
        } else if (order.getState() == 1) {
            holder.mItemShipperOrderBinding.layoutConfirmOrder.setVisibility(View.VISIBLE);
            holder.mItemShipperOrderBinding.layoutCancel.setVisibility(View.GONE);
            holder.mItemShipperOrderBinding.tvState.setText("ĐƠN HÀNG mới đã sẵn sàng.");
        } else if (order.getState() == 2) {
            holder.mItemShipperOrderBinding.layoutConfirmOrder.setVisibility(View.GONE);
            holder.mItemShipperOrderBinding.layoutCancel.setVisibility(View.VISIBLE);
            holder.mItemShipperOrderBinding.tvState.setText("Nhận chuyển đơn hàng. Chờ xác nhận.");
        } else if (order.getState() == 3) {
            holder.mItemShipperOrderBinding.layoutConfirmOrder.setVisibility(View.GONE);
            holder.mItemShipperOrderBinding.layoutCancel.setVisibility(View.VISIBLE);
            holder.mItemShipperOrderBinding.chbStatus.setVisibility(View.VISIBLE);
            holder.mItemShipperOrderBinding.tvState.setText("Đã nhận đơn hàng.");
        }else if (order.getState() == 4) {
            holder.mItemShipperOrderBinding.layoutConfirmOrder.setVisibility(View.GONE);
            holder.mItemShipperOrderBinding.layoutCancel.setVisibility(View.GONE);
            holder.mItemShipperOrderBinding.tvState.setText("Đơn hàng được giao thành công!");
        }

        holder.mItemShipperOrderBinding.chbStatus.setChecked(order.isCompleted());
        holder.mItemShipperOrderBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemShipperOrderBinding.tvEmail.setText(order.getEmail());
        holder.mItemShipperOrderBinding.tvName.setText(order.getName());
        holder.mItemShipperOrderBinding.tvPhone.setText(order.getPhone());
        holder.mItemShipperOrderBinding.tvAddress.setText(order.getAddress());
        holder.mItemShipperOrderBinding.tvMenu.setText(order.getFoods());
        holder.mItemShipperOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemShipperOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == order.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        holder.mItemShipperOrderBinding.tvPayment.setText(paymentMethod);
        holder.mItemShipperOrderBinding.chbStatus.setOnClickListener(
                v -> mIUpdateStatusListener.updateStatus(order));
    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public void release() {
        mContext = null;
    }
    
    public static class ShipperOrderViewHolder extends RecyclerView.ViewHolder {

        private final ItemShipperOrderBinding mItemShipperOrderBinding;

        public ShipperOrderViewHolder(@NonNull ItemShipperOrderBinding mItemShipperOrderBinding) {
            super(mItemShipperOrderBinding.getRoot());
            this.mItemShipperOrderBinding = mItemShipperOrderBinding;
        }
    }
}
