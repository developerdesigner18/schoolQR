package com.newmysmileQR.Parent.Fragment;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.newmysmileQR.Parent.Activity.SchoolListActivity;
import com.newmysmileQR.R;
import com.newmysmileQR.ScanActivity;
import com.newmysmileQR.Utility.Preference;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.R)
public class QRScanFragment extends BaseFragment implements View.OnClickListener {
    String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
    String[] PERMISSIONS1 = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    String img = "";
    Bitmap mBitmap = null;
    private View mView;
    private Button btnScanQR, btnUploadQR, btnViewStudent;
    private CircleImageView ivProfile;
    private TextView tvParentName;

    private ArrayList<Image> images;
    private Uri imageUri;
    private BarcodeDetector detector;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_qrscan2, container, false);
        iniView();
        btnScanQR.setOnClickListener(this);
        btnUploadQR.setOnClickListener(this);
        btnViewStudent.setOnClickListener(this);
        return mView;
    }

    @SuppressLint("SetTextI18n")
    private void iniView() {
        btnScanQR = mView.findViewById(R.id.btnScanQR);
        btnUploadQR = mView.findViewById(R.id.btnUploadQR);
        btnViewStudent = mView.findViewById(R.id.btnViewStudent);
        ivProfile = mView.findViewById(R.id.ivProfile);
        tvParentName = mView.findViewById(R.id.tvParentName);

        tvParentName.setText(Preference.getUser(mContext).getFirstName() + " " + Preference.getUser(mContext).getLastName());

        if (Preference.getUser(mContext).getImage() != null) {
            Glide.with(this).load(Preference.getUser(mContext).getImage()).into(ivProfile);
        }

        detector = new BarcodeDetector.Builder(mContext)
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScanQR: {
                Intent mIntent = new Intent(mContext, ScanActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(mIntent, 108);
                break;
            }
            case R.id.btnUploadQR: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Preference.hasPermissions(mContext, PERMISSIONS)) {
                        if (Environment.isExternalStorageManager()) {
                            OpenCameraAndGallery();
                        } else {
                            startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
                        }
                    } else {
                        Toast.makeText(mContext, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(mContext, PERMISSIONS, 1);
                    }
                } else {
                    if (Preference.hasPermissions(mContext, PERMISSIONS1)) {
                        OpenCameraAndGallery();
                    } else {
                        Toast.makeText(mContext, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(mContext, PERMISSIONS1, 1);
                    }
                }
                break;
            }
            case R.id.btnViewStudent: {
                Intent mIntent = new Intent(mContext, SchoolListActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mIntent.putExtra(Intent.EXTRA_TEXT, Preference.getUser(getActivity()).getICNumber());
                startActivity(mIntent);
                break;
            }
        }
    }

    private void OpenCameraAndGallery() {
        if (hasPermissions(mContext, PERMISSIONS)) {
            final CharSequence[] options = {/*"Camera",*/ "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Upload QR");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    /*if (options[item].equals("Camera")) {
                        img = "1";
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "QR Code" + System.currentTimeMillis());
                        values.put(MediaStore.Images.Media.DESCRIPTION, "Scan QR code");
                        imageUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(photoCaptureIntent, 109);
                    } else*/
                    if (options[item].equals("Gallery")) {
                        ImagePicker.with(mContext)                         //  Initialize ImagePicker with activity or fragment context
                                .setToolbarColor("#F8B23C")         //  Toolbar color
                                .setStatusBarColor("#F8B23C")       //  StatusBar color (works with SDK >= 21  )
                                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                                .setProgressBarColor("#F8B23C")     //  ProgressBar color
                                .setBackgroundColor("#FFFFFF")      //  Background color
                                .setCameraOnly(false)               //  Camera mode
                                .setMultipleMode(false)              //  Select multiple images or single image
                                .setFolderMode(true)                //  Folder mode
                                .setShowCamera(false)                //  Show camera button
                                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                                .setDoneTitle("Done")               //  Done button title
                                /* .setLimitMessage("You have reached selection limit")*/    // Selection limit message
                                .setMaxSize(1)                     //  Max images can be selected
                                .setSavePath("Image picker")         //  Image capture folder name
                                .setSelectedImages(images)          //  Selected images
                                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                                .setRequestCode(100)                //  Set request code, default Config.RC_PICK_IMAGES
                                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                                .start();
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            ActivityCompat.requestPermissions(mContext, PERMISSIONS, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 108) {
            if (resultCode == Activity.RESULT_OK) {
                String code = data.getStringExtra(Intent.EXTRA_TEXT);
                Intent mIntent = new Intent(mContext, SchoolListActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mIntent.putExtra(Intent.EXTRA_TEXT, code);
                startActivity(mIntent);
            }
        } else if (requestCode == 109) {
            try {
                String code = "";
                if (img.equalsIgnoreCase("1")) {
                    mBitmap = decodeBitmapUri(imageUri);
                } else if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    imageUri = data.getData();
                    mBitmap = decodeBitmapUri(imageUri);
                }

                if (detector.isOperational() && mBitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(mBitmap).build();
                    SparseArray<Barcode> barcodes = detector.detect(frame);
                    for (int index = 0; index < barcodes.size(); index++) {
                        Barcode barCode = barcodes.valueAt(index);
                        int type = barcodes.valueAt(index).valueFormat;
                        switch (type) {
                            case Barcode.CONTACT_INFO:
                                code = barCode.contactInfo.title;
                                Log.i("QRCODEAPP", barCode.contactInfo.title);
                                break;
                            case Barcode.EMAIL:
                            case Barcode.TEXT:
                                code = barCode.displayValue;
                                Log.i("QRCODEAPP", barCode.displayValue);
                                break;
                            case Barcode.PHONE:
                                code = barCode.phone.number;
                                Log.i("QRCODEAPP", barCode.phone.number);
                                break;
                            case Barcode.SMS:
                                code = barCode.sms.message;
                                Log.i("QRCODEAPP", barCode.sms.message);
                                break;
                            case Barcode.URL:
                                code = "url: " + barCode.displayValue;
                                Log.i("QRCODEAPP", "url: " + barCode.displayValue);
                                break;
                            case Barcode.WIFI:
                                code = barCode.wifi.ssid;
                                Log.i("QRCODEAPP", barCode.wifi.ssid);
                                break;
                            case Barcode.GEO:
                                code = barCode.geoPoint.lat + ":" + barCode.geoPoint.lng;
                                Log.i("QRCODEAPP", barCode.geoPoint.lat + ":" + barCode.geoPoint.lng);
                                break;
                            case Barcode.CALENDAR_EVENT:
                                code = barCode.calendarEvent.description;
                                Log.i("QRCODEAPP", barCode.calendarEvent.description);
                                break;
                            case Barcode.DRIVER_LICENSE:
                                code = barCode.driverLicense.licenseNumber;
                                Log.i("QRCODEAPP", barCode.driverLicense.licenseNumber);
                                break;
                            case Barcode.ISBN:
                            case Barcode.PRODUCT:
                            default:
                                code = barCode.rawValue;
                                Log.i("QRCODEAPP", barCode.rawValue);
                                break;
                        }
                    }
                    if (barcodes.size() == 0) {
                        Toast.makeText(mContext, "No barcode could be detected. Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent mIntent = new Intent(mContext, SchoolListActivity.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mIntent.putExtra(Intent.EXTRA_TEXT, code);
                        startActivity(mIntent);
                    }
                } else {
                    Toast.makeText(mContext, "Detector initialisation failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            try {
                String code = "";
                if (img.equalsIgnoreCase("1")) {
                    imageUri = Uri.fromFile(new File(images.get(0).getPath()));
                    mBitmap = decodeBitmapUri(imageUri);
                } else if (images != null && images.get(0).getPath() != null) {
                    imageUri = Uri.fromFile(new File(images.get(0).getPath()));
                    mBitmap = decodeBitmapUri(imageUri);
                }

                if (detector.isOperational() && mBitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(mBitmap).build();
                    SparseArray<Barcode> barcodes = detector.detect(frame);
                    for (int index = 0; index < barcodes.size(); index++) {
                        Barcode barCode = barcodes.valueAt(index);
                        int type = barcodes.valueAt(index).valueFormat;
                        switch (type) {
                            case Barcode.CONTACT_INFO:
                                code = barCode.contactInfo.title;
                                Log.i("QRCODEAPP", barCode.contactInfo.title);
                                break;
                            case Barcode.EMAIL:
                            case Barcode.TEXT:
                                code = barCode.displayValue;
                                Log.i("QRCODEAPP", barCode.displayValue);
                                break;
                            case Barcode.PHONE:
                                code = barCode.phone.number;
                                Log.i("QRCODEAPP", barCode.phone.number);
                                break;
                            case Barcode.SMS:
                                code = barCode.sms.message;
                                Log.i("QRCODEAPP", barCode.sms.message);
                                break;
                            case Barcode.URL:
                                code = "url: " + barCode.displayValue;
                                Log.i("QRCODEAPP", "url: " + barCode.displayValue);
                                break;
                            case Barcode.WIFI:
                                code = barCode.wifi.ssid;
                                Log.i("QRCODEAPP", barCode.wifi.ssid);
                                break;
                            case Barcode.GEO:
                                code = barCode.geoPoint.lat + ":" + barCode.geoPoint.lng;
                                Log.i("QRCODEAPP", barCode.geoPoint.lat + ":" + barCode.geoPoint.lng);
                                break;
                            case Barcode.CALENDAR_EVENT:
                                code = barCode.calendarEvent.description;
                                Log.i("QRCODEAPP", barCode.calendarEvent.description);
                                break;
                            case Barcode.DRIVER_LICENSE:
                                code = barCode.driverLicense.licenseNumber;
                                Log.i("QRCODEAPP", barCode.driverLicense.licenseNumber);
                                break;
                            case Barcode.ISBN:
                            case Barcode.PRODUCT:
                            default:
                                code = barCode.rawValue;
                                Log.i("QRCODEAPP", barCode.rawValue);
                                break;
                        }
                    }
                    if (barcodes.size() == 0) {
                        Toast.makeText(mContext, "No barcode could be detected. Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent mIntent = new Intent(mContext, SchoolListActivity.class);
                        mIntent.putExtra(Intent.EXTRA_TEXT, code);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                    }
                } else {
                    Toast.makeText(mContext, "Detector initialisation failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap decodeBitmapUri(Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(mContext.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
}
