package com.pro.foodorder.adapter.admin;

import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemAdminShipperBinding;
import com.pro.foodorder.databinding.ItemAdminShipperBinding;
import com.pro.foodorder.databinding.ItemAdminShipperBinding;
import com.pro.foodorder.listener.IOManageShipperListener;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.User;
import com.pro.foodorder.utils.FirebaseUtils;
import com.pro.foodorder.utils.GlideUtils;

import java.util.List;

public class AdminShipperAdapter extends RecyclerView.Adapter<AdminShipperAdapter.AdminShipperViewHolder>{

    private final List<User> mListShippers;
    public final IOManageShipperListener ioManageShipperListener;

    public AdminShipperAdapter(List<User> mListShippers, IOManageShipperListener ioManageShipperListener) {
        this.mListShippers = mListShippers;
        this.ioManageShipperListener = ioManageShipperListener;
    }

    @NonNull
    @Override
    public AdminShipperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminShipperBinding itemAdminShipperBinding = ItemAdminShipperBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminShipperAdapter.AdminShipperViewHolder(itemAdminShipperBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminShipperViewHolder holder, int position) {
        User shipper = mListShippers.get(position);
        if (shipper == null) {
            return;
        }
//        FirebaseUtils.getOtherProfilePicStorageRef(shipper.getUserId()).getDownloadUrl()
//                .addOnCompleteListener(t -> {
//                    if (t.isSuccessful()){
//                        Uri uri = t.getResult();
//                        String url = uri.toString();
//                        GlideUtils.loadUrl(url, holder.mItemAdminShipperBinding.imgShipper);
//                    }
//                });
        GlideUtils.loadUrl(shipper.getAvatar(), holder.mItemAdminShipperBinding.imgShipper);
        holder.mItemAdminShipperBinding.tvShipperName.setText(shipper.getName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static class AdminShipperViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminShipperBinding mItemAdminShipperBinding;

        public AdminShipperViewHolder(ItemAdminShipperBinding itemAdminShipperBinding) {
            super(itemAdminShipperBinding.getRoot());
            this.mItemAdminShipperBinding = itemAdminShipperBinding;
        }
    }

}
