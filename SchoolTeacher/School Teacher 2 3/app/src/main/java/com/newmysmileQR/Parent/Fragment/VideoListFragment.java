package com.newmysmileQR.Parent.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.Video;
import com.newmysmileQR.Parent.Activity.MediaActivity;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Preference;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.R)
public class VideoListFragment extends Fragment implements View.OnClickListener {
    String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
    String[] PERMISSIONS1 = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private String img = "";
    private Uri imageUri;

    private MediaActivity mContext;
    private View mView;
    private RecyclerView rvList;
    private LinearLayout llEmptyContent;
    private ArrayList<Video> mList = new ArrayList<>();
    private String code;
    private ImageAdapter mAdapter;
    private FloatingActionButton fabAdd;

    public VideoListFragment(String code, ArrayList<Video> mList) {
        this.code = code;
        this.mList = mList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_video_list2, container, false);
        initView();
        fabAdd.setOnClickListener(this);
        return mView;
    }

    private void initView() {
        fabAdd = mView.findViewById(R.id.fabAdd);
        llEmptyContent = mView.findViewById(R.id.llEmptyContent);
        rvList = mView.findViewById(R.id.rvList);
        mAdapter = new ImageAdapter(mList);
        rvList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvList.setAdapter(mAdapter);
        if (mList.size() > 0) {
            llEmptyContent.setVisibility(View.GONE);
            rvList.setVisibility(View.VISIBLE);
        } else {
            llEmptyContent.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = (MediaActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAdd: {
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
        }
    }

    private void OpenCameraAndGallery() {
        if (Preference.hasPermissions(mContext, PERMISSIONS)) {
            final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Upload video");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Camera")) {
                        img = "1";
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Video.Media.TITLE, "Video" + System.currentTimeMillis());
                        values.put(MediaStore.Video.Media.DESCRIPTION, "Student video");
                        imageUri = mContext.getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(photoCaptureIntent, 20);
                    } else if (options[item].equals("Gallery")) {
                        img = "2";
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                        }
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select video"), 109);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            Toast.makeText(mContext, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(mContext, PERMISSIONS, 1);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (resultCode == RESULT_OK && img.equalsIgnoreCase("1")) {
                Log.d(img + " Uri : ", imageUri.toString());
            } else if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                Log.d(img + " Uri : ", imageUri.toString());
            }
            uploadVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadVideo() {
        if (Preference.isNetworkAvailable(mContext)) {
            String school_code = Preference.getUser(mContext).getSchool().getCode();
            String teacher_id = Preference.getUser(mContext).getId() + "";
            String fileType = "1";

            Log.d("final_data", "uploadVideo: " + "school_code: " +school_code +" teacher_id: "+ teacher_id + " fileType: "+fileType + imageUri);

            TeacherHome.uploadVideo(mContext, school_code, teacher_id, code, fileType, imageUri, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    mAdapter.addItem(response.getVideo(), -1);
                    if (mList.size() > 0) {
                        llEmptyContent.setVisibility(View.GONE);
                        rvList.setVisibility(View.VISIBLE);
                    } else {
                        llEmptyContent.setVisibility(View.VISIBLE);
                        rvList.setVisibility(View.GONE);
                    }
                    Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        private ArrayList<Video> mList;

        ImageAdapter(ArrayList<Video> mList) {
            this.mList = mList;
        }

        public void addItem(Video mVideo, int index) {
            if (index == -1) {
                this.mList.add(mVideo);
            } else {
                this.mList.add(index, mVideo);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_video_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Video mVideo = this.mList.get(position);
            Glide.with(mContext)
                    .load(mVideo.getUrl())
                    .error(R.drawable.ic_image_placeholder)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(holder.ivImage);

            final String title = mVideo.getUrl().substring(mVideo.getUrl().lastIndexOf("/") + 1);
            holder.tvTitle.setText(title);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(mVideo.getUrl()), "video/*");
                    startActivity(Intent.createChooser(intent, "Complete action using"));
                }
            });
            holder.ibDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                            .setTitle("Download")
                            .setMessage("Are you sure want to download file?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Preference.DownloadFile(mContext).execute(mVideo.getUrl());
                                }
                            })
                            .setNegativeButton("No", null)
                            .create();
                    alertDialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (this.mList != null && this.mList.size() > 0)
                return this.mList.size();
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View mView;
            ImageView ivImage;
            TextView tvTitle;
            ImageButton ibDownload;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                mView = itemView;
                ivImage = mView.findViewById(R.id.ivImage);
                tvTitle = mView.findViewById(R.id.tvTitle);
                ibDownload = mView.findViewById(R.id.ibDownload);
            }
        }
    }
}
