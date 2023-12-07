package com.pro.foodorder.adapter.admin;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemAdminVoucherBinding;
import com.pro.foodorder.listener.IOnManageVoucherListener;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.Voucher;
import com.pro.foodorder.utils.GlideUtils;

import java.util.List;

public class AdminVoucherAdapter extends RecyclerView.Adapter<AdminVoucherAdapter.AdminVoucherViewHolder> {

    private final List<Voucher> mListVoucher;
    public final IOnManageVoucherListener mIOnManageVoucherListener;

    public AdminVoucherAdapter(List<Voucher> mListVoucher, IOnManageVoucherListener mIOnManageVoucherListener) {
        this.mListVoucher = mListVoucher;
        this.mIOnManageVoucherListener = mIOnManageVoucherListener;
    }

    @NonNull
    @Override
    public AdminVoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminVoucherBinding mItemAdminVoucherBinding = ItemAdminVoucherBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminVoucherViewHolder(mItemAdminVoucherBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminVoucherViewHolder holder, int position) {
        Voucher voucher = mListVoucher.get(position);
        if (voucher == null) {
            return;
        }
        GlideUtils.loadUrl(voucher.getImage(), holder.mItemAdminVoucherBinding.imgVoucher);

        holder.mItemAdminVoucherBinding.tvVoucherName.setText(voucher.getName());
        holder.mItemAdminVoucherBinding.tvValue.setText(voucher.getValue()+" %");
        holder.mItemAdminVoucherBinding.tvCondition.setText(voucher.getCondition()+Constant.CURRENCY);
        holder.mItemAdminVoucherBinding.tvConversionPoint.setText(voucher.getConversionPoint()+" điểm");


        holder.mItemAdminVoucherBinding.imgEdit.setOnClickListener(v -> mIOnManageVoucherListener.onClickUpdateVoucher(voucher));
        holder.mItemAdminVoucherBinding.imgDelete.setOnClickListener(v -> mIOnManageVoucherListener.onClickDeleteVoucher(voucher));
    }

    @Override
    public int getItemCount() {
        return null == mListVoucher ? 0 : mListVoucher.size();
    }


    public static class AdminVoucherViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminVoucherBinding mItemAdminVoucherBinding;

        public AdminVoucherViewHolder(ItemAdminVoucherBinding ItemAdminVoucherBinding) {
            super(ItemAdminVoucherBinding.getRoot());
            this.mItemAdminVoucherBinding = ItemAdminVoucherBinding;
        }
    }
}
