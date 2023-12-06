package com.newmysmileQR.Teacher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newmysmileQR.APICall.ParentHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.Teacher.Adapter.SchoolAdapter;
import com.newmysmileQR.Utility.Preference;

public class School extends AppCompatActivity {
    String code;

    private RecyclerView rvList;
    private LinearLayout llEmptyContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        code = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        TextView mTitle = mToolbar.findViewById(R.id.mTitle);
        TextView tvSpinner = mToolbar.findViewById(R.id.tvSpinner);
        tvSpinner.setVisibility(View.GONE);
        mTitle.setText("School list");

    }

    private void initView() {
        rvList = findViewById(R.id.rvList);
        llEmptyContent = findViewById(R.id.llEmptyContent);
        rvList.setLayoutManager(new GridLayoutManager(this, 1));

        if (Preference.isNetworkAvailable(this)) {

            ParentHome.getSchool(this, code, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    if (response.isSuccess() && response.getSchoolList() != null && response.getSchoolList().size() > 0) {
                        rvList.setAdapter(new SchoolAdapter(School.this, response.getSchoolList(), code));
                        llEmptyContent.setVisibility(View.GONE);
                    } else {
                        rvList.setVisibility(View.GONE);
                        llEmptyContent.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.getMessage();
                    rvList.setVisibility(View.GONE);
                    llEmptyContent.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }


}
