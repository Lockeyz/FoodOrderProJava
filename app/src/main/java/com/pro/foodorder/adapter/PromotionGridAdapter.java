package com.pro.foodorder.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemFoodGridBinding;
import com.pro.foodorder.databinding.ItemPromotionGridBinding;
import com.pro.foodorder.listener.IOnClickPromotionItemListener;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.Promotion;
import com.pro.foodorder.utils.GlideUtils;

import java.util.List;

public class PromotionGridAdapter extends RecyclerView.Adapter<PromotionGridAdapter.PromotionGridViewHolder>{

    private final List<Promotion> mListPromotions;
    public final IOnClickPromotionItemListener mIOnClickPromotionItemListener;

    public PromotionGridAdapter(List<Promotion> mListPromotions, IOnClickPromotionItemListener mIOnClickPromotionItemListener) {
        this.mListPromotions = mListPromotions;
        this.mIOnClickPromotionItemListener = mIOnClickPromotionItemListener;
    }

    @NonNull
    @Override
    public PromotionGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPromotionGridBinding itemPromotionGridBinding = ItemPromotionGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PromotionGridViewHolder(itemPromotionGridBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionGridViewHolder holder, int position) {
        Promotion promotion = mListPromotions.get(position);
        if (promotion == null) {
            return;
        }
        GlideUtils.loadUrl(promotion.getImage(), holder.mItemPromotionGridBinding.imgPromotion);
//        if (food.getSale() <= 0) {
//            holder.mItemPromotionGridBinding.tvSaleOff.setVisibility(View.GONE);
//            holder.mItemPromotionGridBinding.tvPrice.setVisibility(View.GONE);
//
//            String strPrice = food.getPrice() + Constant.CURRENCY;
//            holder.mItemPromotionGridBinding.tvPriceSale.setText(strPrice);
//        } else {
//            holder.mItemPromotionGridBinding.tvSaleOff.setVisibility(View.VISIBLE);
//            holder.mItemPromotionGridBinding.tvPrice.setVisibility(View.VISIBLE);
//
//            String strSale = "Giảm " + food.getSale() + "%";
//            holder.mItemPromotionGridBinding.tvSaleOff.setText(strSale);
//
//            String strOldPrice = food.getPrice() + Constant.CURRENCY;
//            holder.mItemPromotionGridBinding.tvPrice.setText(strOldPrice);
//            holder.mItemPromotionGridBinding.tvPrice.setPaintFlags(holder.mItemPromotionGridBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//            String strRealPrice = food.getRealPrice() + Constant.CURRENCY;
//            holder.mItemPromotionGridBinding.tvPriceSale.setText(strRealPrice);
//        }
        holder.mItemPromotionGridBinding.tvPromotionName.setText(promotion.getName());

        holder.mItemPromotionGridBinding.layoutItem.setOnClickListener(v -> mIOnClickPromotionItemListener.onClickItemPromotion(promotion));

    }

    @Override
    public int getItemCount() {
        return null == mListPromotions ? 0 : mListPromotions.size();
    }

    public static class PromotionGridViewHolder extends RecyclerView.ViewHolder {

        private final ItemPromotionGridBinding mItemPromotionGridBinding;

        public PromotionGridViewHolder(ItemPromotionGridBinding ItemPromotionGridBinding) {
            super(ItemPromotionGridBinding.getRoot());
            this.mItemPromotionGridBinding = ItemPromotionGridBinding;
        }
    }
}
