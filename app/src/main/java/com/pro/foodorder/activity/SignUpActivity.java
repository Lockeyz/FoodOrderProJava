package com.pro.foodorder.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.foodorder.R;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.ActivitySignUpBinding;
import com.pro.foodorder.model.User;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.FirebaseUtils;
import com.pro.foodorder.utils.StringUtil;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding mActivitySignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignUpBinding.getRoot());

        mActivitySignUpBinding.rdbUser.setChecked(true);

        mActivitySignUpBinding.imgBack.setOnClickListener(v -> onBackPressed());
        mActivitySignUpBinding.layoutSignIn.setOnClickListener(v -> finish());
        mActivitySignUpBinding.btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
    }

    private void onClickValidateSignUp() {
        String strEmail = mActivitySignUpBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignUpBinding.edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            if (mActivitySignUpBinding.rdbAdmin.isChecked()) {
                if (!strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid_admin), Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(strEmail, strPassword);
                }
                return;
            }

            if (mActivitySignUpBinding.rdbShipper.isChecked()) {
                if (!strEmail.contains(Constant.SHIPPER_EMAIL_FORMAT)) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid_shipper), Toast.LENGTH_SHORT).show();
                } else {
                    signUpUser(strEmail, strPassword);
                }
                return;
            }

            if (strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
            } else {
                signUpUser(strEmail, strPassword);
            }
        }
    }

    private void signUpUser(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {

                        User user = new User(FirebaseUtils.currentUserId(), email, password, "", "",
                                "", "", "", false, false);
                        User userObject = new User(email, password);

                        if (email != null && email.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                            userObject.setAdmin(true);
                            user.setAdmin(true);
                            //key để set cho child không được có dấu "chấm"
                            String key = FirebaseAuth.getInstance().getUid();
                            FirebaseUtils.getAdminReference(key).setValue(user);
                        }
                        else if (email != null && email.contains(Constant.SHIPPER_EMAIL_FORMAT)) {
                            userObject.setShipper(true);
                            user.setShipper(true);
                            String key = FirebaseAuth.getInstance().getUid();
                            FirebaseUtils.getShipperReference(key).setValue(user);
                        } else {
                            String key = FirebaseAuth.getInstance().getUid();
                            FirebaseUtils.getUserReference(key).setValue(user);
                        }

                        DataStoreManager.setUser(userObject);
                        GlobalFunction.gotoMainActivity(this);
                        finishAffinity();
//                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, getString(R.string.msg_sign_up_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}