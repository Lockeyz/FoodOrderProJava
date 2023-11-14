package com.pro.foodorder.adapter.admin;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemAdminPromotionBinding;
import com.pro.foodorder.databinding.ItemAdminPromotionBinding;
import com.pro.foodorder.listener.IOnManagePromotionListener;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.Promotion;
import com.pro.foodorder.utils.GlideUtils;

import java.util.List;

public class AdminPromotionAdapter extends RecyclerView.Adapter<AdminPromotionAdapter.AdminPromotionViewHolder>{

    private final List<Promotion> mListPromotions;
    public final IOnManagePromotionListener mIOnManagePromotionListener;

    public AdminPromotionAdapter(List<Promotion> mListPromotions, IOnManagePromotionListener mIOnManagePromotionListener) {
        this.mListPromotions = mListPromotions;
        this.mIOnManagePromotionListener = mIOnManagePromotionListener;
    }

    @NonNull
    @Override
    public AdminPromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminPromotionBinding mItemAdminPromotionBinding = ItemAdminPromotionBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminPromotionViewHolder(mItemAdminPromotionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPromotionViewHolder holder, int position) {
        Promotion promotion = mListPromotions.get(position);
        if (promotion == null) {
            return;
        }
        GlideUtils.loadUrl(promotion.getImage(), holder.mItemAdminPromotionBinding.imgPromotion);
//        if (food.getSale() <= 0) {
//            holder.mItemAdminPromotionBinding.tvSaleOff.setVisibility(View.GONE);
//            holder.mItemAdminPromotionBinding.tvPrice.setVisibility(View.GONE);
//
//            String strPrice = food.getPrice() + Constant.CURRENCY;
//            holder.mItemAdminPromotionBinding.tvPriceSale.setText(strPrice);
//        } else {
//            holder.mItemAdminPromotionBinding.tvSaleOff.setVisibility(View.VISIBLE);
//            holder.mItemAdminPromotionBinding.tvPrice.setVisibility(View.VISIBLE);
//
//            String strSale = "Giảm " + food.getSale() + "%";
//            holder.mItemAdminPromotionBinding.tvSaleOff.setText(strSale);
//
//            String strOldPrice = food.getPrice() + Constant.CURRENCY;
//            holder.mItemAdminPromotionBinding.tvPrice.setText(strOldPrice);
//            holder.mItemAdminPromotionBinding.tvPrice.setPaintFlags(holder.mItemAdminPromotionBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//            String strRealPrice = food.getRealPrice() + Constant.CURRENCY;
//            holder.mItemAdminPromotionBinding.tvPriceSale.setText(strRealPrice);
//        }
        holder.mItemAdminPromotionBinding.tvPromotionName.setText(promotion.getName());
        holder.mItemAdminPromotionBinding.tvFoodDescription.setText(promotion.getDescription());
        if (promotion.isPopular()) {
            holder.mItemAdminPromotionBinding.tvPopular.setText("Có");
        } else {
            holder.mItemAdminPromotionBinding.tvPopular.setText("Không");
        }

        holder.mItemAdminPromotionBinding.imgEdit.setOnClickListener(v -> mIOnManagePromotionListener.onClickUpdatePromotion(promotion));
        holder.mItemAdminPromotionBinding.imgDelete.setOnClickListener(v -> mIOnManagePromotionListener.onClickDeletePromotion(promotion));
    }

    @Override
    public int getItemCount() {
        return null == mListPromotions ? 0 : mListPromotions.size();
    }

    public static class AdminPromotionViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminPromotionBinding mItemAdminPromotionBinding;

        public AdminPromotionViewHolder(ItemAdminPromotionBinding mItemAdminPromotionBinding) {
            super(mItemAdminPromotionBinding.getRoot());
            this.mItemAdminPromotionBinding = mItemAdminPromotionBinding;
        }
    }
}
