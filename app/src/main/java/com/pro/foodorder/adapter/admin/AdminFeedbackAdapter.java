package com.pro.foodorder.adapter.admin;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.databinding.ItemFeedbackBinding;
import com.pro.foodorder.model.Feedback;

import java.util.List;

public class AdminFeedbackAdapter extends RecyclerView.Adapter<AdminFeedbackAdapter.FeedbackViewHolder> {

    private final List<Feedback> mListFeedback;

    public AdminFeedbackAdapter(List<Feedback> mListFeedback) {
        this.mListFeedback = mListFeedback;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeedbackBinding mItemFeedbackBinding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FeedbackViewHolder(mItemFeedbackBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = mListFeedback.get(position);
        if (feedback == null) {
            return;
        }
        holder.mItemFeedbackBinding.tvEmail.setText(feedback.getEmail());
        holder.mItemFeedbackBinding.tvFeedback.setText(feedback.getComment());
    }

    @Override
    public int getItemCount() {
        if (mListFeedback != null) {
            return mListFeedback.size();
        }
        return 0;
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        private final ItemFeedbackBinding mItemFeedbackBinding;

        public FeedbackViewHolder(@NonNull ItemFeedbackBinding itemFeedbackBinding) {
            super(itemFeedbackBinding.getRoot());
            this.mItemFeedbackBinding = itemFeedbackBinding;
        }
    }
}
