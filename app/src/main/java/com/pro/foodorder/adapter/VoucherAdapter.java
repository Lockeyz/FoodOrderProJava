package com.pro.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemVoucherBinding;
import com.pro.foodorder.listener.IOnManageVoucherListener;
import com.pro.foodorder.model.Voucher;
import com.pro.foodorder.utils.GlideUtils;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {
    private final List<Voucher> mListVoucher;
    public final IOnManageVoucherListener mIOnManageVoucherListener;

    public VoucherAdapter(List<Voucher> mListVoucher, IOnManageVoucherListener mIOnManageVoucherListener) {
        this.mListVoucher = mListVoucher;
        this.mIOnManageVoucherListener = mIOnManageVoucherListener;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVoucherBinding mItemVoucherBinding = ItemVoucherBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VoucherViewHolder(mItemVoucherBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = mListVoucher.get(position);
        if (voucher == null) {
            return;
        }
        GlideUtils.loadUrl(voucher.getImage(), holder.mItemVoucherBinding.imgVoucher);

        holder.mItemVoucherBinding.tvVoucherName.setText(voucher.getName());
        holder.mItemVoucherBinding.tvValue.setText(voucher.getValue()+" %");
        holder.mItemVoucherBinding.tvCondition.setText(voucher.getCondition()+ Constant.CURRENCY);
        holder.mItemVoucherBinding.tvConversionPoint.setText(voucher.getConversionPoint()+" điểm");

        holder.mItemVoucherBinding.tvExchange.setOnClickListener(v -> mIOnManageVoucherListener.onClickExchangeVoucher(voucher));
    }

    @Override
    public int getItemCount() {
        return null == mListVoucher ? 0 : mListVoucher.size();
    }


    public static class VoucherViewHolder extends RecyclerView.ViewHolder {

        private final ItemVoucherBinding mItemVoucherBinding;

        public VoucherViewHolder(ItemVoucherBinding ItemVoucherBinding) {
            super(ItemVoucherBinding.getRoot());
            this.mItemVoucherBinding = ItemVoucherBinding;
        }
    }
}
