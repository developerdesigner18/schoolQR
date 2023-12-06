package com.newmysmileQR.Parent.Adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.newmysmileQR.APIModel.Notification;
import com.newmysmileQR.ImageActivity;
import com.newmysmileQR.R;

import java.io.File;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Notification> list;

    public Adapter(Context mContext, ArrayList<Notification> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_notification_parent_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvDescription.setText(list.get(position).getDescription());
        holder.tvDate.setText(list.get(position).getCreatedAt());
        if (list.get(position).getQRstudentNotificationsent().getTeacherId() != null) {
            holder.tvType.setText("Teacher");
        } else {
            holder.tvType.setText("School");
        }
        if (list.get(position).getDownloadable().equals(1)) {
            holder.tvdownload.setVisibility(View.VISIBLE);
        } else {
            holder.tvdownload.setVisibility(View.GONE);
        }
        holder.tvdownload.setOnClickListener(v-> {
            downlod(list.get(position).getImage());
//            DownloadTask imageDownloader = new DownloadTask();
//            imageDownloader.execute(list.get(position).getImage());
        });
        if (list.get(position).getImage() != null) {
            holder.ivPhoto.setVisibility(View.VISIBLE);
            holder.ivPhoto.setEnabled(true);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imageUrl = list.get(position).getImage();
                        // It's an image URL
                        Intent intent = new Intent(mContext, ImageActivity.class);
                        intent.putExtra("url", imageUrl);
                        intent.putExtra("title", "Notification ");
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mContext.startActivity(intent);

                }
            });
        } else {
            holder.ivPhoto.setVisibility(View.INVISIBLE);
            holder.ivPhoto.setEnabled(false);
        }
    }
    private boolean isImageUrl(String url) {
        // Check if the URL points to an image (you can use a more robust method)
        return url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(".gif");
    }

    private boolean isPdfUrl(String url) {
        // Check if the URL points to a PDF file (you can use a more robust method)
        return url.endsWith(".pdf");
    }
    private void downlod(String ui) {
        Uri uri = Uri.parse(ui);

// Get the last segment of the URI, which is the filename
        String filename = uri.getLastPathSegment();
        Log.d("check_ends", "downlod: "+filename);
        Log.d("check_ends", "downlod: "+ui.endsWith(".jpg") );
//        String filename = ui.endsWith(".jpg");
        String downloadUrlOfImage = ui;
        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + "smile_dev" + "/");


        if (!direct.exists()) {
            direct.mkdir();
            Log.d("dev_download", "dir created for first time");
        }

        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(downloadUrlOfImage);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("*/*")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + "smile_dev" + File.separator + filename);

        dm.enqueue(request);
    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView tvTitle;
        TextView tvDate;
        TextView tvDescription;
        CardView tvdownload;
        TextView ivPhoto;
        TextView tvType;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvTitle = mView.findViewById(R.id.tvTitle);
            tvDate = mView.findViewById(R.id.tvDate);
            tvDescription = mView.findViewById(R.id.tvDescription);
            ivPhoto = mView.findViewById(R.id.ivPhoto);
            tvType = mView.findViewById(R.id.tvType);
            tvdownload = mView.findViewById(R.id.tvdownload);
        }
    }
}
