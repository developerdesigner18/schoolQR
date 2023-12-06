package com.newmysmileQR.Teacher.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.newmysmileQR.R;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    private EditText etOldPass;
    private EditText etNewPass;
    private EditText etConfirm;
    private Button btnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password2);

        initView();
    }

    private void initView() {
        etOldPass = findViewById(R.id.etOldPass);
        etNewPass = findViewById(R.id.etNewPass);
        etConfirm = findViewById(R.id.etConfirm);
        btnRequest = findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(this);
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etOldPass.getText().toString().trim())) {
            Toast.makeText(this, "Enter old password.", Toast.LENGTH_SHORT).show();
            etOldPass.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etNewPass.getText().toString().trim())) {
            Toast.makeText(this, "Enter new password.", Toast.LENGTH_SHORT).show();
            etNewPass.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etConfirm.getText().toString().trim())) {
            Toast.makeText(this, "Enter confirm password.", Toast.LENGTH_SHORT).show();
            etConfirm.requestFocus();
            return false;
        }
        if (!etNewPass.getText().toString().trim().equals(etConfirm.getText().toString().trim())) {
            Toast.makeText(this, "Password not match.", Toast.LENGTH_SHORT).show();
            etConfirm.requestFocus();
            return false;
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRequest: {
                if (isValid()) {
                    changePassword();
                    break;
                }
            }
        }
    }

    private void changePassword() {

    }
}
