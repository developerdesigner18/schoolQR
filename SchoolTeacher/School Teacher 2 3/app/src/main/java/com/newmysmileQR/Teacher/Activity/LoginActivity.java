package com.newmysmileQR.Teacher.Activity;

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
import android.util.Log;
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
import com.newmysmileQR.Utility.Preference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvRegister;
    private TextView tvForget;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etSchoolCode;
    private Button btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btnLogIn.setOnClickListener(this);
    }

    private void initView() {
        tvRegister = findViewById(R.id.tvRegister);
        tvForget = findViewById(R.id.tvForget);
        etSchoolCode = findViewById(R.id.etSchoolCode);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogIn = findViewById(R.id.btnLogIn);

        tvRegister.setText("New user? ");
        SpannableString spannableString = new SpannableString("Signup");
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
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primaryTextColor)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.append(spannableString);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
        tvRegister.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    private void login() {
        if (Preference.isNetworkAvailable(this)) {
            String code = etSchoolCode.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String fcmToken = FirebaseInstanceId.getInstance().getToken();
            Authentication.login(this,"teacher", "", code, email, password, fcmToken, "0", new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    btnLogIn.setEnabled(true);

                    Log.d("check_user_ol", "onResponse: "+response.getUser().getEmail());
                    Log.d("check_user_ol", "onResponse: "+response.getUser().getUserType());

                    if (response.getUser().getUserType() == 2) {
                        Preference.setLoginStatus(LoginActivity.this, true);
                        Preference.saveUser(LoginActivity.this, response.getUser());
                        Log.d("ttt", String.valueOf(Preference.getUser(LoginActivity.this)));
                        Intent mIntent = new Intent(LoginActivity.this, DashboardActivity.class);

                        SharedPreferences pref = LoginActivity.this.getSharedPreferences("MyPref", MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor myedit = pref.edit();
                        myedit.putString("schoolcode",code);
                        myedit.apply();

                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                        finishAffinity();
                    } else {
                        Toast.makeText(LoginActivity.this, "User not exist!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    btnLogIn.setEnabled(true);
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btnLogIn.setEnabled(true);
            Toast.makeText(this, "Check your mobile network", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etSchoolCode.getText())) {
            Toast.makeText(this, "Enter school code.", Toast.LENGTH_SHORT).show();
            etSchoolCode.requestFocus();
            return false;
        }
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
