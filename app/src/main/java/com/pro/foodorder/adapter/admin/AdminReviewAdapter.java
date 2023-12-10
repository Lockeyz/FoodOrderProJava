package com.pro.foodorder.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.pro.foodorder.adapter.ReviewAdapter;
import com.pro.foodorder.databinding.ItemAdminReviewBinding;
import com.pro.foodorder.model.Review;
import com.pro.foodorder.model.User;
import com.pro.foodorder.utils.DateTimeUtils;
import com.pro.foodorder.utils.GlideUtils;

import java.util.List;

public class AdminReviewAdapter extends RecyclerView.Adapter<AdminReviewAdapter.AdminReviewViewHolder> {

    Context mContext;
    private final List<Review> mListReview;

    public AdminReviewAdapter(Context mContext, List<Review> mListReview) {
        this.mContext = mContext;
        this.mListReview = mListReview;
    }

    @NonNull
    @Override
    public AdminReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminReviewBinding mItemAdminReviewBinding = ItemAdminReviewBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AdminReviewViewHolder(mItemAdminReviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminReviewViewHolder holder, int position) {
        Review review = mListReview.get(position);
        if (review == null) {
            return;
        }

        FirebaseDatabase.getInstance().getReference("user/"+review.getUserId())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        User user = task.getResult().getValue(User.class);
                        GlideUtils.loadUrl(user.getAvatar(), holder.mItemAdminReviewBinding.imgAvatar);
                        holder.mItemAdminReviewBinding.tvUserName.setText(user.getName());
                    }
                });

        holder.mItemAdminReviewBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(review.getId()));
        holder.mItemAdminReviewBinding.tvReview.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        if (mListReview != null) {
            return mListReview.size();
        }
        return 0;
    }

    public static class AdminReviewViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminReviewBinding mItemAdminReviewBinding;

        public AdminReviewViewHolder(@NonNull ItemAdminReviewBinding ItemAdminReviewBinding) {
            super(ItemAdminReviewBinding.getRoot());
            this.mItemAdminReviewBinding = ItemAdminReviewBinding;
        }
    }
}
