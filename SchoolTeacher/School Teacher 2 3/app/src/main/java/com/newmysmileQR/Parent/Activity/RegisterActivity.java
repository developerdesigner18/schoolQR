package com.newmysmileQR.Parent.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.newmysmileQR.APICall.Authentication;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.TermsConditionActivity;
import com.newmysmileQR.Utility.Preference;

import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    EditText  etCode, etName, etDate, etEmail, etMobile, etPassword;
    TextView tvTerms;
    TextView tvSignIn;
    Button btnSignUp;

    //Date
    private Calendar calendar = Calendar.getInstance();
    private int mYear = calendar.get(Calendar.YEAR);
    private int mMonth = calendar.get(Calendar.MONTH);
    private int mDay = calendar.get(Calendar.DAY_OF_MONTH);
    private String selectedDate = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        initView();
        btnSignUp.setOnClickListener(this);
        etDate.setOnTouchListener(this);
    }

    private void showDateDialog() {
        DatePickerDialog dialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month + 1;
                        mDay = dayOfMonth;
                        selectedDate = String.format(Locale.ENGLISH, "%04d-%02d-%02d", year, mMonth, dayOfMonth);
                        etDate.setText(String.format(Locale.ENGLISH, "%02d-%02d-%04d", dayOfMonth, mMonth, year));
                    }
                }, mYear, mMonth - 1, mDay);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, mMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.YEAR, mYear);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dialog.show();
    }

    private void initView() {
//        etICNumber = findViewById(R.id.etICNumber);
        etCode = findViewById(R.id.etCode);
        etName = findViewById(R.id.etFirstName);
        etDate = findViewById(R.id.etDate);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);

        tvTerms = findViewById(R.id.tvTerms);
        tvSignIn = findViewById(R.id.tvSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        tvTerms.setText("By Signup, you are agree our ");
        SpannableString spannableString = new SpannableString("terms & condition");
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
        tvTerms.append(spannableString);
        tvTerms.setMovementMethod(LinkMovementMethod.getInstance());
        tvTerms.setOnLongClickListener(new View.OnLongClickListener() {
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

//        etICNumber.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                etICNumber.setError("Last 5 alphanumeric of student IC and year the child is born (eg. 1234H2012)");
//                return false;
//            }
//        });


    }

    private boolean isValid() {
//        if (TextUtils.isEmpty(etICNumber.getText())) {
//            Toast.makeText(this, "Enter student reference", Toast.LENGTH_SHORT).show();
//            etICNumber.requestFocus();
//            return false;
//        }
        if (TextUtils.isEmpty(etCode.getText())) {
            Toast.makeText(this, "Enter school code", Toast.LENGTH_SHORT).show();
            etCode.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etName.getText())) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etDate.getText())) {
            Toast.makeText(this, "Select date of birth", Toast.LENGTH_SHORT).show();
            etDate.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etMobile.getText())) {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show();
            etMobile.requestFocus();
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
                btnSignUp.setEnabled(false);
                if (isValid()) {
                    signUp();
                    btnSignUp.setEnabled(true);
                    Intent mIntent = new Intent(this, DashboardActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                } else {
                    btnSignUp.setEnabled(true);
                }
                break;
            }
        }
    }

    private void signUp() {
        if (Preference.isNetworkAvailable(this)) {
//            String ICNumber = etICNumber.getText().toString();
            String code = etCode.getText().toString();
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etMobile.getText().toString();
            String password = etPassword.getText().toString();
            String dateOfBirth = selectedDate;
            Authentication.parentSignUp(this, "", code, name, email, phone, password, dateOfBirth, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    btnSignUp.setEnabled(true);
                    if (response.getUser().getUserType() == 3) {
                        Preference.setLoginStatus(RegisterActivity.this, true);
                        Preference.saveUser(RegisterActivity.this, response.getUser());
                        Intent mIntent = new Intent(RegisterActivity.this, DashboardActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                        finishAffinity();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    btnSignUp.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            showDateDialog();
        }
        return false;
    }
}
