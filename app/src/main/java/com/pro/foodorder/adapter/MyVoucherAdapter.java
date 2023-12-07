package com.pro.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemMyVoucherBinding;
import com.pro.foodorder.listener.IOnManageMyVoucherListener;
import com.pro.foodorder.model.MyVoucher;
import com.pro.foodorder.utils.GlideUtils;

import java.util.List;

public class MyVoucherAdapter extends RecyclerView.Adapter<MyVoucherAdapter.MyVoucherViewHolder>{

    private final List<MyVoucher> mListVoucher;
    public final IOnManageMyVoucherListener mIOnManageMyVoucherListener;

    public MyVoucherAdapter(List<MyVoucher> mListVoucher, IOnManageMyVoucherListener mIOnManageMyVoucherListener) {
        this.mListVoucher = mListVoucher;
        this.mIOnManageMyVoucherListener = mIOnManageMyVoucherListener;
    }

    @NonNull
    @Override
    public MyVoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyVoucherBinding mItemMyVoucherBinding = ItemMyVoucherBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyVoucherViewHolder(mItemMyVoucherBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVoucherViewHolder holder, int position) {
        MyVoucher voucher = mListVoucher.get(position);
        if (voucher == null) {
            return;
        }
        GlideUtils.loadUrl(voucher.getImage(), holder.mItemMyVoucherBinding.imgVoucher);

        holder.mItemMyVoucherBinding.tvVoucherName.setText(voucher.getName());
        holder.mItemMyVoucherBinding.tvValue.setText(voucher.getValue()+" %");
        holder.mItemMyVoucherBinding.tvCondition.setText(voucher.getCondition()+ Constant.CURRENCY);
        holder.mItemMyVoucherBinding.tvConversionPoint.setText(voucher.getConversionPoint()+" điểm");

        holder.mItemMyVoucherBinding.tvUse.setOnClickListener(v -> mIOnManageMyVoucherListener.onClickUseVoucher(voucher));
    }

    @Override
    public int getItemCount() {
        return null == mListVoucher ? 0 : mListVoucher.size();
    }


    public static class MyVoucherViewHolder extends RecyclerView.ViewHolder {

        private final ItemMyVoucherBinding mItemMyVoucherBinding;

        public MyVoucherViewHolder(ItemMyVoucherBinding ItemMyVoucherBinding) {
            super(ItemMyVoucherBinding.getRoot());
            this.mItemMyVoucherBinding = ItemMyVoucherBinding;
        }
    }
}
