package com.newmysmileQR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.newmysmileQR.APIModel.User;
import com.newmysmileQR.Utility.Preference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isLogged = Preference.getLoginStatus(SplashActivity.this, false);
                User user = Preference.getUser(SplashActivity.this);
                if(isLogged && user != null){
                    if(user.getUserType() == 2) {
                        Intent mIntent = new Intent(SplashActivity.this, com.newmysmileQR.Teacher.Activity.DashboardActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                    }else if(user.getUserType() == 3){
                        Intent mIntent = new Intent(SplashActivity.this, com.newmysmileQR.Parent.Activity.DashboardActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                    }
                    finishAffinity();
                }else {
                    Intent mIntent = new Intent(SplashActivity.this, SelectActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                    finish();
                }
            }
        },1000);

    }
}
