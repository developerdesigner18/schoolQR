package com.newmysmileQR.Teacher.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.Teacher.Activity.NotificationAddActivity;
import com.newmysmileQR.Teacher.Adapter.Adapter;
import com.newmysmileQR.Utility.Preference;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private RecyclerView rvList;
    private LinearLayout llEmptyContent;
    private SwipeRefreshLayout swipeView;
    private FloatingActionButton fabAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notification, container, false);
        initView();
        fabAdd.setOnClickListener(this);
        return mView;
    }

    private void initView() {
        llEmptyContent = mView.findViewById(R.id.llEmptyContent);
        swipeView = mView.findViewById(R.id.swipeView);
        fabAdd = mView.findViewById(R.id.fabAdd);
        rvList = mView.findViewById(R.id.rvList);
        rvList.setLayoutManager(new GridLayoutManager(mContext, 1));

        swipeView.setOnRefreshListener(() -> {
            swipeView.setRefreshing(false);
            getNotification();
        });

       // getNotification();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (swipeView.isRefreshing()) {
            swipeView.setRefreshing(false);
        } else {
            //swipeView.setRefreshing(true);
            getNotification();
        }
    }

    private void getNotification() {

        String teacherID = Preference.getUser(mContext).getId() + "";
        String standardID = Preference.getClassId(mContext) + "";

        Log.d("notifydata", "getNotification: " + teacherID + " " + standardID);

        if (Preference.isNetworkAvailable(mContext)) {
            if (standardID != null && !standardID.equals("-1")) {
                TeacherHome.studentByNotification(mContext, teacherID, standardID, new ResponseCallback() {
                    @Override
                    public void onResponse(RootModel response) {

                        Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();

                        swipeView.setRefreshing(false);
                        if (response.isSuccess() && response.getStudNotification() != null && response.getStudNotification().size() > 0) {
                            rvList.setAdapter(new Adapter(mContext, response.getStudNotification()));
                            rvList.setVisibility(View.VISIBLE);
                            llEmptyContent.setVisibility(View.GONE);
                        } else {
                            rvList.setVisibility(View.GONE);
                            llEmptyContent.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        swipeView.setRefreshing(false);
                        rvList.setVisibility(View.GONE);
                        llEmptyContent.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                fabAdd.setVisibility(View.GONE);
                llEmptyContent.setVisibility(View.VISIBLE);
                swipeView.setRefreshing(false);
            }
        } else {
            swipeView.setRefreshing(false);
            Toast.makeText(mContext, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAdd: {
                Intent mIntent = new Intent(mContext, NotificationAddActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(mIntent, 108);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 108) {
            if (resultCode == Activity.RESULT_OK) {
                getNotification();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
