package com.pro.foodorder.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.adapter.ReviewAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityReviewBinding;
import com.pro.foodorder.model.Review;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends BaseActivity {

    Order mOrder;
    private ActivityReviewBinding mActivityReviewBinding;
    private List<Review> mListReview;
    private ReviewAdapter mReviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityReviewBinding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(mActivityReviewBinding.getRoot());

        getDataIntent();
        getListReview();
        initToolbar();
        initView();

        mActivityReviewBinding.tvSendReview.setOnClickListener(v -> onClickSendReview());
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityReviewBinding.rcvReview.setLayoutManager(linearLayoutManager);
    }

    protected void initToolbar() {
        mActivityReviewBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityReviewBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityReviewBinding.toolbar.tvTitle.setText("Đánh giá đơn hàng");
        mActivityReviewBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            mOrder = (Order) bundleReceived.get(Constant.KEY_INTENT_ORDER_OBJECT);
        }
    }

    private void onClickSendReview() {

        String strComment = mActivityReviewBinding.edtComment.getText().toString();

        if (StringUtil.isEmpty(strComment)) {
            GlobalFunction.showToastMessage(this, getString(R.string.comment_require));
        } else {
            showProgressDialog(true);
            long reviewId = System.currentTimeMillis();
            Review review = new Review(reviewId, mOrder.getId()+"", FirebaseAuth.getInstance().getUid(), strComment);
            ControllerApplication.get(this).getALlReviewDatabaseReference()
                    .child(String.valueOf(reviewId))
                    .setValue(review, (databaseError, databaseReference) -> {
                        showProgressDialog(false);
                        sendReviewSuccess();
                    });
        }
    }

    public void sendReviewSuccess() {
        GlobalFunction.hideSoftKeyboard(this);
        GlobalFunction.showToastMessage(this, "Gửi đánh giá đơn hàng thành công!");
        mActivityReviewBinding.edtComment.setText("");
    }



    public void getListReview() {

        ControllerApplication.get(this).getALlReviewDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mListReview != null) {
                            mListReview.clear();
                        } else {
                            mListReview = new ArrayList<>();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Review review = dataSnapshot.getValue(Review.class);
                            if (review != null) {
                                mListReview.add(0, review);
                            }
                        }
                        mReviewAdapter = new ReviewAdapter(ReviewActivity.this, mListReview);
                        mActivityReviewBinding.rcvReview.setAdapter(mReviewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}