package com.newmysmileQR.Teacher.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.APIConstant;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.User;
import com.newmysmileQR.ImageActivity;
import com.newmysmileQR.R;
import com.newmysmileQR.Teacher.Activity.EditProfileActivity;
import com.newmysmileQR.Utility.Preference;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private AvatarView avProfile;
    private TextView tvTitle, tvName, tvMobile, tvAddress, tvCode, tvUpdate;

    private FloatingActionButton fabEdit;
    private SwipeRefreshLayout swipeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        initView();
        fabEdit.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        avProfile.setOnClickListener(this);
        return mView;
    }

    private void initView() {
        avProfile = mView.findViewById(R.id.avProfile);
        tvTitle = mView.findViewById(R.id.tvTitle);
        tvName = mView.findViewById(R.id.tvName);
        tvMobile = mView.findViewById(R.id.tvMobile);
        tvAddress = mView.findViewById(R.id.tvAddress);
        tvCode = mView.findViewById(R.id.tvCode);
        tvUpdate = mView.findViewById(R.id.tvUpdate);
        fabEdit = mView.findViewById(R.id.fabEdit);
        swipeView = mView.findViewById(R.id.swipeView);

        loadProfile();
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(false);
                loadProfile();
            }
        });
    }

    private void loadProfile() {
        /*User mUser = Preference.getUser(mContext);
        if (mUser != null) {
            IImageLoader imageLoader = new PicassoLoader();
            imageLoader.loadImage(avProfile, mUser.getImage() != null && !mUser.getImage().equalsIgnoreCase("") ? mUser.getImage() : APIConstant.BASE_URL, mUser.getName());

            tvMobile.setText(mUser.getPhone());
            tvAddress.setText(mUser.getAddress());
            tvCode.setText(mUser.getSchool().getCode());
            tvName.setText(mUser.getName());
            tvTitle.setText(mUser.getName());
        } else {
            Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }*/

        TeacherHome.teacherData(mContext, new ResponseCallback() {
            @Override
            public void onResponse(RootModel response) {
                Preference.saveUser(mContext, response.getUser());
                if (response.getUser() != null) {
                    IImageLoader imageLoader = new PicassoLoader();
                    imageLoader.loadImage(avProfile, response.getUser().getImage() != null && !response.getUser().getImage().equalsIgnoreCase("") ? response.getUser().getImage() : APIConstant.BASE_URL, response.getUser().getName());

                    tvMobile.setText(response.getUser().getPhone());
                    tvAddress.setText(response.getUser().getAddress());
                    tvCode.setText(response.getUser().getSchool().getCode());
                    tvName.setText(response.getUser().getName());
                    tvTitle.setText(response.getUser().getName());
                    swipeView.setRefreshing(false);
                } else {
                    swipeView.setRefreshing(false);
                    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabEdit: {
                Intent mIntent = new Intent(mContext, EditProfileActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(mIntent, 101);
                break;
            }
            case R.id.tvUpdate: {
                Preference.rateAction(mContext);
                break;
            }
            case R.id.avProfile: {
                User mUser = Preference.getUser(mContext);
                if (mUser != null && mUser.getImage() != null && !mUser.getImage().equalsIgnoreCase("")) {
                    Intent mIntent = new Intent(mContext, ImageActivity.class);
                    mIntent.putExtra("url", mUser.getImage());
                    mIntent.putExtra("title", tvName.getText());
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                loadProfile();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
