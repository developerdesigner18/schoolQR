package com.newmysmileQR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.newmysmileQR.Teacher.Activity.LoginActivity;
import com.newmysmileQR.R;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
    }

    public void onTeacherClick(View view) {
        Intent mIntent = new Intent(this, LoginActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mIntent);
    finish();
    }

    public void onParentClick(View view) {
        Intent mIntent = new Intent(this, com.newmysmileQR.Parent.Activity.LoginActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mIntent);
        finish();
    }
}
