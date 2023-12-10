package com.pro.foodorder.activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.BaseActivity;
import com.pro.foodorder.activity.ReviewActivity;
import com.pro.foodorder.adapter.ReviewAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivityAdminReviewBinding;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.model.Review;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminReviewActivity extends BaseActivity {

    Order mOrder;
    private ActivityAdminReviewBinding mActivityAdminReviewBinding;
    private List<Review> mListReview;
    private ReviewAdapter mReviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminReviewBinding = ActivityAdminReviewBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminReviewBinding.getRoot());

        getDataIntent();
        getListReview();
        initToolbar();
        initView();

        mActivityAdminReviewBinding.tvSendReview.setOnClickListener(v -> onClickSendReview());
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityAdminReviewBinding.rcvReview.setLayoutManager(linearLayoutManager);
    }

    protected void initToolbar() {
        mActivityAdminReviewBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAdminReviewBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityAdminReviewBinding.toolbar.tvTitle.setText("Đánh giá đơn hàng");
        mActivityAdminReviewBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void getDataIntent() {
        Bundle bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            mOrder = (Order) bundleReceived.get(Constant.KEY_INTENT_ORDER_OBJECT);
        }
    }

    private void onClickSendReview() {

        String strComment = mActivityAdminReviewBinding.edtComment.getText().toString();

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
        mActivityAdminReviewBinding.edtComment.setText("");
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
                            if (review != null && review.getOrderId().contains(mOrder.getId()+"")) {
                                mListReview.add(0, review);
                            }
                        }
                        mReviewAdapter = new ReviewAdapter(AdminReviewActivity.this, mListReview);
                        mActivityAdminReviewBinding.rcvReview.setAdapter(mReviewAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}