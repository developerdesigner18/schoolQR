package com.newmysmileQR.Parent.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newmysmileQR.R;

public class NotificationImage extends AppCompatActivity {
    private ImageView mIcon;
    private ImageView ivInfo;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_image2);
        imageUrl = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        initToolbar();
        initView();
    }

    private void initView() {
        ivInfo = findViewById(R.id.ivInfo);
        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_image_placeholder).into(ivInfo);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.topToolbar);
        TextView mTitle = toolbar.findViewById(R.id.mTitle);
        TextView tvSpinner = toolbar.findViewById(R.id.tvSpinner);
        tvSpinner.setVisibility(View.GONE);
        mTitle.setText("Document");
        mIcon = toolbar.findViewById(R.id.mIcon);
        mIcon.setImageResource(R.drawable.ic_back);
        int padding = (int) getResources().getDimension(R.dimen._8sdp);
        mIcon.setPadding(padding, padding, padding, padding);

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
