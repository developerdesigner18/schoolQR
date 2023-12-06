package com.newmysmileQR;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImageActivity extends AppCompatActivity {
    private ImageView ivImage;
    VideoView videoid;
    ProgressBar progressBar;
    PDFView idPDFView;
    RelativeLayout parentlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        String title = getIntent().getStringExtra("title");
        Toolbar mToolbar = findViewById(R.id.mToolbar);

        mToolbar.setTitle(title);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initView() {
        String url = getIntent().getStringExtra("url");

        Log.d("imageurl", "initView: " + url);

        videoid = findViewById(R.id.videoid);
        ivImage = findViewById(R.id.ivImage);
        parentlayout = findViewById(R.id.parentlayout);
        idPDFView = findViewById(R.id.idPDFView);
        progressBar = findViewById(R.id.progressBar);

        Log.d("devi100", "initView: " + isImageUrl(url));
        if (isImageUrl(url)) {

            parentlayout.setVisibility(View.GONE);
            videoid.setVisibility(View.GONE);
            ivImage.setVisibility(View.VISIBLE);
            idPDFView.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(url)) {
//                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                Glide.with(this)
                        .load(url)
                        .error(R.drawable.ic_image_thumbnail)
                        .placeholder(R.drawable.ic_image_thumbnail)
                        .into(ivImage);
            } else {
//                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                Glide.with(this)
                        .load(url)
                        .error(R.drawable.ic_image_thumbnail)
                        .placeholder(R.drawable.ic_image_thumbnail)
                        .into(ivImage);
            }
        } else if (isPdfUrl(url)) {
//            Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
            parentlayout.setVisibility(View.GONE);
            ivImage.setVisibility(View.GONE);
            videoid.setVisibility(View.GONE);

            new RetrievePDFfromUrl().execute(url);
        }
    }

    private boolean isImageUrl(String url) {
        return url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".gif");
    }

    private boolean isPdfUrl(String url) {
        return url.endsWith(".pdf");
    }

    @SuppressLint("StaticFieldLeak")
    class RetrievePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {

            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;

            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            parentlayout.setVisibility(View.GONE);
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            Log.d("inputdata_url_pdf", "onPostExecute: " + inputStream);
            idPDFView.fromStream(inputStream).load();
        }
    }
}