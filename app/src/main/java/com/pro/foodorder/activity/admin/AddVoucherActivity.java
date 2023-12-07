package com.pro.foodorder.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.BaseActivity;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityAddVoucherBinding;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.FoodObject;
import com.pro.foodorder.model.Image;
import com.pro.foodorder.model.Voucher;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddVoucherActivity extends BaseActivity {

    private ActivityAddVoucherBinding mActivityAddVoucherBinding;
    private boolean isUpdate;
    private Voucher mVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAddVoucherBinding = ActivityAddVoucherBinding.inflate(getLayoutInflater());
        setContentView(mActivityAddVoucherBinding.getRoot());

        getDataIntent();
        initToolbar();
        initView();

        mActivityAddVoucherBinding.btnAddOrEdit.setOnClickListener(v -> addOrEditFood());
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            isUpdate = true;
            mVoucher = (Voucher) bundleReceived.get(Constant.KEY_INTENT_VOUCHER_OBJECT);
        }
    }

    private void initToolbar() {
        mActivityAddVoucherBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAddVoucherBinding.toolbar.imgCart.setVisibility(View.GONE);

        mActivityAddVoucherBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        if (isUpdate) {
            mActivityAddVoucherBinding.toolbar.tvTitle.setText("Chỉnh sửa voucher");
            mActivityAddVoucherBinding.btnAddOrEdit.setText(getString(R.string.action_edit));

            mActivityAddVoucherBinding.edtName.setText(mVoucher.getName());
            mActivityAddVoucherBinding.edtImage.setText(mVoucher.getImage());
            mActivityAddVoucherBinding.edtValue.setText(mVoucher.getValue()+"");
            mActivityAddVoucherBinding.edtCondition.setText(mVoucher.getCondition()+"");
            mActivityAddVoucherBinding.edtConversionPoint.setText(mVoucher.getConversionPoint()+"");

        } else {
            mActivityAddVoucherBinding.toolbar.tvTitle.setText("Thêm voucher mới");
            mActivityAddVoucherBinding.btnAddOrEdit.setText(getString(R.string.action_add));
        }
    }

    private void addOrEditFood() {
        String strName = mActivityAddVoucherBinding.edtName.getText().toString().trim();
        String strImage = mActivityAddVoucherBinding.edtImage.getText().toString().trim();
        String strValue = mActivityAddVoucherBinding.edtValue.getText().toString().trim();
        String strCondition = mActivityAddVoucherBinding.edtCondition.getText().toString().trim();
        String strConversionPoint = mActivityAddVoucherBinding.edtConversionPoint.getText().toString().trim();

        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, "Vui lòng nhập tên voucher", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, "Vui lòng nhập link ảnh voucher", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strValue)) {
            Toast.makeText(this, "Vui lòng nhập giá trị của voucher", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strCondition)) {
            Toast.makeText(this, "Vui lòng nhập giá trị của đơn hàng áp dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtil.isEmpty(strConversionPoint)) {
            Toast.makeText(this, "Vui lòng nhập điểm thưởng dùng để quy đổi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update food
        if (isUpdate) {
            showProgressDialog(true);
            Map<String, Object> map = new HashMap<>();
            map.put("name", strName);
            map.put("image", strImage);
            map.put("value", Integer.parseInt(strValue));
            map.put("condition", Integer.parseInt(strCondition));
            map.put("conversionPoint", Integer.parseInt(strConversionPoint));

            ControllerApplication.get(this).getAllVoucherDatabaseReference()
                    .child(String.valueOf(mVoucher.getId())).updateChildren(map, (error, ref) -> {
                        showProgressDialog(false);
                        Toast.makeText(
                                this,
                                "Chỉnh sửa voucher thành công", Toast.LENGTH_SHORT).show();
                        GlobalFunction.hideSoftKeyboard(this);
                    });
            return;
        }

        // Add food
        showProgressDialog(true);
        long voucherId = System.currentTimeMillis();
        Voucher voucher = new Voucher(voucherId, strName, strImage, Integer.parseInt(strValue),
                Integer.parseInt(strCondition), Integer.parseInt(strConversionPoint));

        ControllerApplication.get(this).getAllVoucherDatabaseReference()
                .child(String.valueOf(voucherId)).setValue(voucher, (error, ref) -> {
                    showProgressDialog(false);
                    mActivityAddVoucherBinding.edtName.setText("");
                    mActivityAddVoucherBinding.edtImage.setText("");
                    mActivityAddVoucherBinding.edtValue.setText("");
                    mActivityAddVoucherBinding.edtCondition.setText("");
                    mActivityAddVoucherBinding.edtConversionPoint.setText("");
                    GlobalFunction.hideSoftKeyboard(this);
                    Toast.makeText(this, "Thêm voucher mới thành công", Toast.LENGTH_SHORT).show();
                });
    }
}