package com.newmysmileQR.Parent.Fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.newmysmileQR.Parent.Activity.DashboardActivity;

public class BaseFragment extends Fragment {
    DashboardActivity mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = (DashboardActivity)context;
        super.onAttach(context);
    }
}
