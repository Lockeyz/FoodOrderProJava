package com.pro.foodorder.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemUserOrderBinding;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.utils.DateTimeUtils;

import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.UserOrderViewHolder> {

    private Context mContext;
    private final List<Order> mListOrder;
    private final ICancelOrderListener mICancelOrderListener;

    public interface ICancelOrderListener {
        void cancelOrder(Order order);
    }

    public UserOrderAdapter(Context mContext, List<Order> mListOrder, ICancelOrderListener listener) {
        this.mContext = mContext;
        this.mListOrder = mListOrder;
        this.mICancelOrderListener = listener;
    }

    @NonNull
    @Override
    public UserOrderAdapter.UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserOrderBinding mItemUserOrderBinding = ItemUserOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new UserOrderViewHolder(mItemUserOrderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrderAdapter.UserOrderViewHolder holder, int position) {
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }
//        if (order.isCompleted()) {
//            holder.mItemUserOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay));
//        }
//        else {
//            holder.mItemUserOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        }
//        holder.mItemUserOrderBinding.chbStatus.setChecked(order.isCompleted());


        if (order.getState() == 0){
            holder.mItemUserOrderBinding.tvState.setText("Đơn hàng đang chờ được xác nhận.");
            holder.mItemUserOrderBinding.imageCancel.setVisibility(View.VISIBLE);
        } else if (order.getState() == 1) {
            holder.mItemUserOrderBinding.tvState.setText("Đơn hàng đang được chuẩn bị.");
            holder.mItemUserOrderBinding.imageCancel.setVisibility(View.GONE);
        }else if (order.getState() == 2) {
            holder.mItemUserOrderBinding.tvState.setText("Đơn hàng đang được chuẩn bị.");
        }else if (order.getState() == 3) {
            holder.mItemUserOrderBinding.tvState.setText("Đơn hàng đang được giao.");
        }else if (order.getState() == 4) {
            holder.mItemUserOrderBinding.tvState.setText("Đơn hàng được giao thành công.");
        }
        
        holder.mItemUserOrderBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemUserOrderBinding.tvEmail.setText(order.getEmail());
        holder.mItemUserOrderBinding.tvName.setText(order.getName());
        holder.mItemUserOrderBinding.tvPhone.setText(order.getPhone());
        holder.mItemUserOrderBinding.tvAddress.setText(order.getAddress());
        holder.mItemUserOrderBinding.tvMenu.setText(order.getFoods());
        holder.mItemUserOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemUserOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == order.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        holder.mItemUserOrderBinding.tvPayment.setText(paymentMethod);
        holder.mItemUserOrderBinding.imageCancel.setOnClickListener(
                v -> mICancelOrderListener.cancelOrder(order));
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

    public static class UserOrderViewHolder extends RecyclerView.ViewHolder {

        private final ItemUserOrderBinding mItemUserOrderBinding;

        public UserOrderViewHolder(@NonNull ItemUserOrderBinding mItemUserOrderBinding) {
            super(mItemUserOrderBinding.getRoot());
            this.mItemUserOrderBinding = mItemUserOrderBinding;
        }
    }
}
