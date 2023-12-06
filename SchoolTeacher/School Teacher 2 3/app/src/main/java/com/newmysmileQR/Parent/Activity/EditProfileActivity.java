package com.newmysmileQR.Parent.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newmysmileQR.APICall.ParentHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.User;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Config;
import com.newmysmileQR.Utility.Preference;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

@RequiresApi(api = Build.VERSION_CODES.R)
public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
    String[] PERMISSIONS1 = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    String img = "";
    Bitmap mBitmap = null;
    AvatarView avProfile;
    TextView tvTitle, tvCode, etFirstName, etLastName, etFatherName, etMotherName, etDOB, tvMobile, etAddress;

    FloatingActionButton fabDone;
    private Uri imageUri;

    //Date
    private Calendar calendar = Calendar.getInstance();
    private int mYear = calendar.get(Calendar.YEAR);
    private int mMonth = calendar.get(Calendar.MONTH);
    private int mDay = calendar.get(Calendar.DAY_OF_MONTH);
    private String selectedDate = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);
        initToolbar();
        initView();
        fabDone.setOnClickListener(this);
        avProfile.setOnClickListener(this);
        etDOB.setOnTouchListener(this);
    }

    private void initView() {
        avProfile = findViewById(R.id.avProfile);
        fabDone = findViewById(R.id.fabDone);
        tvCode = findViewById(R.id.tvCode);
        tvTitle = findViewById(R.id.tvTitle);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etFatherName = findViewById(R.id.etFatherName);
        etMotherName = findViewById(R.id.etMotherName);
        etDOB = findViewById(R.id.etDOB);
        tvMobile = findViewById(R.id.tvMobile);
        etAddress = findViewById(R.id.etAddress);
        loadProfile();
    }

    private void loadProfile() {
        User mUser = Preference.getUser(this);
        if (mUser != null) {
            IImageLoader imageLoader = new PicassoLoader();
            imageLoader.loadImage(avProfile, mUser.getImage() != null && !mUser.getImage().equalsIgnoreCase("") ? mUser.getImage() : "no url", mUser.getName());

            tvMobile.setText(mUser.getPhone());
            String name = mUser.getFirstName() +" "+ mUser.getLastName();
            tvTitle.setText(name);
            tvCode.setText(mUser.getICNumber());
            etFirstName.setText(mUser.getFirstName());
            etLastName.setText(mUser.getLastName());
            etFatherName.setText(mUser.getFatherName());
            etMotherName.setText(mUser.getMotherName());
            etDOB.setText(mUser.getBirthdate());
            selectedDate = mUser.getBirthdate();
            etAddress.setText(mUser.getAddress());
            etFirstName.requestFocus();
//            Preference.showKeyboard(this);
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        mToolbar.setTitle("Edit Profile");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabDone: {
                if (isValid()) {
                    updateProfile();
                }
                break;
            }
            case R.id.avProfile: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Preference.hasPermissions(this, PERMISSIONS)) {
                        if (Environment.isExternalStorageManager()) {
                            OpenCameraAndGallery();
                        } else {
                            startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
                        }
                    } else {
                        Toast.makeText(this, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
                    }
                } else {
                    if (Preference.hasPermissions(this, PERMISSIONS1)) {
                        OpenCameraAndGallery();
                    } else {
                        Toast.makeText(this, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(this, PERMISSIONS1, 1);
                    }
                }
                break;
            }

        }
    }


    private boolean isValid() {
        if (TextUtils.isEmpty(etFirstName.getText())) {
            Toast.makeText(this, "Enter first name", Toast.LENGTH_SHORT).show();
            etFirstName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etLastName.getText())) {
            Toast.makeText(this, "Enter last name", Toast.LENGTH_SHORT).show();
            etLastName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etFatherName.getText())) {
            Toast.makeText(this, "Enter father name", Toast.LENGTH_SHORT).show();
            etFatherName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etMotherName.getText())) {
            Toast.makeText(this, "Enter mother name", Toast.LENGTH_SHORT).show();
            etMotherName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etDOB.getText())) {
            Toast.makeText(this, "Enter Date of birth", Toast.LENGTH_SHORT).show();
            etDOB.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etAddress.getText())) {
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
            etAddress.requestFocus();
            return false;
        }
        return true;
    }

    private void showDateDialog() {
        DatePickerDialog dialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month + 1;
                        mDay = dayOfMonth;
                        selectedDate = String.format(Locale.ENGLISH, "%04d-%02d-%02d", year, mMonth, dayOfMonth);
                        etDOB.setText(String.format(Locale.ENGLISH, "%02d-%02d-%04d", dayOfMonth, mMonth, year));
                    }
                }, mYear, mMonth - 1, mDay);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, mMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.YEAR, mYear);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dialog.show();
    }


    private void updateProfile() {
        if (Preference.isNetworkAvailable(this)) {
            String studentId = Preference.getUser(this).getId() + "";
            String FirstName = etFirstName.getText().toString();
            String LastName = etLastName.getText().toString();
            String FatherName = etFatherName.getText().toString();
            String MotherName = etMotherName.getText().toString();
            String birthDate = selectedDate;
            String address = etAddress.getText().toString();
            ParentHome.updateProfile(this, studentId, FirstName, LastName, FatherName, MotherName, birthDate, address, imageUri, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    Preference.saveUser(EditProfileActivity.this, response.getUser());
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Throwable t) {
                    setResult(RESULT_CANCELED);
                    Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (img.equalsIgnoreCase("1")) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    mBitmap = Preference.getResizedBitmap(mBitmap, Config.IMAGE_RESOLUTION);
                    avProfile.setImageBitmap(mBitmap);
                    avProfile.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Log.v("error", e.toString());
                }
            } else if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                try {
                    imageUri = data.getData();
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    mBitmap = Preference.getResizedBitmap(mBitmap, Config.IMAGE_RESOLUTION);
                    avProfile.setImageBitmap(mBitmap);
                    avProfile.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void OpenCameraAndGallery() {
        if (Preference.hasPermissions(this, PERMISSIONS)) {
            final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Upload Image");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Camera")) {
                        img = "1";
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "Profile " + System.currentTimeMillis());
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Upload profile");
                        imageUri = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(photoCaptureIntent, 20);
                    } else if (options[item].equals("Gallery")) {
                        img = "2";
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        }
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select picture"), 109);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
            Toast.makeText(this, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            showDateDialog();
        }
        return false;
    }
}
