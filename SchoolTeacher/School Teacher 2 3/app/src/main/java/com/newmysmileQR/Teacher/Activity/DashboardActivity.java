package com.newmysmileQR.Teacher.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.newmysmileQR.APICall.Authentication;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.ClassModel;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.SelectActivity;
import com.newmysmileQR.Teacher.Fragment.NotificationFragment;
import com.newmysmileQR.Teacher.Fragment.ProfileFragment;
import com.newmysmileQR.Teacher.Fragment.QRScanFragment;
import com.newmysmileQR.Teacher.Fragment.StudentListFragment;
import com.newmysmileQR.Utility.Config;
import com.newmysmileQR.Utility.Preference;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {


    public TextView mTitle;
    //Division
    public BottomNavigationView bottomToolbar;
    boolean exit = false;
    private Toolbar topToolbar;
    private SpinnerAdapter mAdapter;
    public FrameLayout flSpinner;
    private Spinner spDivision;
    private TextView tvSpinner;
    private int selectedPos = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initToolbar();
      loadPermission();
    }
    private void initToolbar() {
        try {
            topToolbar = findViewById(R.id.topToolbar);
            mTitle = topToolbar.findViewById(R.id.mTitle);
            spDivision = topToolbar.findViewById(R.id.spDivision);
            flSpinner = topToolbar.findViewById(R.id.flSpinner);
            tvSpinner = topToolbar.findViewById(R.id.tvSpinner);
            mTitle.setText("Dashboard");
            topToolbar.inflateMenu(R.menu.top_dashboard);
            bottomToolbar = findViewById(R.id.bottomToolbar);
            spDivision.setVisibility(View.VISIBLE);
            spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    if (arg1 != null)
                        ((TextView) arg1).setText(null);
                    ClassModel classModel = (ClassModel) spDivision.getItemAtPosition(position);
                    tvSpinner.setText(classModel.getStandardData().getTitle());
                    Preference.setClassId(DashboardActivity.this, classModel.getStandardId());
                    switch (selectedPos) {
                        case Config.POS_STUDENT_LIST_FRAGMENT: {
                            Bundle bundle = new Bundle();
                            swipeFragment(Config.VIEW_STUDENT_LIST_FRAGMENT, bundle);
                            break;
                        }
                        case Config.POS_NOTIFICATION_FRAGMENT: {
                            Bundle bundle = new Bundle();
                            swipeFragment(Config.VIEW_NOTIFICATION_FRAGMENT, bundle);
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            topToolbar.setOnMenuItemClickListener(this);
            bottomToolbar.setOnNavigationItemSelectedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mLogout: {
                showLogoutDialog();
                break;
            }
            case R.id.mFilter: {
                openFilterDialog();
                break;
            }
            case R.id.mNotification: {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(DashboardActivity.this, AllNotification.class));
                    }
                }, 250);

            }
        }
        return false;
    }
    private void openFilterDialog() {
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Bundle bundle = new Bundle();
        switch (menuItem.getItemId()) {
            case R.id.mScan: {
                if (selectedPos != Config.POS_QR_SCAN_FRAGMENT) {
                    swipeFragment(Config.VIEW_QR_SCAN_FRAGMENT, bundle);
                }
                break;
            }
            case R.id.mNotification: {
                if (selectedPos != Config.POS_NOTIFICATION_FRAGMENT) {
                    swipeFragment(Config.VIEW_NOTIFICATION_FRAGMENT, bundle);
                }
                break;
            }
            case R.id.mProfile: {
                if (selectedPos != Config.POS_PROFILE_FRAGMENT) {
                    swipeFragment(Config.VIEW_PROFILE_FRAGMENT, bundle);
                }
                break;
            }
            case R.id.mList: {
                if (selectedPos != Config.POS_STUDENT_LIST_FRAGMENT) {
                    swipeFragment(Config.VIEW_STUDENT_LIST_FRAGMENT, bundle);
                }
                break;
            }
        }
        return true;
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    public void Logout() {
        Preference.logout(this);
        Intent mIntent = new Intent(this, SelectActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mIntent);
        finishAffinity();
    }

    public void loadPermission() {
        try {
            if (Preference.isNetworkAvailable(this)) {
                String code = Preference.getUser(this).getSchool().getCode();
                String teacherId = Preference.getUser(this).getId() + "";
                Authentication.getPermission(this, code, teacherId, new ResponseCallback() {
                    @Override
                    public void onResponse(RootModel response) {
                        Preference.setPermission(DashboardActivity.this, response.getPermissions());
                        Log.d("PERMISSION", response.getPermissions() + "");
                        Menu menu = bottomToolbar.getMenu();
                        MenuItem notification = menu.findItem(R.id.mNotification);
                        notification.setVisible(Preference.isNotificationPermission(DashboardActivity.this));
                        getClassList();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Preference.setClassId(DashboardActivity.this, -1);
                    }
                });
            } else {
                Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
                return;
            }

            swipeFragment(Config.VIEW_QR_SCAN_FRAGMENT, new Bundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getClassList() {
        if (Preference.isNetworkAvailable(this)) {
            String code = Preference.getUser(this).getSchool().getCode();
            String teacherId = Preference.getUser(this).getId() + "";

            Log.d("response_data", "onResponse: " + code + " " + teacherId);

            TeacherHome.classList(this, code, teacherId, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    Log.d("CLASSLIST", response.getClassList() + "");
                    ArrayList<ClassModel> list = new ArrayList<>();
                    if (response.getClassList() != null && response.getClassList().size() > 0) {
                        ClassModel all = new ClassModel();
                        all.setStandardId(0);
                        ClassModel.StandardData data = new ClassModel.StandardData();
                        data.setTitle("All");
                        data.setId(0);
                        all.setStandardData(data);
                        list.add(all);
                        list.addAll(response.getClassList());
                        Log.d("response_data", "onResponse: " + response.getMessage());
                    }
                    mAdapter = new ArrayAdapter<>(DashboardActivity.this, R.layout.support_simple_spinner_dropdown_item, list);
                    spDivision.setAdapter(mAdapter);
                    if (list == null && list.size() < 0) {
                        Preference.setClassId(DashboardActivity.this, -1);
                        tvSpinner.setText("");
                    } else if (response.getClassList() != null) {
                        tvSpinner.setText(list.get(0).toString());
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Preference.setClassId(DashboardActivity.this, -1);
                    tvSpinner.setText("");
                }
            });
        } else {
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        if (spDivision != null) {
            ClassModel classModel = (ClassModel) spDivision.getSelectedItem();
            if (classModel != null) {
                Preference.setClassId(this, classModel.getStandardId());
            }
        }
        super.onResume();
    }

    // Inside the swipeFragment method
    public void swipeFragment(String strFragment, Bundle bundle) {
        spDivision.setVisibility(View.VISIBLE);
        tvSpinner.setVisibility(View.VISIBLE);
        Menu menu = topToolbar.getMenu();
        MenuItem filter = menu.findItem(R.id.mFilter);
        filter.setVisible(false);
        Fragment mFragment = new Fragment();

        switch (strFragment) {
            case Config.VIEW_QR_SCAN_FRAGMENT: {
                selectedPos = Config.POS_QR_SCAN_FRAGMENT;
                mFragment = new QRScanFragment();
                MenuItem item = bottomToolbar.getMenu().findItem(R.id.mScan);
                mTitle.setText(item.getTitle());
                item.setChecked(true);
                spDivision.setVisibility(View.GONE);
                tvSpinner.setVisibility(View.GONE);
                break;
            }
            case Config.VIEW_STUDENT_LIST_FRAGMENT: {
                selectedPos = Config.POS_STUDENT_LIST_FRAGMENT;
                mFragment = new StudentListFragment();
                MenuItem item = bottomToolbar.getMenu().findItem(R.id.mList);
                mTitle.setText(item.getTitle());
                item.setChecked(true);
                break;
            }
            case Config.VIEW_NOTIFICATION_FRAGMENT: {
                selectedPos = Config.POS_NOTIFICATION_FRAGMENT;
                mFragment = new NotificationFragment();
                mFragment.setArguments(bundle);
                MenuItem item = bottomToolbar.getMenu().findItem(R.id.mNotification);
                mTitle.setText("Notifications sent");
                item.setChecked(true);
                filter.setVisible(false);
                break;
            }
            case Config.VIEW_PROFILE_FRAGMENT: {
                selectedPos = Config.POS_PROFILE_FRAGMENT;
                mFragment = new ProfileFragment();
                MenuItem item = bottomToolbar.getMenu().findItem(R.id.mProfile);
                mTitle.setText(item.getTitle());
                item.setChecked(true);
                spDivision.setVisibility(View.GONE);
                tvSpinner.setVisibility(View.GONE);
                break;
            }
        }

        if (mFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, mFragment)
                    .commit();
        }
    }


    @Override
    public void onBackPressed() {

        switch (selectedPos) {
            case Config.POS_PROFILE_FRAGMENT:
            case Config.POS_STUDENT_LIST_FRAGMENT:
            case Config.POS_NOTIFICATION_FRAGMENT: {
                Bundle bundle = new Bundle();
                swipeFragment(Config.VIEW_QR_SCAN_FRAGMENT, bundle);
                break;
            }
            case Config.POS_QR_SCAN_FRAGMENT:
            default: {
                if (!exit) {
                    Toast.makeText(this, "Press again to Exit", Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 2000);
                } else {
                    super.onBackPressed();
                }
                break;
            }
        }
    }
}
