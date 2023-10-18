package com.pro.foodorder.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.foodorder.constant.Constant;

public class FirebaseUtils {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static DatabaseReference getUserReference(String userId){
        return FirebaseDatabase.getInstance().getReference("users").child(userId);
    }


}
