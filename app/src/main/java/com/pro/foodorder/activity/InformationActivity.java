package com.pro.foodorder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.admin.AddShipperActivity;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityInformationBinding;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.User;
import com.pro.foodorder.utils.FirebaseUtils;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformationActivity extends BaseActivity {

    private ActivityInformationBinding mActivityInformationBinding;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityInformationBinding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(mActivityInformationBinding.getRoot());

        initToolbar();
        initView();

        mActivityInformationBinding.btnAddOrEdit.setOnClickListener(v -> editInformation());
    }

    private void initToolbar() {
        mActivityInformationBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityInformationBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityInformationBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        mActivityInformationBinding.toolbar.tvTitle.setText("Chỉnh sửa thông tin tài khoản");
        mActivityInformationBinding.btnAddOrEdit.setText(getString(R.string.action_edit));
        mActivityInformationBinding.edtEmail.setInputType(View.AUTOFILL_TYPE_NONE);

        // Get data
        FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        mUser = task.getResult().getValue(User.class);
                        mActivityInformationBinding.edtName.setText(mUser.getName());
                        mActivityInformationBinding.edtEmail.setText(mUser.getEmail());
                        mActivityInformationBinding.edtPhone.setText(mUser.getPhone());
                        mActivityInformationBinding.edtGender.setText(mUser.getGender());
                        mActivityInformationBinding.edtAddress.setText(mUser.getAddress());
                    }
                }) ;

    }

    private void editInformation() {
        String strName = mActivityInformationBinding.edtName.getText().toString().trim();
        String strPhone = mActivityInformationBinding.edtPhone.getText().toString().trim();
        String strGender = mActivityInformationBinding.edtGender.getText().toString().trim();
        String strAddress = mActivityInformationBinding.edtAddress.getText().toString().trim();
        boolean isAdmin = false;
        boolean isShipper = false;

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

        showProgressDialog(true);
        Map<String, Object> map = new HashMap<>();
        map.put("name", strName);
        map.put("phone", strPhone);
        map.put("gender", strGender);
        map.put("address", strAddress);
        map.put("isAdmin", isAdmin);
        map.put("isShipper", isShipper);


        ControllerApplication.get(this).getAllUserDatabaseReference()
                .child(String.valueOf(mUser.getUserId())).updateChildren(map, (error, ref) -> {
                    showProgressDialog(false);
                    Toast.makeText(this,
                            "Chỉnh sửa tài khoản thành công", Toast.LENGTH_SHORT).show();
                    GlobalFunction.hideSoftKeyboard(this);
                });
    }

}