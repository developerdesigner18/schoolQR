package com.newmysmileQR.Teacher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.Media;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.SchoolList;
import com.newmysmileQR.R;
import com.newmysmileQR.Teacher.Fragment.ImageListFragment;
import com.newmysmileQR.Teacher.Fragment.VideoListFragment;
import com.newmysmileQR.Utility.Preference;

public class MediaActivity extends AppCompatActivity {

    public Media mMedia;
    SchoolList schoolList;
    String code;
    private TabLayout tlMedia;
    private ViewPager vpMedia;
    private static final int REQUEST_SELECT_IMAGES = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        schoolList = new Gson().fromJson(getIntent().getStringExtra(Intent.EXTRA_TITLE), SchoolList.class);
        code = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        initToolbar();
        initView();
    }

    private void initView() {
        tlMedia = findViewById(R.id.tlMedia);
        vpMedia = findViewById(R.id.vpMedia);
        loadMedia();
    }

    private void loadMedia() {
        if (Preference.isNetworkAvailable(this)) {
            String schoolID = Preference.getUser(this).getSchoolId() + "";
            TeacherHome.mediaList(this, code + "", schoolID + "", new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    mMedia = response.getMedia();
                    vpMedia.setAdapter(new MediaPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.POSITION_NONE));
                    tlMedia.setupWithViewPager(vpMedia);
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(MediaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        mToolbar.setTitle("Student gallery");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    class MediaPageAdapter extends FragmentStatePagerAdapter {
        public MediaPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new Fragment();
            Bundle bundle = new Bundle();
            bundle.putString(Intent.EXTRA_TEXT, code);
            switch (position) {
                case 0: {
                    fragment = new ImageListFragment(code, mMedia.getImage());
//                    Toast.makeText(MediaActivity.this,"1 image", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 1: {
                    fragment = new VideoListFragment(code, mMedia.getVideo());
//                    Toast.makeText(MediaActivity.this,"2 video", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return "Image";
                }
                case 1: {
                    return "Video";
                }
            }
            return "";
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
