package com.newmysmileQR;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView mScannerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mScannerView = findViewById(R.id.scanner);
        progressBar = findViewById(R.id.progressbar);
        initToolbar();
        requestPermission();
    }

    public void startScanner() {
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setAutoFocus(true);
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        mToolbar.setTitle("Scan QR");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void handleResult(Result result) {
        Intent mIntent = new Intent();
        mIntent.putExtra(Intent.EXTRA_TEXT, result.getText());
        setResult(RESULT_OK, mIntent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mScannerView != null) {
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();          // Start camera on resume
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 108);
        } else {
            startScanner();
        }
    }

    public void showAppLocation(final Context context) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context)
                .setTitle("Enable camera:")
                .setMessage("Your camera will help us to scan.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        ((Activity) context).startActivityForResult(intent, 10);
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case 108: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startScanner();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showAppLocation(this);
                    } else {
                        Toast.makeText(this, "Allow ACCESS_CAMERA and EXTERNAL_STORAGE Permission from Setting", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}