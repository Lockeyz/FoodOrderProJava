package com.pro.foodorder.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemRevenueBinding;
import com.pro.foodorder.listener.IOnManageOrderListener;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.utils.DateTimeUtils;

import java.util.List;

public class RevenueAdapter extends RecyclerView.Adapter<RevenueAdapter.RevenueViewHolder> {

    Context context;
    private final List<Order> mListOrder;
    IOnManageOrderListener iOnManageOrderListener;

    public RevenueAdapter(List<Order> mListOrder, IOnManageOrderListener iOnManageOrderListener) {
        this.mListOrder = mListOrder;
        this.iOnManageOrderListener = iOnManageOrderListener;
    }

    @NonNull
    @Override
    public RevenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRevenueBinding itemRevenueBinding = ItemRevenueBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new RevenueViewHolder(itemRevenueBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RevenueViewHolder holder, int position) {
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }
        holder.mItemRevenueBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemRevenueBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate_2(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemRevenueBinding.tvTotalAmount.setText(strAmount);

        holder.itemView.setOnClickListener(v -> {
            iOnManageOrderListener.onClickDetailOrder(order);
        });
    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public static class RevenueViewHolder extends RecyclerView.ViewHolder {

        private final ItemRevenueBinding mItemRevenueBinding;

        public RevenueViewHolder(@NonNull ItemRevenueBinding itemRevenueBinding) {
            super(itemRevenueBinding.getRoot());
            this.mItemRevenueBinding = itemRevenueBinding;
        }
    }
}
