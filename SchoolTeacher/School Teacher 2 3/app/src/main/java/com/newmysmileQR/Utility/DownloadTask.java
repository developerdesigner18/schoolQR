package com.newmysmileQR.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "DownloadTask";

    @Override
    protected Boolean doInBackground(String... urls) {
        String imageUrl = urls[0];

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Create a directory for the image
            String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/MyAppImages";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Save the image to the directory
            String fileName = "downloaded_image.jpg";
            File file = new File(folder, fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error downloading image: " + e.getMessage());
            return false;
        }
    }
}
