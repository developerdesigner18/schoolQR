package com.newmysmileQR.Parent.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.newmysmileQR.APIModel.User;
import com.newmysmileQR.Parent.Adapter.SchoolListAdapter;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Preference;

public class SchoolListActivity extends AppCompatActivity {
    String code;

    private RecyclerView rvList;
    private LinearLayout llEmptyContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);
        code = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        TextView tvSpinner = findViewById(R.id.tvSpinner);
        tvSpinner.setVisibility(View.GONE);
        TextView mTitle = mToolbar.findViewById(R.id.mTitle);
        mTitle.setText("School list");

    }

    private void initView() {
        rvList = findViewById(R.id.rvList);
        llEmptyContent = findViewById(R.id.llEmptyContent);
        rvList.setLayoutManager(new GridLayoutManager(this, 1));
        User mUser = Preference.getUser(this);
        Log.d("getusenricumber", "initView: " + mUser.getICNumber());

        if (Preference.isNetworkAvailable(this)) {

            ParentHome.getSchool(this, Preference.getUser(this).getICNumber() + "", new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    if (response.isSuccess() && response.getSchoolList() != null && response.getSchoolList().size() > 0) {
                        rvList.setAdapter(new SchoolListAdapter(SchoolListActivity.this, code, response.getSchoolList()));
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
