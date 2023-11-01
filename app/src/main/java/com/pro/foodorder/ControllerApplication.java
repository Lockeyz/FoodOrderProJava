package com.pro.foodorder;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.foodorder.prefs.DataStoreManager;

public class ControllerApplication extends Application {

    private FirebaseDatabase mFirebaseDatabase;

    public static ControllerApplication get(Context context) {
        return (ControllerApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
//        mFirebaseDatabase = FirebaseDatabase.getInstance(Constant.FIREBASE_URL);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DataStoreManager.init(getApplicationContext());
    }

    public DatabaseReference getAllUserDatabaseReference() {
        return mFirebaseDatabase.getReference("user");
    }
    public DatabaseReference getAllAdminDatabaseReference() {
        return mFirebaseDatabase.getReference("admin");
    }
    public DatabaseReference getAllShipperDatabaseReference() {
        return mFirebaseDatabase.getReference("shipper");
    }
    public DatabaseReference getAllFoodDatabaseReference() {
        return mFirebaseDatabase.getReference("food");
    }

    public DatabaseReference getALlFeedbackDatabaseReference() {
        return mFirebaseDatabase.getReference("feedback");
    }

    public DatabaseReference getAllBookingDatabaseReference() {
        return mFirebaseDatabase.getReference("booking");
    }
}