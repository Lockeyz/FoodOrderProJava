package com.pro.foodorder.listener;

import com.pro.foodorder.model.User;

public interface IOnManageUserListener {

        void onClickUpdateUser(User user);
        void onClickDeleteUser(User user);

}
