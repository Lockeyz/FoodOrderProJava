package com.pro.foodorder.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pro.foodorder.model.Food;
import com.pro.foodorder.model.User;

import java.util.List;

public interface UserDAO {
    @Insert
    void insertFood(User user);

    @Query("SELECT * FROM user")
    List<Food> getListUser();

    @Query("SELECT * FROM food WHERE id=:id")
    List<Food> checkUser(String id);

    @Delete
    void deleteUser(User user);

    @Update
    void updateUser(User user);

    @Query("DELETE from user")
    void deleteAllUser();
}
