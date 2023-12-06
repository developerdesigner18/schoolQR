package com.newmysmileQR.Parent.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.newmysmileQR.APICall.Authentication;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Config;
import com.newmysmileQR.Utility.Preference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvRegister;
    private EditText etICNumber;
    private TextView etEmail;
    private TextView etPassword;
    private Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        initView();
        btnLogIn.setOnClickListener(this);
//        etICNumber.setOnClickListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

//        etICNumber = findViewById(R.id.etICNumber);

        tvRegister = findViewById(R.id.tvRegister);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogIn = findViewById(R.id.btnLogIn);

        tvRegister.setText("Don't have an account? ");
        SpannableString spannableString = new SpannableString("Register");
        spannableString.setSpan(new ClickableSpan() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(@NonNull View widget) {
                widget.cancelPendingInputEvents();
                Intent mIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mIntent);
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        }, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.append(spannableString);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
        tvRegister.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

//        etICNumber.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                etICNumber.setError("Last 5 alphanumeric of student IC and year the child is born (eg. 1234H2012)");
//                return false;
//            }
//        });

    }

    private void login() {
        if (Preference.isNetworkAvailable(this)) {
//            String ICnumber = etICNumber.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String fcmToken = FirebaseInstanceId.getInstance().getToken();
            Authentication.login(this,"student", "","", email, password, fcmToken, "0", new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    btnLogIn.setEnabled(true);
                    if (response.getUser().getUserType() == 3) {
                        Preference.setLoginStatus(LoginActivity.this, true);
                        Preference.saveUser(LoginActivity.this, response.getUser());
                        if (response.getUser().getIslogin() == 1) {

                            SharedPreferences sharedPref = getSharedPreferences(Config.SETTING_PREFERENCE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            if (Preference.getBoolean(LoginActivity.this, Config.INTRO, false)) {
                                Intent mIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(mIntent);
                                finish();
                            } else {
                                Intent mIntent = new Intent(LoginActivity.this, AppIntro.class);
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(mIntent);
                                finish();
                            }
                        } else {
                            startActivity(new Intent(LoginActivity.this, ChangePassword.class));
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "User not exist!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    btnLogIn.setEnabled(true);
                }
            });
        } else {
            Toast.makeText(this, "Check Your mobile network", Toast.LENGTH_SHORT).show();
            btnLogIn.setEnabled(true);
        }
    }

    private boolean isValid() {
//        if (TextUtils.isEmpty(etICNumber.getText())) {
//            Toast.makeText(this, "Enter student reference.", Toast.LENGTH_SHORT).show();
//            etICNumber.requestFocus();
//            return false;
//        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            Toast.makeText(this, "Enter email.", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            Toast.makeText(this, "Enter password.", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return false;
        }

        if (!Preference.isValidEmail(etEmail.getText())) {
            Toast.makeText(this, "Enter valid email.", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    public void onForgotClick(View view) {
        Intent mIntent = new Intent(this, ForgotPassword.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mIntent);
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogIn: {
                btnLogIn.setEnabled(false);
                if (isValid()) {
                    login();
                } else {
                    btnLogIn.setEnabled(true);
                }
                break;
            }

        }
    }


}
