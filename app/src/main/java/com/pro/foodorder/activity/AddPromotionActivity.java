package com.pro.foodorder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityAddPromotionBinding;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.FoodObject;
import com.pro.foodorder.model.Image;
import com.pro.foodorder.model.Promotion;
import com.pro.foodorder.model.PromotionObject;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPromotionActivity extends BaseActivity {

    private ActivityAddPromotionBinding mActivityAddPromotionBinding;
    private boolean isUpdate;
    private Promotion mPromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityAddPromotionBinding = ActivityAddPromotionBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddPromotionBinding.getRoot());

        getDataIntent();
        initToolbar();
        initView();

        mActivityAddPromotionBinding.btnAddOrEdit.setOnClickListener(v -> addOrEditFood());
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true;
            mPromotion = (Promotion) bundleReceived.get(Constant.KEY_INTENT_PROMOTION_OBJECT);
        }
    }

    private void initToolbar() {
        mActivityAddPromotionBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAddPromotionBinding.toolbar.imgCart.setVisibility(View.GONE);

        mActivityAddPromotionBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        if (isUpdate) {
            mActivityAddPromotionBinding.toolbar.tvTitle.setText(getString(R.string.edit_food));
            mActivityAddPromotionBinding.btnAddOrEdit.setText(getString(R.string.action_edit));

            mActivityAddPromotionBinding.edtName.setText(mPromotion.getName());

            mActivityAddPromotionBinding.edtDescription.setText(mPromotion.getDescription());
            
            mActivityAddPromotionBinding.edtImage.setText(mPromotion.getImage());
            mActivityAddPromotionBinding.edtImageBanner.setText(mPromotion.getBanner());
            mActivityAddPromotionBinding.chbPopular.setChecked(mPromotion.isPopular());
            mActivityAddPromotionBinding.edtOtherImage.setText(getTextOtherImages());
        } else {
            mActivityAddPromotionBinding.toolbar.tvTitle.setText("Thêm khuyến mãi");
            mActivityAddPromotionBinding.btnAddOrEdit.setText(getString(R.string.action_add));
        }
    }

    private String getTextOtherImages() {
        String result = "";
        if (mPromotion == null || mPromotion.getImages() == null || mPromotion.getImages().isEmpty()) {
            return result;
        }
        for (Image image : mPromotion.getImages()) {
            if (StringUtil.isEmpty(result)) {
                result = result + image.getUrl();
            } else {
                result = result + ";" + image.getUrl();
            }
        }
        return result;
    }

    private void addOrEditFood() {
        String strName = mActivityAddPromotionBinding.edtName.getText().toString().trim();
        String strDescription = mActivityAddPromotionBinding.edtDescription.getText().toString().trim();
        String strImage = mActivityAddPromotionBinding.edtImage.getText().toString().trim();
        String strImageBanner = mActivityAddPromotionBinding.edtImageBanner.getText().toString().trim();
        boolean isPopular = mActivityAddPromotionBinding.chbPopular.isChecked();
        String strOtherImages = mActivityAddPromotionBinding.edtOtherImage.getText().toString().trim();
        List<Image> listImages = new ArrayList<>();
        if (!StringUtil.isEmpty(strOtherImages)) {
            String[] temp = strOtherImages.split(";");
            for (String strUrl : temp) {
                Image image = new Image(strUrl);
                listImages.add(image);
            }
        }

        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strDescription)) {
            Toast.makeText(this, getString(R.string.msg_description_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, getString(R.string.msg_image_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strImageBanner)) {
            Toast.makeText(this, getString(R.string.msg_image_banner_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        // Update promotion
        if (isUpdate) {
            showProgressDialog(true);
            Map<String, Object> map = new HashMap<>();
            map.put("name", strName);
            map.put("description", strDescription);
//            map.put("price", Integer.parseInt(strPrice));
//            map.put("sale", Integer.parseInt(strDiscount));
            map.put("image", strImage);
            map.put("banner", strImageBanner);
            map.put("popular", isPopular);
            if (!listImages.isEmpty()) {
                map.put("images", listImages);
            }

            ControllerApplication.get(this).getAllPromotionDatabaseReference()
                    .child(String.valueOf(mPromotion.getId())).updateChildren(map, (error, ref) -> {
                        showProgressDialog(false);
                        Toast.makeText(this,
                                getString(R.string.msg_edit_food_success), Toast.LENGTH_SHORT).show();
                        GlobalFunction.hideSoftKeyboard(this);
                    });
            return;
        }

        // Add promotion
        showProgressDialog(true);
        long promotionId = System.currentTimeMillis();
        PromotionObject promotion = new PromotionObject(promotionId, strName, strDescription,
                strImage, strImageBanner, isPopular);
        if (!listImages.isEmpty()) {
            promotion.setImages(listImages);
        }
        ControllerApplication.get(this).getAllFoodDatabaseReference()
                .child(String.valueOf(promotionId)).setValue(promotion, (error, ref) -> {
                    showProgressDialog(false);
                    mActivityAddPromotionBinding.edtName.setText("");
                    mActivityAddPromotionBinding.edtDescription.setText("");
                    mActivityAddPromotionBinding.edtImage.setText("");
                    mActivityAddPromotionBinding.edtImageBanner.setText("");
                    mActivityAddPromotionBinding.chbPopular.setChecked(false);
                    mActivityAddPromotionBinding.edtOtherImage.setText("");
                    GlobalFunction.hideSoftKeyboard(this);
                    Toast.makeText(this, "Thêm khuyến mãi thành công", Toast.LENGTH_SHORT).show();
                });
    }
}