package com.pro.foodorder.listener;

import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.User;

public interface IOManageShipperListener {
    void onClickUpdateShipper(User shipper);
    void onClickDeleteShipper(User shipper);
}
