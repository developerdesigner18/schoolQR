package com.newmysmileQR.Teacher.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.newmysmileQR.APIModel.Student;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Preference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvTitle, tvSubTitle, tvID, tvDate;
    AvatarView avStudent;
    ImageView ivQRCode;
    Button btnSave;
    Button btnTakePic;
    Bitmap bitmap;
    Student mStudent;
    String img = "";
    Bitmap mBitmap = null;
    private String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Uri imageUri;
    private BarcodeDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdetail);
        mStudent = new Gson().fromJson(getIntent().getStringExtra(Intent.EXTRA_TEXT), Student.class);
        initView();
        btnSave.setOnClickListener(this);
        btnTakePic.setOnClickListener(this);
    }

    private void initView() {
        avStudent = findViewById(R.id.avStudent);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubTitle);
        tvID = findViewById(R.id.tvID);
        tvDate = findViewById(R.id.tvDate);
        btnSave = findViewById(R.id.btnSave);
        btnTakePic = findViewById(R.id.btnTakePic);
        ivQRCode = findViewById(R.id.ivQRCode);

        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(avStudent, mStudent.getImage(), mStudent.getFirstName() + mStudent.getLastName());
        String otp = mStudent.getICnumber() + "";
        QRGEncoder qrgEncoder = new QRGEncoder(otp, null, QRGContents.Type.TEXT, 1000);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            bitmap = Preference.drawMultilineTextToBitmap(this, bitmap, mStudent.getFirstName() + " " + mStudent.getLastName());
            ivQRCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.v("HEKK", e.toString());
        }
        String name = mStudent.getFirstName() + " " + mStudent.getLastName();
        tvTitle.setText(name);
        tvSubTitle.setText(mStudent.getEmail());
        tvID.setText(String.format(Locale.ENGLISH, "Student ID: %d", mStudent.getId()));
        tvDate.setText(Preference.getRequiredFormat(mStudent.getCreatedAt()));

        detector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave: {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Save")
                        .setMessage("Do you want to save QR code?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveQRCode(mStudent.getId() + "");
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                alertDialog.show();
                break;
            }
            case R.id.btnTakePic: {
                Intent mIntent = new Intent(this, MediaActivity.class);
                mIntent.putExtra(Intent.EXTRA_TEXT, mStudent.getICnumber() + "");
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(mIntent);
                break;
            }
        }
    }

    private void saveQRCode(String qrCode) {
        if (Preference.hasPermissions(this, PERMISSIONS)) {
            // Assume block needs to be inside a Try/Catch block.
            try {
                String path = Environment.getExternalStorageDirectory().toString() + "/QR Code App";
                File root = new File(path);
                OutputStream fOut = null;
                if (!root.exists()) {
                    root.mkdirs();
                }
                File file = new File(root, qrCode + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                Preference.RefreshGallery(QRDetailActivity.this, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 109 && resultCode == RESULT_OK) {
            try {
                String code = "";
                if (img.equalsIgnoreCase("1")) {
                    mBitmap = Preference.decodeBitmapUri(this, imageUri);
                } else if (data != null && data.getData() != null) {
                    imageUri = data.getData();
                    mBitmap = Preference.decodeBitmapUri(this, imageUri);
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
                        Toast.makeText(this, "No barcode could be detected. Please try again.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent mIntent = new Intent(this, MediaActivity.class);
                        mIntent.putExtra(Intent.EXTRA_TEXT, code);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                    }
                } else {
                    Toast.makeText(this, "Detector initialisation failed", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void deleteQRCode() {
        if (Preference.hasPermissions(this, PERMISSIONS)) {
            String path = Environment.getExternalStorageDirectory().toString() + "/QR Code App";
            File root = new File(path);
            File file = new File(root, mStudent.getId() + ".jpg");
            if (file.exists()) {
                file.delete();
                Toast.makeText(QRDetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                Preference.RefreshGallery(QRDetailActivity.this, file);
            }
        } else {
            Toast.makeText(this, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }
}
