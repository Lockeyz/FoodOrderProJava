package com.pro.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.databinding.ItemPromotionPopularBinding;
import com.pro.foodorder.databinding.ItemPromotionPopularBinding;
import com.pro.foodorder.listener.IOnClickFoodItemListener;
import com.pro.foodorder.listener.IOnClickPromotionItemListener;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.Promotion;
import com.pro.foodorder.utils.GlideUtils;

import java.util.List;

public class PromotionPopularAdapter extends RecyclerView.Adapter<PromotionPopularAdapter.PromotionPopularViewHolder>{

    private final List<Promotion> mListPromotions;
    public final IOnClickPromotionItemListener mIOnClickPromotionItemListener;
    public PromotionPopularAdapter(List<Promotion> mListPromotions, IOnClickPromotionItemListener mIOnClickPromotionItemListener) {
        this.mListPromotions = mListPromotions;
        this.mIOnClickPromotionItemListener = mIOnClickPromotionItemListener;
    }

    @NonNull
    @Override
    public PromotionPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPromotionPopularBinding mItemPromotionPopularBinding = ItemPromotionPopularBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PromotionPopularViewHolder(mItemPromotionPopularBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionPopularViewHolder holder, int position) {
        Promotion promotion = mListPromotions.get(position);
        if (promotion == null) {
            return;
        }
        GlideUtils.loadUrlBanner(promotion.getBanner(), holder.mItemPromotionPopularBinding.imagePromotion);
//        if (promotion.getSale() <= 0) {
//            holder.mItemPromotionPopularBinding.tvSaleOff.setVisibility(View.GONE);
//        } else {
//            holder.mItemPromotionPopularBinding.tvSaleOff.setVisibility(View.VISIBLE);
//            String strSale = "Giảm " + promotion.getSale() + "%";
//            holder.mItemPromotionPopularBinding.tvSaleOff.setText(strSale);
//        }
        holder.mItemPromotionPopularBinding.layoutItem.setOnClickListener(v -> mIOnClickPromotionItemListener.onClickItemPromotion(promotion));
    }

    @Override
    public int getItemCount() {
        if (mListPromotions != null) {
            return mListPromotions.size();
        }
        return 0;
    }

    public static class PromotionPopularViewHolder extends RecyclerView.ViewHolder {

        private final ItemPromotionPopularBinding mItemPromotionPopularBinding;

        public PromotionPopularViewHolder(@NonNull ItemPromotionPopularBinding mItemPromotionPopularBinding) {
            super(mItemPromotionPopularBinding.getRoot());
            this.mItemPromotionPopularBinding = mItemPromotionPopularBinding;
        }
    }
}
