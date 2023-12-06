package com.newmysmileQR.Teacher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Preference;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText etSchoolCode;
    private EditText etEmail;
    private Button btnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initView();

    }

    private void initView() {
        etSchoolCode = findViewById(R.id.etSchoolCode);
        etEmail = findViewById(R.id.etEmail);
        btnRequest = findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(this);
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
            String email = etEmail.getText().toString().trim();
            String code = etSchoolCode.getText().toString().trim();
            TeacherHome.forgetPassword(this, email,code, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    btnRequest.setEnabled(true);
                    if (response.isSuccess()) {
                        Toast.makeText(ForgotPassword.this, response.getMessage() + "", Toast.LENGTH_SHORT).show();
                        Intent mIntent = new Intent(ForgotPassword.this, LoginActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
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
        if (TextUtils.isEmpty(etSchoolCode.getText().toString())) {
            Toast.makeText(this, "Enter school code", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            Toast.makeText(this, "Enter school code", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Preference.isValidEmail(etEmail.getText().toString())) {
            Toast.makeText(this, "Enter school code", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
