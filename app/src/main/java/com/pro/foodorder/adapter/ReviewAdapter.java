package com.pro.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ItemReviewBinding;
import com.pro.foodorder.model.Review;
import com.pro.foodorder.model.User;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.DateTimeUtils;
import com.pro.foodorder.utils.GlideUtils;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    Context mContext;
    private final List<Review> mListReview;

    public ReviewAdapter(Context mContext, List<Review> mListReview) {
        this.mContext = mContext;
        this.mListReview = mListReview;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding mItemReviewBinding = ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ReviewViewHolder(mItemReviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Review review = mListReview.get(position);
        if (review == null) {
            return;
        }

        if (DataStoreManager.getUser().isAdmin() == true && review.getUserId().contains(FirebaseAuth.getInstance().getUid())){
            FirebaseDatabase.getInstance().getReference("admin/" + review.getUserId())
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            User user = task.getResult().getValue(User.class);
                            GlideUtils.loadUrl(user.getAvatar(), holder.mItemReviewBinding.imgAvatar);
                            holder.mItemReviewBinding.tvUserName.setText("Cửa hàng");
                        }
                    });
        } else if (DataStoreManager.getUser().isAdmin() == true && !review.getUserId().contains(FirebaseAuth.getInstance().getUid())) {
            FirebaseDatabase.getInstance().getReference("user/" + review.getUserId())
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            User user = task.getResult().getValue(User.class);
                            GlideUtils.loadUrl(user.getAvatar(), holder.mItemReviewBinding.imgAvatar);
                            holder.mItemReviewBinding.tvUserName.setText(user.getName());
                        }
                    });
        } else if (!review.getUserId().contains(FirebaseAuth.getInstance().getUid())){
            FirebaseDatabase.getInstance().getReference("admin/" + review.getUserId())
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            User user = task.getResult().getValue(User.class);
                            GlideUtils.loadUrl(user.getAvatar(), holder.mItemReviewBinding.imgAvatar);
                            holder.mItemReviewBinding.tvUserName.setText("Cửa hàng");
                        }
                    });
        } else if (review.getUserId().contains(FirebaseAuth.getInstance().getUid())){
            FirebaseDatabase.getInstance().getReference("user/" + review.getUserId())
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            User user = task.getResult().getValue(User.class);
                            GlideUtils.loadUrl(user.getAvatar(), holder.mItemReviewBinding.imgAvatar);
                            holder.mItemReviewBinding.tvUserName.setText(user.getName());
                        }
                    });
        }


        holder.mItemReviewBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(review.getId()));
        holder.mItemReviewBinding.tvReview.setText(review.getComment());
    }

    @Override
    public int getItemCount() {
        if (mListReview != null) {
            return mListReview.size();
        }
        return 0;
    }
    
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final ItemReviewBinding mItemReviewBinding;

        public ReviewViewHolder(@NonNull ItemReviewBinding ItemReviewBinding) {
            super(ItemReviewBinding.getRoot());
            this.mItemReviewBinding = ItemReviewBinding;
        }
    }
}
