package com.pro.foodorder.adapter.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.MapsActivity;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ItemAdminOrderBinding;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.DateTimeUtils;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder> {

    private Context mContext;
    private final List<Order> mListOrder;
    private final IUpdateStatusListener mIUpdateStatusListener;

    public interface IUpdateStatusListener {
        void updateStatus(Order order);
    }

    public AdminOrderAdapter(Context mContext, List<Order> mListOrder, IUpdateStatusListener listener) {
        this.mContext = mContext;
        this.mListOrder = mListOrder;
        this.mIUpdateStatusListener = listener;
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminOrderBinding itemAdminOrderBinding = ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AdminOrderViewHolder(itemAdminOrderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }
//        if (order.isCompleted()) {
//            holder.mItemAdminOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay));
//        } else {
//            holder.mItemAdminOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        }
        holder.mItemAdminOrderBinding.imageCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(mContext)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này này không?")
                    .setPositiveButton("Đồng ý", (dialogInterface, i) -> {
                        if (mContext == null) {
                            return;
                        }
                        ControllerApplication.get(mContext).getAllFoodDatabaseReference()
                                .child(String.valueOf(order.getId())).removeValue((error, ref) ->
                                        Toast.makeText(mContext,
                                                "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("Hủy bỏ", null)
                    .show();
        });

        holder.mItemAdminOrderBinding.btnConfirmOrder.setOnClickListener(v -> {
            ControllerApplication.get(mContext).getAllBookingDatabaseReference()
                    .child(String.valueOf(order.getId())).child("state").setValue(1);
        });

        holder.mItemAdminOrderBinding.btnCancelOrder.setOnClickListener(v -> {
            ControllerApplication.get(mContext).getAllFoodDatabaseReference()
                    .child(String.valueOf(order.getId())).removeValue((error, ref) ->
                            Toast.makeText(mContext,
                                    "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show());
        });

        holder.mItemAdminOrderBinding.btnConfirmShipper.setOnClickListener(v -> {
            ControllerApplication.get(mContext).getAllBookingDatabaseReference()
                    .child(String.valueOf(order.getId())).child("state").setValue(3);
        });

        holder.mItemAdminOrderBinding.btnCancelShipper.setOnClickListener(v -> {
            ControllerApplication.get(mContext).getAllBookingDatabaseReference()
                    .child(String.valueOf(order.getId())).child("state").setValue(1);
        });

        holder.mItemAdminOrderBinding.btnMap.setOnClickListener(v -> {
            GlobalFunction.startActivity(mContext, MapsActivity.class);
        });

        if (order.getState() == 0){
            holder.mItemAdminOrderBinding.layoutConfirmOrder.setVisibility(View.VISIBLE);
            holder.mItemAdminOrderBinding.tvState.setText("ĐƠN HÀNG mới chờ được xác nhận.");
        } else if (order.getState() == 1) {
            holder.mItemAdminOrderBinding.layoutConfirmOrder.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.layoutConfirmShipper.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.layoutCancel.setVisibility(View.VISIBLE);
            holder.mItemAdminOrderBinding.tvState.setText("Chờ shipper nhận đơn hàng.");
        } else if (order.getState() == 2) {
            holder.mItemAdminOrderBinding.layoutConfirmOrder.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.layoutConfirmShipper.setVisibility(View.VISIBLE);
            holder.mItemAdminOrderBinding.layoutCancel.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.tvState.setText("Có SHIPPER đã nhận đơn hàng.");
        } else if (order.getState() == 3) {
            holder.mItemAdminOrderBinding.layoutConfirmOrder.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.layoutConfirmShipper.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.layoutCancel.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.tvState.setText("Đơn hàng đang được giao.");
        } else if (order.getState() == 4) {
            holder.mItemAdminOrderBinding.layoutConfirmOrder.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.layoutConfirmShipper.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.layoutCancel.setVisibility(View.GONE);
            holder.mItemAdminOrderBinding.tvState.setText("Đơn hàng được giao thành công!");
        }


        holder.mItemAdminOrderBinding.chbStatus.setChecked(order.isCompleted());
        holder.mItemAdminOrderBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemAdminOrderBinding.tvEmail.setText(order.getEmail());
        holder.mItemAdminOrderBinding.tvName.setText(order.getName());
        holder.mItemAdminOrderBinding.tvPhone.setText(order.getPhone());
        holder.mItemAdminOrderBinding.tvAddress.setText(order.getAddress());
        holder.mItemAdminOrderBinding.tvMenu.setText(order.getFoods());
        holder.mItemAdminOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemAdminOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == order.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        holder.mItemAdminOrderBinding.tvPayment.setText(paymentMethod);
        holder.mItemAdminOrderBinding.chbStatus.setOnClickListener(
                v -> mIUpdateStatusListener.updateStatus(order));
    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public void release() {
        mContext = null;
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminOrderBinding mItemAdminOrderBinding;

        public AdminOrderViewHolder(@NonNull ItemAdminOrderBinding mItemAdminOrderBinding) {
            super(mItemAdminOrderBinding.getRoot());
            this.mItemAdminOrderBinding = mItemAdminOrderBinding;
        }
    }
}
