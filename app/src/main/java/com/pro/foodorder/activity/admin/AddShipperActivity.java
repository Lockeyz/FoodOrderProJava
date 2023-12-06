package com.pro.foodorder.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.BaseActivity;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityAddShipperBinding;
import com.pro.foodorder.model.User;
import com.pro.foodorder.utils.FirebaseUtils;
import com.pro.foodorder.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class AddShipperActivity extends BaseActivity {

    private ActivityAddShipperBinding mActivityAddShipperBinding;
    private boolean isUpdate;
    private User mShipper;
    private FirebaseAuth mAuth1;
    private FirebaseAuth mAuth2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddShipperBinding = ActivityAddShipperBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddShipperBinding.getRoot());

        getDataIntent();
        initToolbar();
        initView();

        mActivityAddShipperBinding.btnAddOrEdit.setOnClickListener(v -> addOrEditShipper());
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true;
            mShipper = (User) bundleReceived.get(Constant.KEY_INTENT_SHIPPER_OBJECT);
        }
    }

    private void initToolbar() {
        mActivityAddShipperBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAddShipperBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityAddShipperBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        if (isUpdate) {
            mActivityAddShipperBinding.toolbar.tvTitle.setText("Chỉnh sửa tài khoản shipper");
            mActivityAddShipperBinding.btnAddOrEdit.setText(getString(R.string.action_edit));
            mActivityAddShipperBinding.edtEmail.setInputType(View.AUTOFILL_TYPE_NONE);
            mActivityAddShipperBinding.layoutPassword.setVisibility(View.GONE);


            mActivityAddShipperBinding.edtName.setText(mShipper.getName());
            mActivityAddShipperBinding.edtEmail.setText(mShipper.getEmail());
            mActivityAddShipperBinding.edtPassword.setText(mShipper.getPassword());
            mActivityAddShipperBinding.edtPhone.setText(mShipper.getPhone());
            mActivityAddShipperBinding.edtGender.setText(mShipper.getGender());
            mActivityAddShipperBinding.edtAddress.setText(mShipper.getAddress());
            
        } else {
            mActivityAddShipperBinding.toolbar.tvTitle.setText("Thêm tài khoản shipper");
            mActivityAddShipperBinding.btnAddOrEdit.setText(getString(R.string.action_add));
            mActivityAddShipperBinding.layoutPassword.setVisibility(View.VISIBLE);
        }
//        Toast.makeText(this, FirebaseAuth.getInstance().getUid() + "", Toast.LENGTH_SHORT).show();
    }

