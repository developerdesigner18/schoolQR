package com.newmysmileQR.Teacher.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.APIConstant;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Preference;

import java.io.File;

public class StudentAddMultipleActivity extends AppCompatActivity implements View.OnClickListener {

    String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
    String[] PERMISSIONS1 = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    private Uri imageUri;

    private ImageView ivImage;
    private Button btnSubmit;
    private TextView tvSampleFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add_multiple);
        initToolbar();
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Preference.hasPermissions(this, PERMISSIONS)) {
                if (Environment.isExternalStorageManager()) {

                } else {
                    startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
                }
            } else {
                Toast.makeText(this, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
            }
        } else {
            if (Preference.hasPermissions(this, PERMISSIONS1)) {

            } else {
                Toast.makeText(this, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, PERMISSIONS1, 1);
            }
        }

        SpannableString spannableString = new SpannableString("Download Sample XLXs file");
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSampleFile.setText(spannableString);

        ivImage.setOnClickListener(this);
        tvSampleFile.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void initView() {
        ivImage = findViewById(R.id.ivImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvSampleFile = findViewById(R.id.tvSampleFile);
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        mToolbar.setTitle("Add multiple student");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImage: {
                Intent intent = new Intent();
                intent.setType("application/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select XLXs file"), 109);
                break;
            }
            case R.id.btnSubmit: {
                btnSubmit.setEnabled(false);
                if (isValid()) {
                    addMultipleStudent();
                } else {
                    btnSubmit.setEnabled(true);
                }
                break;
            }
            case R.id.tvSampleFile: {
                if (Preference.hasPermissions(this, PERMISSIONS)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(StudentAddMultipleActivity.this)
                            .setTitle("Download")
                            .setMessage("Are you sure want to download file?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Preference.DownloadFile(StudentAddMultipleActivity.this).execute(APIConstant.BASE_DOMAIN + "SchoolQR/public/assets/sample.xlsx");
                                    //https://mysmileqr.com/SchoolQR/public/assets/use%20this%20file.xlsx
                                }
                            })
                            .setNegativeButton("No", null)
                            .create();
                    alertDialog.show();
                } else {
                    Toast.makeText(this, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
                }
                break;
            }
        }
    }

    private boolean isValid() {
        Log.d("imageUri", "isValid: " + imageUri);
        if (imageUri == null) {
            Toast.makeText(this, "File not selected!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addMultipleStudent() {
        if (Preference.isNetworkAvailable(this)) {
            String code = Preference.getUser(this).getSchool().getCode();
            TeacherHome.addMultipleStudent(this, code, "", imageUri, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    Toast.makeText(StudentAddMultipleActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    btnSubmit.setEnabled(true);
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Throwable t) {
                    btnSubmit.setEnabled(true);
                    Log.d("getErrormessage", "onFailure: " + t.getMessage());
                    Toast.makeText(StudentAddMultipleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btnSubmit.setEnabled(true);
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
            }
            ivImage.setAlpha(0.5f);
            if (imageUri == null) {
                Toast.makeText(this, "File not found. Try again!", Toast.LENGTH_SHORT).show();
                return;
            }
            File file = new File(Preference.getFilePath(this, imageUri));
            if (!file.exists()) {
                Toast.makeText(this, "Selected file not present. Try again!", Toast.LENGTH_SHORT).show();
                imageUri = null;
                return;
            }
            String extension = Preference.getFileExtension(file.getAbsolutePath());
            if (!extension.equalsIgnoreCase(".xlsx")) {
                Toast.makeText(this, "Invalid file extension. Please select XLSX file!", Toast.LENGTH_SHORT).show();
                imageUri = null;
                return;
            }
            Toast.makeText(this, file.getName() + " selected", Toast.LENGTH_SHORT).show();
            ivImage.setAlpha(1.0f);
        } catch (Exception e) {
            imageUri = null;
            ivImage.setAlpha(0.5f);
            Log.d("getfiledata", "onActivityResult: " + e.getMessage());
            Toast.makeText(this, "File not found. Try again!" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
