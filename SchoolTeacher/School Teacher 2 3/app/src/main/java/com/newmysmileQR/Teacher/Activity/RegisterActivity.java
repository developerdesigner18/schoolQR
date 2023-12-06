package com.newmysmileQR.Teacher.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.newmysmileQR.APICall.Authentication;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.TermsConditionActivity;
import com.newmysmileQR.Utility.Preference;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    String otp;
    private EditText etCode, etName, etEmail, etPhone, etPassword;
    private Button btnSignUp;
    private TextView tvSignIn;
    private TextView tvVerify;
    private TextView chkTerms;
    private CheckBox cbSign;
    private Spinner spCountry;
    private SpinnerAdapter mAdapter;
    private String selectCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btnSignUp.setOnClickListener(this);
        tvVerify.setOnClickListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

        etCode = findViewById(R.id.etCode);
        etName = findViewById(R.id.etFirstName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        tvSignIn = findViewById(R.id.tvSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvVerify = findViewById(R.id.tvVerify);
        chkTerms = findViewById(R.id.tvTerms);
        cbSign = findViewById(R.id.cbSign);
        spCountry = findViewById(R.id.spCountry);


        chkTerms.setText("I accept the ");

        SpannableString spannableString = new SpannableString("terms and conditions ");
        spannableString.setSpan(new ClickableSpan() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(@NonNull View widget) {
                widget.cancelPendingInputEvents();
                Intent mIntent = new Intent(RegisterActivity.this, TermsConditionActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mIntent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        }, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        chkTerms.append(spannableString);

        chkTerms.append("relating to the use of this application.");

        chkTerms.setMovementMethod(LinkMovementMethod.getInstance());
        chkTerms.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        tvSignIn.setText("Already have an account ? ");
        spannableString = new SpannableString("Login");
        spannableString.setSpan(new ClickableSpan() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(@NonNull View widget) {
                widget.cancelPendingInputEvents();
                Intent mIntent = new Intent(RegisterActivity.this, LoginActivity.class);
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
        tvSignIn.append(spannableString);
        tvSignIn.setMovementMethod(LinkMovementMethod.getInstance());
        tvSignIn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        ArrayList<String> listCountry = new ArrayList<>();
        listCountry.add("+65");

        mAdapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, listCountry);
        spCountry.setAdapter(mAdapter);
        spCountry.setSelection(0);
        selectCountry = spCountry.getSelectedItem() + "";
        selectCountry = selectCountry.replace("+", "");


    }

    private void signUp() {
        if (Preference.isNetworkAvailable(this)) {
            String code = etCode.getText().toString();
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String password = etPassword.getText().toString();
            Authentication.teacherSignUp(this, code, name, email, phone, password, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    btnSignUp.setEnabled(true);
                    if (response.getUser().getUserType() == 2) {
                        //Preference.setLoginStatus(RegisterActivity.this, true);
                        //Preference.saveUser(RegisterActivity.this, response.getUser());
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("My Smile QR")
                                .setMessage("Registration is successful, you will  be informed within 24 hours when account is created.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent mIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(mIntent);
                                        finishAffinity();
                                    }
                                }).create().show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    btnSignUp.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btnSignUp.setEnabled(true);
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etCode.getText())) {
            Toast.makeText(this, "Enter School code", Toast.LENGTH_SHORT).show();
            etCode.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etName.getText())) {
            Toast.makeText(this, "Enter full name", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etPhone.getText())) {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show();
            etPhone.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return false;
        }
        if (!Preference.isValidEmail(etEmail.getText())) {
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        if (!cbSign.isChecked()) {
            Toast.makeText(this, "Please indicate that you accept the terms and conditions", Toast.LENGTH_SHORT).show();
            btnSignUp.setEnabled(true);
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(this, LoginActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mIntent);
        finish();
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp: {
                if (isValid()) {
                    //btnSignUp.setEnabled(false);
                    signUp();
                }
                break;
            }
            case R.id.tvVerify: {
                if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                    Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                    etPhone.requestFocus();
                    break;
                } else {
                    verifyMobileNumber(selectCountry.trim() + etPhone.getText().toString());
                    Log.d("COUNTERY", selectCountry.trim() + etPhone.getText().toString());
                    displayOtpDialog();
                }

                break;
            }
        }
    }

    private void displayOtpDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.setContentView(R.layout.layout_otp_verification);
        dialog.setCancelable(false);

        final ImageView ivClose;
        final EditText etOtp;
        final Button btnContinue;
        final TextView tvResendOtp;


        ivClose = dialog.findViewById(R.id.ivClose);
        etOtp = dialog.findViewById(R.id.et_otp);
        btnContinue = dialog.findViewById(R.id.btn_continue);
        tvResendOtp = dialog.findViewById(R.id.tvResendOtp);
        // if button is clicked, close the custom dialog

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etOtp.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "please enter 4 digit code", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.equals(etOtp.getText().toString().trim(), otp)) {
                    Toast.makeText(RegisterActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    tvVerify.setText("verified");
                    tvVerify.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                    dialog.dismiss();
                    tvVerify.setEnabled(false);
                    etPassword.setEnabled(true);

                    Toast.makeText(RegisterActivity.this, "OTP match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvResendOtp.setVisibility(View.VISIBLE);
            }
        }, 20000);

        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyMobileNumber(selectCountry.trim() + etPhone.getText().toString());
            }
        });

        dialog.show();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        // getResources().getDisplayMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        int dialogWindowHeight = (int) (displayHeight * 0.49f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void verifyMobileNumber(String mobileNo) {

        otp = Preference.generateVerificationCode();
        Preference.showProcess(this, "", getResources().getString(R.string.message_progress));
        TeacherHome.sendOTP(this, mobileNo, otp, new ResponseCallback() {
            @Override
            public void onResponse(RootModel response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
