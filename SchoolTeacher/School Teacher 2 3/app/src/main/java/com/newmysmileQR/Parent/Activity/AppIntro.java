package com.newmysmileQR.Parent.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.newmysmileQR.Parent.Adapter.IntroAdapter;
import com.newmysmileQR.R;

public class AppIntro extends AppCompatActivity implements View.OnClickListener {

    private ViewPager vpintro;
    private TextView tvSkip;
    private TextView tvNext;
    private TabLayout tlindicate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_intro);
        initView();
    }

    private void initView() {
        vpintro = findViewById(R.id.vpintro);
        tvSkip = findViewById(R.id.tvSkip);
        tvNext = findViewById(R.id.tvNext);
        tlindicate = findViewById(R.id.tlindicate);

        tvSkip.setOnClickListener(this);
        tvNext.setOnClickListener(this);

        vpintro.setAdapter(new IntroAdapter(this));
        tlindicate.setupWithViewPager(vpintro);
        vpintro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    tvNext.setText("Finish");
                } else {
                    tvNext.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSkip: {
                Intent mIntent = new Intent(AppIntro.this, DashboardActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mIntent);
                finish();
                break;
            }
            case R.id.tvNext: {
                if (vpintro.getCurrentItem() + 1 < 3) {
                    vpintro.setCurrentItem(vpintro.getCurrentItem() + 1);
                } else {
                    Intent mIntent = new Intent(AppIntro.this, DashboardActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                finish();
                }

                break;
            }
        }
    }
}
