package com.newmysmileQR.Parent.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.newmysmileQR.Parent.Fragment.NotificationFragment;
import com.newmysmileQR.Parent.Fragment.ProfileFragment;
import com.newmysmileQR.Parent.Fragment.QRScanFragment;
import com.newmysmileQR.R;
import com.newmysmileQR.SelectActivity;
import com.newmysmileQR.Utility.Config;
import com.newmysmileQR.Utility.Preference;


public class DashboardActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public Toolbar mToolbar;
    public BottomNavigationView bottomToolbar;
    boolean exit = false;
    private TextView mTitle;
    private int selectedPos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        initToolbar();
        initView();
        bottomToolbar.setOnNavigationItemSelectedListener(this);
    }

    private void initView() {
        bottomToolbar = findViewById(R.id.bottomToolbar);
        swipeFragment(Config.VIEW_QR_SCAN_FRAGMENT, null);
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.mToolbar);
        mTitle = findViewById(R.id.mTitle);
        mTitle.setText("Dashboard");
        mToolbar.inflateMenu(R.menu.top_dashboard);
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mLogout: {
                showLogoutDialog();
                break;
            }
        }
        return false;
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

    private void Logout() {
        Preference.logout(this);
        Intent mIntent = new Intent(this, SelectActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mIntent);
        finishAffinity();
    }

    public void swipeFragment(String strFragment, Bundle bundle) {
        Fragment mFragment = new Fragment();
        Menu menu = mToolbar.getMenu();
        MenuItem filter = menu.findItem(R.id.mFilter);
        MenuItem notification = menu.findItem(R.id.mNotification);
        filter.setVisible(false);
        notification.setVisible(false);

        switch (strFragment) {
            case Config.VIEW_QR_SCAN_FRAGMENT: {
                selectedPos = Config.POS_QR_SCAN_FRAGMENT;
                mFragment = new QRScanFragment();
                MenuItem item = bottomToolbar.getMenu().findItem(R.id.mScan);
                mTitle.setText(item.getTitle());
                item.setChecked(true);
                break;
            }
            case Config.VIEW_NOTIFICATION_FRAGMENT: {
                selectedPos = Config.POS_NOTIFICATION_FRAGMENT;
                mFragment = new NotificationFragment();
                MenuItem item = bottomToolbar.getMenu().findItem(R.id.mNotification);
                mTitle.setText("Notifications received");
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
                break;
            }
        }
        mFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, mFragment).commit();
    }

    @Override
    public void onBackPressed() {
        switch (selectedPos) {
            case Config.POS_PROFILE_FRAGMENT:
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
