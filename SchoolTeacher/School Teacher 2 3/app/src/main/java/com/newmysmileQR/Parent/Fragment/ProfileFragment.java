package com.newmysmileQR.Parent.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.multidex.MultiDex;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newmysmileQR.APICall.ParentHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.Parent.Activity.EditProfileActivity;
import com.newmysmileQR.R;
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
    private TextView tvTitle, tvUpdate, tvCode, tvFirstName, tvLastName, tvFatherName, tvMotherName, tvDOB, tvMobile, tvAddress;
    private FloatingActionButton fabEdit;
    private SwipeRefreshLayout swipeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile2, container, false);
        initView();
        fabEdit.setOnClickListener(this);
        avProfile.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        return mView;
    }

    private void initView() {
        avProfile = mView.findViewById(R.id.avProfile);
        fabEdit = mView.findViewById(R.id.fabEdit);
        tvTitle = mView.findViewById(R.id.tvTitle);
        tvUpdate = mView.findViewById(R.id.tvUpdate);
        tvCode = mView.findViewById(R.id.tvCode);
        tvFirstName = mView.findViewById(R.id.tvFirstName);
        tvLastName = mView.findViewById(R.id.tvLastName);
        tvFatherName = mView.findViewById(R.id.tvFatherName);
        tvMotherName = mView.findViewById(R.id.tvMotherName);
        tvDOB = mView.findViewById(R.id.tvDOB);
        tvMobile = mView.findViewById(R.id.tvMobile);
        tvAddress = mView.findViewById(R.id.tvAddress);
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
            String name = mUser.getFirstName() + " " + mUser.getLastName();
            IImageLoader imageLoader = new PicassoLoader();
            imageLoader.loadImage(avProfile, mUser.getImage() != null && !mUser.getImage().equalsIgnoreCase("") ? mUser.getImage() : "no url", name);

            tvTitle.setText(name);
            tvCode.setText(mUser.getICNumber());
            tvFirstName.setText(mUser.getFirstName());
            tvLastName.setText(mUser.getLastName());
            tvFatherName.setText(mUser.getFatherName());
            tvMotherName.setText(mUser.getMotherName());
            tvDOB.setText(mUser.getBirthdate());
            tvMobile.setText(mUser.getPhone());
            tvAddress.setText(mUser.getAddress());

        } else {
            Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }*/

        ParentHome.parentsData(mContext, new ResponseCallback() {
            @Override
            public void onResponse(RootModel response) {
                if (response.getUser() != null) {
                    Preference.saveUser(mContext, response.getUser());
                    String name = response.getUser().getFirstName() + " " + response.getUser().getLastName();
                    IImageLoader imageLoader = new PicassoLoader();
                    imageLoader.loadImage(avProfile, response.getUser().getImage() != null && !response.getUser().getImage().equalsIgnoreCase("") ? response.getUser().getImage() : "no url", name);

                    tvTitle.setText(name);
                    tvCode.setText(response.getUser().getICNumber());
                    tvFirstName.setText(response.getUser().getFirstName());
                    tvLastName.setText(response.getUser().getLastName());
                    tvFatherName.setText(response.getUser().getFatherName());
                    tvMotherName.setText(response.getUser().getMotherName());
                    tvDOB.setText(response.getUser().getBirthdate());
                    tvMobile.setText(response.getUser().getPhone());
                    tvAddress.setText(response.getUser().getAddress());
                    swipeView.setRefreshing(false);
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
            case R.id.avProfile: {
                 /*Intent mIntent = new Intent(mContext, ImageActivity.class);
                mIntent.putExtra("url",imageUrl);
                mIntent.putExtra("title",tvName.getText());
                startActivity(mIntent);*/
                break;
            }
            case R.id.tvUpdate: {
                Preference.rateAction(mContext);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MultiDex.install(mContext);
    }
}
