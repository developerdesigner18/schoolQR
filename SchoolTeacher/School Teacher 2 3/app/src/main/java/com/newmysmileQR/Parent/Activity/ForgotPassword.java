package com.newmysmileQR.Parent.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.newmysmileQR.APICall.ParentHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Preference;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText etICNumber;
    private EditText etEmail;
    private Button btnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);
        initView();


    }

    private void initView() {
//        etICNumber = findViewById(R.id.etICNumber);
        etEmail = findViewById(R.id.etEmail);
        btnRequest = findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(this);

//        etICNumber.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                etICNumber.setError("Last 5 alphanumeric of student IC and year the child is born (eg. 1234H2012)");
//                return false;
//            }
//        });
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRequest: {
                if (isValid()) {
                    btnRequest.setEnabled(false);
                    forgetPassword();
                } else {
                    btnRequest.setEnabled(true);
                }
                break;
            }
        }
    }

    private void forgetPassword() {
        if (Preference.isNetworkAvailable(this)) {
//            String ICnumber = etICNumber.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            ParentHome.forgetPassword(this, email, "", new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    if (response.isSuccess()) {
                        Toast.makeText(ForgotPassword.this, response.getMessage() + "", Toast.LENGTH_SHORT).show();
                        Intent mIntent = new Intent(ForgotPassword.this, LoginActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                        finishAffinity();
                    }else {
                        btnRequest.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    btnRequest.setEnabled(true);
                    Toast.makeText(ForgotPassword.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btnRequest.setEnabled(true);
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid() {

//        if (TextUtils.isEmpty(etICNumber.getText().toString().trim())) {
//            Toast.makeText(this, "Enter student reference", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Preference.isValidEmail(etEmail.getText().toString().trim())) {
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
