package com.newmysmileQR.Teacher.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.Notification;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.Teacher.Adapter.AdapterNotification;
import com.newmysmileQR.Utility.Preference;

import java.util.ArrayList;

public class AllNotification extends AppCompatActivity {

    private RecyclerView rvNoti;
    private LinearLayout llEmptyContent;
    private Spinner spinner;
    private ImageView mIcon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notification);
        initToolbar();
        initView();

    }

    private void initView() {
        rvNoti = findViewById(R.id.rvNoti);

        llEmptyContent = findViewById(R.id.llEmptyContent);
        rvNoti.setLayoutManager(new GridLayoutManager(this, 1));
        getNotification();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.topToolbar);
        TextView mtitle = toolbar.findViewById(R.id.mTitle);
        TextView tvSpinner = toolbar.findViewById(R.id.tvSpinner);
        tvSpinner.setVisibility(View.GONE);
        mIcon = toolbar.findViewById(R.id.mIcon);
        mtitle.setText("Teacher notification");
        spinner = toolbar.findViewById(R.id.spDivision);
        spinner.setVisibility(View.GONE);

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

    private void getNotification() {
        if (Preference.isNetworkAvailable(this)) {
            String code = Preference.getUser(this).getSchool().getCode();
            String teacherId = Preference.getUser(this).getId() + "";
            TeacherHome.notificationList(this, teacherId, code, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    if (response.isSuccess() && response.getNotification() != null && response.getNotification().size() > 0) {
                        ArrayList<Notification> list = response.getNotification();
                        rvNoti.setAdapter(new AdapterNotification(AllNotification.this, list));
                        llEmptyContent.setVisibility(View.GONE);
                    } else {
                        rvNoti.setVisibility(View.GONE);
                        llEmptyContent.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(AllNotification.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    rvNoti.setVisibility(View.GONE);
                    llEmptyContent.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }

    }


}
