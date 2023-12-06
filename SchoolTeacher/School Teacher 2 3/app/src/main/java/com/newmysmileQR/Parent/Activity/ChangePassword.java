package com.newmysmileQR.Parent.Activity;

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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.newmysmileQR.APICall.ParentHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.TermsConditionActivity;
import com.newmysmileQR.Utility.Preference;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    private EditText etOldPass;
    private EditText etNewPass;
    private EditText etConfirm;
    private Button btnRequest;

    private TextView chkTerms;
    private CheckBox cbSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
    }

    private void initView() {
        etOldPass = findViewById(R.id.etOldPass);
        etNewPass = findViewById(R.id.etNewPass);
        etConfirm = findViewById(R.id.etConfirm);
        btnRequest = findViewById(R.id.btnRequest);
        chkTerms = findViewById(R.id.tvTerms);
        cbSign = findViewById(R.id.cbSign);

        btnRequest.setOnClickListener(this);

        chkTerms.setText("I accept the ");

        SpannableString spannableString = new SpannableString("terms and conditions ");
        spannableString.setSpan(new ClickableSpan() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(@NonNull View widget) {
                widget.cancelPendingInputEvents();
                Intent mIntent = new Intent(ChangePassword.this, TermsConditionActivity.class);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRequest: {
                if (isValid()) {
                    changePass();
                }
                break;
            }
        }
    }

    private void changePass() {
        if (Preference.isNetworkAvailable(this)) {
            String oldPassword = etOldPass.getText().toString().trim();
            String newPassword = etNewPass.getText().toString().trim();
            ParentHome.changePassword(this, oldPassword, newPassword, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    Toast.makeText(ChangePassword.this, response.getMessage() + "", Toast.LENGTH_SHORT).show();
                    Preference.saveUser(ChangePassword.this, response.getUser());
                    Preference.setChangeStatus(ChangePassword.this, true);
                    startActivity(new Intent(ChangePassword.this, AppIntro.class));
                    finishAffinity();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(ChangePassword.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Check your Network", Toast.LENGTH_SHORT).show();
        }
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
        if (!cbSign.isChecked()) {
            Toast.makeText(this, "Please indicate that you accept the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
