package com.newmysmileQR.Utility;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.util.Strings;
import com.google.gson.Gson;
import com.newmysmileQR.APIModel.Permission;
import com.newmysmileQR.APIModel.User;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Preference {
    public static ProgressDialog progressDialog;

    private static void setInt(Context mContext, String key, int value) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Config.SETTING_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setString(Context mContext, String key, String value) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Config.SETTING_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context mContext, String key, String defaultValue) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Config.SETTING_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    private static int getInt(Context mContext, String key, int defaultValue) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Config.SETTING_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultValue);
    }

    public static void setClassId(Context mContext, int id) {
        setInt(mContext, Config.KEY_CLASS_ID, id);
    }

    public static int getClassId(Context mContext) {
        return getInt(mContext, Config.KEY_CLASS_ID, -1);
    }

    public static void setBoolean(Context mContext, String key, boolean value) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Config.SETTING_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context mContext, String key, boolean defaultValue) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Config.SETTING_PREFERENCE, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }


    public static boolean isNetworkAvailable(Context mContext) {
        return (((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()) != null
                && (((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()).isConnectedOrConnecting();
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, false);
    }

    public static String getStringImage(Bitmap bmp) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 70, baos);
            byte[] imageBytes = baos.toByteArray();
            //String ba1 = HttpRequest.Base64.encodeBytes(imageBytes);
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return "";
        }
    }

    public static void showKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static byte[] getBytesImage(Bitmap bmp) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return new byte[]{};
        }
    }

    public static void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200);
        view.startAnimation(anim);
    }

    public static ProgressDialog showProcess(Activity mContext, String title, String message) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(mContext);
        if (!message.equals(""))
            progressDialog.setMessage(message);
        if (!title.equals(""))
            progressDialog.setMessage(title);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return null;
    }

    public static void hideProcess() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void setLoginStatus(Context mContext, boolean status) {
        Preference.setBoolean(mContext, Config.KEY_LOGIN_STATUS, status);
    }

    public static boolean getLoginStatus(Context mContext, boolean defaultStatus) {
        return Preference.getBoolean(mContext, Config.KEY_LOGIN_STATUS, defaultStatus);
    }

    public static void setChangeStatus(Context mContext, boolean status) {
        Preference.setBoolean(mContext, Config.KEY_CHANGE_STATUS, status);
    }

    public static boolean getChangeStatus(Context mContext, boolean defaultStatus) {
        return Preference.getBoolean(mContext, Config.KEY_CHANGE_STATUS, defaultStatus);
    }

    public static void logout(Context mContext) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Config.SETTING_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Config.KEY_LOGIN_STATUS);
        editor.remove(Config.USER_KEY);
        editor.remove("FCM_TOKEN");
        editor.apply();
    }

    public static void resetPreference(Context mContext, String key) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(Config.SETTING_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static void rateAction(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

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

    public static String getFilePath(Context context, Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static void saveUser(Context mContext, User user) {
        try {
            String data = new Gson().toJson(user);
            setString(mContext, Config.USER_KEY, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getUser(Context mContext) {
        String data = getString(mContext, Config.USER_KEY, "");
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        return new Gson().fromJson(data, User.class);
    }

    public static boolean isValidMobile(CharSequence text, int length) { //Fixed length
        if (text == null) {
            return false;
        }
        return text.toString().length() == length;
    }

    public static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = "";
        if (Strings.isEmptyOrWhitespace(dateString)) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static String getRequiredFormat(String dateString) {
        return changeDateFormat("yyyy-MM-dd hh:mm", "dd/MM/yyyy hh:mm", dateString);
    }

    public static String getDefaultTimeZone(String currentFormat, String requiredFormat, String dateString) {
        String formattedDate = "";
        if (Strings.isEmptyOrWhitespace(dateString)) {
            return formattedDate;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dateString);
            df = new SimpleDateFormat(requiredFormat, Locale.ENGLISH);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getFileExtension(String absolutePath) {
        return absolutePath.substring(absolutePath.lastIndexOf("."));
    }

    public static void setPermission(Context mContext, ArrayList<Permission> permissions) {
        resetPermission(mContext);
        if (permissions != null && permissions.size() > 0) {
            for (Permission permission : permissions) {
                if (permission.getTeacher().getTitle().equalsIgnoreCase("notification")) {
                    setBoolean(mContext, "notification_permission", true);
                }
                if (permission.getTeacher().getTitle().equalsIgnoreCase("all")) {
                    setBoolean(mContext, "crud_permission", true);
                }
                if (permission.getTeacher().getTitle().equalsIgnoreCase("read")) {
                    setBoolean(mContext, "read_permission", true);
                }
            }
        }
    }

    private static void resetPermission(Context mContext) {
        resetPreference(mContext, "notification_permission");
        resetPreference(mContext, "crud_permission");
        resetPreference(mContext, "read_permission");
    }

    public static boolean isNotificationPermission(Context mContext) {
        return getBoolean(mContext, "notification_permission", false);
    }

    public static boolean isReadPermission(Context mContext) {
        return getBoolean(mContext, "read_permission", false);
    }

    public static boolean isCRUDPermission(Context mContext) {
        return getBoolean(mContext, "crud_permission", false);
    }

    public static void scanFile(Context mContext, File file) {
        String[] projection = {MediaStore.Images.Media._ID};

        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{file.getAbsolutePath()};

        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
            // File not found in media store DB
        }
        c.close();
    }

    public static void RefreshGallery(Context mContext, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file); // out is your output file
            mediaScanIntent.setData(contentUri);
            mContext.sendBroadcast(mediaScanIntent);
        } else {
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    public static void deleteFromGallery(Context mContext, File file) {
        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[]{Uri.fromFile(file).getPath()};
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c != null && c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
            c.close();
        }
    }

    public static Bitmap decodeBitmapUri(Context mContext, Uri uri) throws FileNotFoundException {
        int targetW = 1000;
        int targetH = 1000;
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

    public static Bitmap drawMultilineTextToBitmap(Context gContext, Bitmap bitmap, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(61, 61, 61));
        paint.setTextSize((int) (35 * scale));
        paint.setFlags(Paint.UNDERLINE_TEXT_FLAG);
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        int textWidth = canvas.getWidth() - (int) (16 * scale);

        StaticLayout textLayout = new StaticLayout(
                gText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        int textHeight = textLayout.getHeight();
        float x = (bitmap.getWidth() - textWidth) / 2;
        float y = (bitmap.getHeight() - textHeight) / 50;
        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bitmap;
    }

    public static String generateVerificationCode() {
        int randomPin = (int) (Math.random() * 9000) + 1000;
        String otp = String.valueOf(randomPin);
        return otp;
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    public static Locale getLocale(Context context) {
        Configuration config = context.getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }


    public static class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        private Context mContext;
        private String url;

        public DownloadFile(Context mContext) {
            this.mContext = mContext;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(mContext);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setProgressNumberFormat("");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lengthOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                fileName = getFileName(url.getPath());
                folder = Environment.getExternalStorageDirectory() + File.separator + "My Smile QR";
                File directory = new File(folder);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                File file = new File(directory, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream output = new FileOutputStream(file);
                byte[] data = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return "Downloaded at: " + folder + "/" + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return "Something went wrong";
        }

        protected void onProgressUpdate(String... progress) {
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String message) {
            this.progressDialog.dismiss();
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }
}
