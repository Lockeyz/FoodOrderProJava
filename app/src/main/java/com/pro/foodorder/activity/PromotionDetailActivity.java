package com.pro.foodorder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pro.foodorder.R;
import com.pro.foodorder.adapter.MoreImageAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.database.FoodDatabase;
import com.pro.foodorder.databinding.ActivityPromotionDetailBinding;
import com.pro.foodorder.event.ReloadListCartEvent;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.Promotion;
import com.pro.foodorder.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PromotionDetailActivity extends BaseActivity {

    private ActivityPromotionDetailBinding mActivityPromotionDetailBinding;
    private Promotion mPromotion;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityPromotionDetailBinding = ActivityPromotionDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityPromotionDetailBinding.getRoot());

        getDataIntent();
        initToolbar();
        setDataPromotionDetail();
//        initListener();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPromotion = (Promotion) bundle.get(Constant.KEY_INTENT_PROMOTION_OBJECT);
        }
    }

    private void initToolbar() {
        mActivityPromotionDetailBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityPromotionDetailBinding.toolbar.imgCart.setVisibility(View.VISIBLE);
        mActivityPromotionDetailBinding.toolbar.tvTitle.setText(getString(R.string.food_detail_title));

        mActivityPromotionDetailBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void setDataPromotionDetail() {
        if (mPromotion == null) {
            return;
        }

        GlideUtils.loadUrlBanner(mPromotion.getBanner(), mActivityPromotionDetailBinding.imagePromotion);
//        if (mPromotion.getSale() <= 0) {
//            mActivityPromotionDetailBinding.tvSaleOff.setVisibility(View.GONE);
//            mActivityPromotionDetailBinding.tvPrice.setVisibility(View.GONE);
//
//            String strPrice = mPromotion.getPrice() + Constant.CURRENCY;
//            mActivityPromotionDetailBinding.tvPriceSale.setText(strPrice);
//        } else {
//            mActivityPromotionDetailBinding.tvSaleOff.setVisibility(View.VISIBLE);
//            mActivityPromotionDetailBinding.tvPrice.setVisibility(View.VISIBLE);
//
//            String strSale = "Giảm " + mPromotion.getSale() + "%";
//            mActivityPromotionDetailBinding.tvSaleOff.setText(strSale);
//
//            String strPriceOld = mPromotion.getPrice() + Constant.CURRENCY;
//            mActivityPromotionDetailBinding.tvPrice.setText(strPriceOld);
//            mActivityPromotionDetailBinding.tvPrice.setPaintFlags(mActivityPromotionDetailBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//            String strRealPrice = mPromotion.getRealPrice() + Constant.CURRENCY;
//            mActivityPromotionDetailBinding.tvPriceSale.setText(strRealPrice);
//        }
        mActivityPromotionDetailBinding.tvPromotionName.setText(mPromotion.getName());
        mActivityPromotionDetailBinding.tvPromotionDescription.setText(mPromotion.getDescription());

        displayListMoreImages();

//        setStatusButtonAddToCart();
    }

    private void displayListMoreImages() {
        if (mPromotion.getImages() == null || mPromotion.getImages().isEmpty()) {
            mActivityPromotionDetailBinding.tvMoreImageLabel.setVisibility(View.GONE);
            return;
        }
        mActivityPromotionDetailBinding.tvMoreImageLabel.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mActivityPromotionDetailBinding.rcvImages.setLayoutManager(gridLayoutManager);

        MoreImageAdapter moreImageAdapter = new MoreImageAdapter(mPromotion.getImages());
        mActivityPromotionDetailBinding.rcvImages.setAdapter(moreImageAdapter);
    }

//    private void setStatusButtonAddToCart() {
//        if (isFoodInCart()) {
//            mActivityPromotionDetailBinding.tvAddToCart.setBackgroundResource(R.drawable.bg_gray_shape_corner_6);
//            mActivityPromotionDetailBinding.tvAddToCart.setText(getString(R.string.added_to_cart));
//            mActivityPromotionDetailBinding.tvAddToCart.setTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));
//            mActivityPromotionDetailBinding.toolbar.imgCart.setVisibility(View.GONE);
//        } else {
//            mActivityPromotionDetailBinding.tvAddToCart.setBackgroundResource(R.drawable.bg_green_shape_corner_6);
//            mActivityPromotionDetailBinding.tvAddToCart.setText(getString(R.string.add_to_cart));
//            mActivityPromotionDetailBinding.tvAddToCart.setTextColor(ContextCompat.getColor(this, R.color.white));
//            mActivityPromotionDetailBinding.toolbar.imgCart.setVisibility(View.VISIBLE);
//        }
//    }

//    private boolean isFoodInCart() {
//        List<Food> list = FoodDatabase.getInstance(this).foodDAO().checkFoodInCart(mPromotion.getId());
//        return list != null && !list.isEmpty();
//    }

//    private void initListener() {
//        mActivityPromotionDetailBinding.tvAddToCart.setOnClickListener(v -> onClickAddToCart());
//        mActivityPromotionDetailBinding.toolbar.imgCart.setOnClickListener(v -> onClickAddToCart());
//    }

//    public void onClickAddToCart() {
//        if (isFoodInCart()) {
//            return;
//        }
//
//        @SuppressLint("InflateParams") View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_cart, null);
//
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(viewDialog);
//
//        ImageView imgFoodCart = viewDialog.findViewById(R.id.img_food_cart);
//        TextView tvFoodNameCart = viewDialog.findViewById(R.id.tv_food_name_cart);
//        TextView tvFoodPriceCart = viewDialog.findViewById(R.id.tv_food_price_cart);
//        TextView tvSubtractCount = viewDialog.findViewById(R.id.tv_subtract);
//        TextView tvCount = viewDialog.findViewById(R.id.tv_count);
//        TextView tvAddCount = viewDialog.findViewById(R.id.tv_add);
//        TextView tvCancel = viewDialog.findViewById(R.id.tv_cancel);
//        TextView tvAddCart = viewDialog.findViewById(R.id.tv_add_cart);
//
//        GlideUtils.loadUrl(mPromotion.getImage(), imgFoodCart);
//        tvFoodNameCart.setText(mPromotion.getName());
//
//        int totalPrice = mPromotion.getRealPrice();
//        String strTotalPrice = totalPrice + Constant.CURRENCY;
//        tvFoodPriceCart.setText(strTotalPrice);
//
//        mPromotion.setCount(1);
//        mPromotion.setTotalPrice(totalPrice);
//
//        tvSubtractCount.setOnClickListener(v -> {
//            int count = Integer.parseInt(tvCount.getText().toString());
//            if (count <= 1) {
//                return;
//            }
//            int newCount = Integer.parseInt(tvCount.getText().toString()) - 1;
//            tvCount.setText(String.valueOf(newCount));
//
//            int totalPrice1 = mPromotion.getRealPrice() * newCount;
//            String strTotalPrice1 = totalPrice1 + Constant.CURRENCY;
//            tvFoodPriceCart.setText(strTotalPrice1);
//
//            mPromotion.setCount(newCount);
//            mPromotion.setTotalPrice(totalPrice1);
//        });
//
//        tvAddCount.setOnClickListener(v -> {
//            int newCount = Integer.parseInt(tvCount.getText().toString()) + 1;
//            tvCount.setText(String.valueOf(newCount));
//
//            int totalPrice2 = mPromotion.getRealPrice() * newCount;
//            String strTotalPrice2 = totalPrice2 + Constant.CURRENCY;
//            tvFoodPriceCart.setText(strTotalPrice2);
//
//            mPromotion.setCount(newCount);
//            mPromotion.setTotalPrice(totalPrice2);
//        });
//
//        tvCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
//
//        tvAddCart.setOnClickListener(v -> {
//            FoodDatabase.getInstance(FoodDetailActivity.this).foodDAO().insertFood(mPromotion);
//            bottomSheetDialog.dismiss();
//            setStatusButtonAddToCart();
//            EventBus.getDefault().post(new ReloadListCartEvent());
//        });
//
//        bottomSheetDialog.show();
//    }
}