//    private String getTextOtherImages() {
//        String result = "";
//        if (mShipper == null || mShipper.getImages() == null || mShipper.getImages().isEmpty()) {
//            return result;
//        }
//        for (Image image : mShipper.getImages()) {
//            if (StringUtil.isEmpty(result)) {
//                result = result + image.getUrl();
//            } else {
//                result = result + ";" + image.getUrl();
//            }
//        }
//        return result;
//    }

    private void addOrEditShipper() {
        String strName = mActivityAddShipperBinding.edtName.getText().toString().trim();
        String strEmail = mActivityAddShipperBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivityAddShipperBinding.edtPassword.getText().toString().trim();
        String strPhone = mActivityAddShipperBinding.edtPhone.getText().toString().trim();
        String strGender = mActivityAddShipperBinding.edtGender.getText().toString().trim();
        String strAddress = mActivityAddShipperBinding.edtAddress.getText().toString().trim();
        boolean isAdmin = false;
        boolean isShipper = true;
//        List<Image> listImages = new ArrayList<>();
//        if (!StringUtil.isEmpty(strOtherImages)) {
//            String[] temp = strOtherImages.split(";");
//            for (String strUrl : temp) {
//                Image image = new Image(strUrl);
//                listImages.add(image);
//            }
//        }

        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(this, getString(R.string.msg_description_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(this, getString(R.string.msg_price_food_require), Toast.LENGTH_SHORT).show();
            return;
        }

//        if (StringUtil.isEmpty(strPhone)) {
//            Toast.makeText(this, getString(R.string.msg_discount_food_require), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (StringUtil.isEmpty(strGender)) {
//            Toast.makeText(this, getString(R.string.msg_image_food_require), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (StringUtil.isEmpty(strAddress)) {
//            Toast.makeText(this, getString(R.string.msg_image_banner_food_require), Toast.LENGTH_SHORT).show();
//            return;
//        }

        // Update shipper
        if (isUpdate) {
            showProgressDialog(true);
            Map<String, Object> map = new HashMap<>();
            map.put("name", strName);
            map.put("email", strEmail);
            map.put("password", strPassword);
            map.put("phone", strPhone);
            map.put("gender", strGender);
            map.put("address", strAddress);
            map.put("isAdmin", isAdmin);
            map.put("isShipper", isShipper);
//            if (!listImages.isEmpty()) {
//                map.put("images", listImages);
//            }

            ControllerApplication.get(this).getAllShipperDatabaseReference()
                    .child(String.valueOf(mShipper.getUserId())).updateChildren(map, (error, ref) -> {
                        showProgressDialog(false);
                        Toast.makeText(AddShipperActivity.this,
                                "Chỉnh sửa tài khoản thành công", Toast.LENGTH_SHORT).show();
                        GlobalFunction.hideSoftKeyboard(this);
                    });
            return;
        }

        // Add shipper
        showProgressDialog(true);
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("https://food-order-a56b8-default-rtdb.firebaseio.com")
                .setApiKey("AIzaSyA1Zn4Rl96b8_ogMyq8UzuaUKu3qyGLfv4")
                .setApplicationId("food-order-a56b8").build();

        try {
            FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "FoodOrder");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e) {
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("FoodOrder"));
        }

        mAuth2.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){

                        String userId = mAuth2.getUid();

                        User user = new User(userId, strEmail, strPassword,
                                strName, strPhone, strGender, strAddress, "",
                                false, true);

                        FirebaseUtils.getShipperReference(userId).setValue(user);
                        mAuth2.signOut();

                        showProgressDialog(false);
                        Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                    }
                });

        mActivityAddShipperBinding.edtName.setText("");
        mActivityAddShipperBinding.edtEmail.setText("");
        mActivityAddShipperBinding.edtPassword.setText("");
        mActivityAddShipperBinding.edtPhone.setText("");
        mActivityAddShipperBinding.edtGender.setText("");
        mActivityAddShipperBinding.edtAddress.setText("");

//        long foodId = System.currentTimeMillis();
//        FoodObject food = new FoodObject(foodId, strName, strEmail, Integer.parseInt(strPassword),
//                Integer.parseInt(strPhone), strGender, strAddress, isAdmin);
//        if (!listImages.isEmpty()) {
//            food.setImages(listImages);
//        }
//        ControllerApplication.get(this).getFoodDatabaseReference()
//                .child(String.valueOf(foodId)).setValue(food, (error, ref) -> {
//                    showProgressDialog(false);
//                    mActivityAddShipperBinding.edtName.setText("");
//                    mActivityAddShipperBinding.edtDescription.setText("");
//                    mActivityAddShipperBinding.edtPrice.setText("");
//                    mActivityAddShipperBinding.edtDiscount.setText("");
//                    mActivityAddShipperBinding.edtImage.setText("");
//                    mActivityAddShipperBinding.edtImageBanner.setText("");
//                    mActivityAddShipperBinding.chbPopular.setChecked(false);
//                    mActivityAddShipperBinding.edtOtherImage.setText("");
//                    GlobalFunction.hideSoftKeyboard(this);
//                    Toast.makeText(this, getString(R.string.msg_add_food_success), Toast.LENGTH_SHORT).show();
//                });
    }
}