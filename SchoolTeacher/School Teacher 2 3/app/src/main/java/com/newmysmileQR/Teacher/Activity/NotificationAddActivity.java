package com.newmysmileQR.Teacher.Activity;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.ClassModel;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Config;
import com.newmysmileQR.Utility.Preference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import kotlin.jvm.internal.Intrinsics;

@RequiresApi(api = Build.VERSION_CODES.R)
public class NotificationAddActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView mTitle;
    public ImageView mIcon;
    public Button btnSend;
    //Division
    public FrameLayout flSpinner;

    String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
    String[] PERMISSIONS1 = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    String img = "";
    Bitmap mBitmap = null;
    String classID;
    private EditText etTitle;
    private EditText etDesc;
    private Uri imageUri;
    private ImageView ivImage;
    private SpinnerAdapter mAdapter;
    private Spinner spDivision;
    private TextView tvSpinner;
    private String[] classes = new String[]{};
    RadioButton radio_yes, radio_no;
    File pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_add);

        initToolbar();
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
            } else {
                startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            }
        }
        ivImage.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    private void getClassList() {
        if (Preference.isNetworkAvailable(this)) {
            String code = Preference.getUser(this).getSchool().getCode();
            String teacherId = Preference.getUser(this).getId() + "";
            TeacherHome.classList(this, code, teacherId, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    if (response.getClassList() != null && response.getClassList().size() == 0) {
                        Preference.setClassId(NotificationAddActivity.this, -1);
                        tvSpinner.setText("");
                    }
                    mAdapter = new ArrayAdapter<>(NotificationAddActivity.this, R.layout.support_simple_spinner_dropdown_item, response.getClassList());
                    spDivision.setAdapter(mAdapter);
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(NotificationAddActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Preference.setClassId(NotificationAddActivity.this, -1);
                    tvSpinner.setText("");
                }
            });
        } else {
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        ivImage = findViewById(R.id.ivImage);
        btnSend = findViewById(R.id.btnSend);
        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
    }

    private void initToolbar() {
        Toolbar topToolbar = findViewById(R.id.topToolbar);
        mTitle = topToolbar.findViewById(R.id.mTitle);
        mIcon = topToolbar.findViewById(R.id.mIcon);
        spDivision = topToolbar.findViewById(R.id.spDivision);
        flSpinner = topToolbar.findViewById(R.id.flSpinner);
        tvSpinner = topToolbar.findViewById(R.id.tvSpinner);
        mTitle.setText("Send notification");
        spDivision.setVisibility(View.VISIBLE);

        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (arg1 != null)
                    ((TextView) arg1).setText(null);
                ClassModel classModel = (ClassModel) spDivision.getItemAtPosition(position);
                tvSpinner.setText(classModel.getStandardData().getTitle());
                classID = classModel.getStandardId() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mIcon.setImageResource(R.drawable.ic_back);
        int padding = (int) getResources().getDimension(R.dimen._8sdp);
        mIcon.setPadding(padding, padding, padding, padding);
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getClassList();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImage: {
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
            case R.id.btnSend: {
                if (isValid()) {
                    sendNotification();
                }
                break;
            }
        }
    }

    private void sendNotification() {
        String teacherID = Preference.getUser(this).getId() + "";
        String title = etTitle.getText().toString();
        String description = etDesc.getText().toString();

        String downloadable;
        if (radio_yes.isChecked()) {
            downloadable = "1";
        } else {
            downloadable = "0";
        }

//         pdfFile = new File(imageUri.getPath());
//        Log.d("mobil_io", "sendNotification: "+);
        TeacherHome.sendNotification(this, title, description, classID, teacherID, downloadable, pdfFile, new ResponseCallback() {
            @Override
            public void onResponse(RootModel response) {
                if (response.isSuccess()) {
                    Toast.makeText(NotificationAddActivity.this, response.getMessage() + "", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(NotificationAddActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            Toast.makeText(this, "Enter notification title", Toast.LENGTH_SHORT).show();
            etTitle.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(etDesc.getText().toString().trim())) {
            Toast.makeText(this, "Enter notification title", Toast.LENGTH_SHORT).show();
            etDesc.requestFocus();
            return false;
        }
        if (classID == null) {
            Toast.makeText(this, "You don't have any class assigned", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void OpenCameraAndGallery() {
        if (Preference.hasPermissions(this, PERMISSIONS)) {
            final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Upload image");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Camera")) {
                        img = "1";
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "Notification " + System.currentTimeMillis());
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Add notification");
                        imageUri = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(photoCaptureIntent, 20);
                    } else if (options[item].equals("Gallery")) {
                        img = "2";
                        Intent intent = new Intent();
                        intent.setType("*/*");
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
            Toast.makeText(this, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (img.equalsIgnoreCase("1")) {
                try {
                    pdfFile = new File(getRealPathFromURI(imageUri, NotificationAddActivity.this));
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    mBitmap = Preference.getResizedBitmap(mBitmap, Config.IMAGE_RESOLUTION);
                    ivImage.setImageBitmap(mBitmap);
                    ivImage.setVisibility(View.VISIBLE);
                    ExifInterface ei = new ExifInterface(Preference.getFilePath(this, imageUri));
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            mBitmap = rotateImage(mBitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            mBitmap = rotateImage(mBitmap, 180);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            mBitmap = rotateImage(mBitmap, 270);
                            break;
                    }
                    ivImage.setImageBitmap(mBitmap);
                    ivImage.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Log.v("error", e.toString());

                }
            } else if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                Log.d("file_chwckig", "onActivityResult: " + imageUri);
                Log.d("getmimetype", "onActivityResult: " + getContentResolver().getType(imageUri));
//                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                    mBitmap = Preference.getResizedBitmap(mBitmap, Config.IMAGE_RESOLUTION);
                    ivImage.setImageURI(imageUri);
                    ivImage.setVisibility(View.VISIBLE);
                pdfFile = new File(getRealPathFromURI(imageUri, NotificationAddActivity.this));

//                String sss= getRealPathFromURI(imageUri, NotificationAddActivity.this);
//                Log.d("get_pathsss", "onActivityResult: "+sss);


                Log.d("get_paths", "onActivityResult: " + pdfFile.getPath());
                Log.d("get_paths", "onActivityResult: " + pdfFile.getAbsolutePath());
                Log.d("get_paths", "onActivityResult: " + pdfFile.getAbsoluteFile());
                Log.d("get_paths", "onActivityResult: " + pdfFile.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @NonNull
    public final String getRealPathFromURI(@NotNull Uri uri, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(uri, "uri");
        Intrinsics.checkNotNullParameter(context, "context");
        Cursor returnCursor = context.getContentResolver().query(uri, (String[]) null, (String) null, (String[]) null, (String) null);
        Intrinsics.checkNotNull(returnCursor);
        int nameIndex = returnCursor.getColumnIndex("_display_name");
        int sizeIndex = returnCursor.getColumnIndex("_size");
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        String size = String.valueOf(returnCursor.getLong(sizeIndex));
        File file = new File(context.getFilesDir(), name);

        try {

            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1048576;
            int bytesAvailable = inputStream != null ? inputStream.available() : 0;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffers = new byte[bufferSize];

            while (true) {
                Integer var16 = inputStream != null ? inputStream.read(buffers) : null;
                boolean var18 = false;
                if (var16 != null) {
                    read = var16;
                }

                if (var16 != null) {
                    if (var16 == -1) {
                        Log.e("File Size", "Size " + file.length());
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        outputStream.close();
                        Log.e("File Path", "Path " + file.getPath());
                        break;
                    }
                }
                outputStream.write(buffers, 0, read);
            }

        } catch (Exception var19) {
            String var10001 = var19.getMessage();
            Intrinsics.checkNotNull(var10001);
            Log.e("Exception", var10001);
        }
        return file.getPath();
    }
}
