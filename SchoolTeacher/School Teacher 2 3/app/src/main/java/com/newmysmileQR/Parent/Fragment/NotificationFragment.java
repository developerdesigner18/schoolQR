package com.newmysmileQR.Parent.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.newmysmileQR.APICall.ParentHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.Notification;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.Parent.Adapter.Adapter;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Preference;

import java.util.ArrayList;

public class NotificationFragment extends BaseFragment {
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private View mView;
    private RecyclerView rvList;
    private LinearLayout llEmptyContent;
    private SwipeRefreshLayout swipeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notification2, container, false);
        initView();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        return mView;
    }

    private void initView() {
        llEmptyContent = mView.findViewById(R.id.llEmptyContent);
        swipeView = mView.findViewById(R.id.swipeView);
        rvList = mView.findViewById(R.id.rvList);
        rvList.setLayoutManager(new GridLayoutManager(mContext, 1));

        swipeView.setOnRefreshListener(() -> {
            getNotification();
            swipeView.setRefreshing(false);
        });
        getNotification();
    }

    private void getNotification() {

        if (Preference.isNetworkAvailable(mContext)) {
        ParentHome.notificationList(mContext, new ResponseCallback() {
            @Override
            public void onResponse(RootModel response) {
                if (response.isSuccess() && response.getNotification() != null && response.getNotification().size() > 0) {
                    ArrayList<Notification> list = response.getNotification();
                    llEmptyContent.setVisibility(View.GONE);
                    rvList.setAdapter(new Adapter(mContext, list));
                } else {
                    rvList.setVisibility(View.GONE);
                    llEmptyContent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                rvList.setVisibility(View.GONE);
                llEmptyContent.setVisibility(View.VISIBLE);
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });}else {
            Toast.makeText(mContext, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAdapter() {

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
