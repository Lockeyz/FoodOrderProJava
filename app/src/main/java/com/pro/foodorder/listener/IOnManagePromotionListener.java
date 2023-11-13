package com.pro.foodorder.listener;
import com.pro.foodorder.model.Promotion;

public interface IOnManagePromotionListener {
    void onClickUpdatePromotion(Promotion promotion);
    void onClickDeletePromotion(Promotion promotion);
}
