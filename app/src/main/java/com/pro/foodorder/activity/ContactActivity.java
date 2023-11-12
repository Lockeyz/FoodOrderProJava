package com.pro.foodorder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.pro.foodorder.R;
import com.pro.foodorder.adapter.ContactAdapter;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityFeedbackBinding;
import com.pro.foodorder.databinding.ActivityContactBinding;
import com.pro.foodorder.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends BaseActivity {

    ActivityContactBinding mActivityContactBinding;
    private ContactAdapter mContactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityContactBinding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(mActivityContactBinding.getRoot());
        
        initToolbar();
        initView();
    }

    private void initView() {
        mContactAdapter = new ContactAdapter(this,
                getListContact(), () -> GlobalFunction.callPhoneNumber(this));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mActivityContactBinding.rcvData.setNestedScrollingEnabled(false);
        mActivityContactBinding.rcvData.setFocusable(false);
        mActivityContactBinding.rcvData.setLayoutManager(layoutManager);
        mActivityContactBinding.rcvData.setAdapter(mContactAdapter);
    }

    public List<Contact> getListContact() {
        List<Contact> contactArrayList = new ArrayList<>();
        contactArrayList.add(new Contact(Contact.FACEBOOK, R.drawable.ic_facebook));
        contactArrayList.add(new Contact(Contact.HOTLINE, R.drawable.ic_hotline));
        contactArrayList.add(new Contact(Contact.GMAIL, R.drawable.ic_gmail));
        contactArrayList.add(new Contact(Contact.SKYPE, R.drawable.ic_skype));
        contactArrayList.add(new Contact(Contact.YOUTUBE, R.drawable.ic_youtube));
        contactArrayList.add(new Contact(Contact.ZALO, R.drawable.ic_zalo));

        return contactArrayList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContactAdapter.release();
    }

    protected void initToolbar() {
        mActivityContactBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityContactBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityContactBinding.toolbar.tvTitle.setText("Phản hồi");
        mActivityContactBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }
}