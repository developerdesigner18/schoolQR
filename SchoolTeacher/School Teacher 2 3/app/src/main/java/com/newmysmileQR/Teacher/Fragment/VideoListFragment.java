package com.newmysmileQR.Teacher.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ACTIVITY_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.Video;
import com.newmysmileQR.R;
import com.newmysmileQR.Teacher.Activity.MediaActivity;
import com.newmysmileQR.Utility.Preference;
import com.newmysmileQR.Utility.VideoCompressor.VideoCompress;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoListFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 100;
    String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
    String[] PERMISSIONS1 = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private String img = "";
    private Uri imageUri;
    private MediaActivity mContext;
    private View mView;
    private RecyclerView rvList;
    private LinearLayout llEmptyContent;
    private ArrayList<Video> mList;
    private String code;
    private ImageAdapter mAdapter;
    private FloatingActionButton fabAdd;
    private SwipeRefreshLayout swipeView;

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, you can access the video file here
            uploadVideo(imageUri);
        }
    }

    public VideoListFragment(String code, ArrayList<Video> mList) {
        this.code = code;
        this.mList = mList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_video_list, container, false);
        initView();
        fabAdd.setOnClickListener(this);
        Log.d("memoryInfo", getAvailableMemory() + "");

//        requestStoragePermission();

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
        return mView;
    }

    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    private void initView() {
        fabAdd = mView.findViewById(R.id.fabAdd);
        llEmptyContent = mView.findViewById(R.id.llEmptyContent);
        rvList = mView.findViewById(R.id.rvList);
        swipeView = mView.findViewById(R.id.swipeView);

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

        if (Preference.isCRUDPermission(mContext)) {
            fabAdd.setVisibility(View.VISIBLE);
        } else {
            fabAdd.setVisibility(View.GONE);
        }

        swipeView.setOnRefreshListener(() -> {
            rvList.setAdapter(new ImageAdapter(mList));
            swipeView.setRefreshing(false);
        });
        swipeView.setRefreshing(false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = (MediaActivity) context;
        super.onAttach(context);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAdd) {
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
        }
    }

    private void OpenCameraAndGallery() {
        if (Preference.hasPermissions(mContext, PERMISSIONS)) {
            final CharSequence[] options = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Upload video");
            builder.setItems(options, (dialog, item) -> {
                if (options[item].equals("Camera")) {
                    img = "1";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Video.Media.TITLE, "Video" + System.currentTimeMillis());
                    values.put(MediaStore.Video.Media.DESCRIPTION, "Student video");
                    imageUri = mContext.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                    Intent photoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    photoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);

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
            if (resultCode == RESULT_OK) {
                if (img.equalsIgnoreCase("1")) {
                    Log.d(img + " Uri : ", imageUri.toString());
                    File myDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "My Smile QR/CompressVideo");
                    if (!myDirectory.exists()) {
                        myDirectory.mkdirs();
                    }

                    String path = Preference.getFilePath(mContext, imageUri);
                    final String destPath = myDirectory.getAbsolutePath() + File.separator + "Compress" + new SimpleDateFormat("yyyyMMdd_HHmmss", Preference.getLocale(mContext)).format(new Date()) + ".mp4";

                    final ProgressBar progressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleSmall);
                    VideoCompress.compressVideoLow(path, destPath, new VideoCompress.CompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            File videoFile = new File(destPath);
                            imageUri = Uri.fromFile(videoFile);
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(mContext, "Fail to compress", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(float percent) {
                            progressBar.setMax(100);
                            progressBar.setProgress((int) percent);
                        }
                    });
                } else if (data != null && data.getData() != null) {
                    imageUri = data.getData();
                    Log.d("Final_data_video" + " Uri : ", imageUri.toString());
                }
                uploadVideo(imageUri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadVideo(Uri imageUri) {
        if (Preference.isNetworkAvailable(mContext)) {
            String school_code = Preference.getUser(mContext).getSchool().getCode();
            String teacher_id = Preference.getUser(mContext).getId() + "";
            String fileType = "1";
            Log.d("final_data1223", "uploadVideo: " + "school_code: " + school_code + " teacher_id: " + teacher_id + " fileType: " + fileType + imageUri);
            TeacherHome.uploadVideo(mContext, school_code, teacher_id, code, fileType, imageUri, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    mAdapter.addItem(response.getVideo(), -1);
                    if (mList.size() > 0) {
                        llEmptyContent.setVisibility(View.GONE);
                        rvList.setVisibility(View.VISIBLE);
                        rvList.setAdapter(mAdapter);
                    } else {
                        llEmptyContent.setVisibility(View.VISIBLE);
                        rvList.setVisibility(View.GONE);
                    }
                    Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Throwable t) {
//                    rvList.setVisibility(View.GONE);
//                    llEmptyContent.setVisibility(View.VISIBLE);
                    Log.d("throw_getMe : ", String.valueOf(t.getCause()));
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

        @SuppressLint("NotifyDataSetChanged")
        public void addItem(Video mVideo, int index) {
            if (index == -1) {
                this.mList.add(mVideo);
            } else {
                this.mList.add(index, mVideo);
                notifyDataSetChanged();
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_video_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final Video mVideo = this.mList.get(position);
            Glide.with(mContext)
                    .load(mVideo.getUrl())
                    .error(R.drawable.ic_image_placeholder)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(holder.ivImage);

            final String title = mVideo.getUrl().substring(mVideo.getUrl().lastIndexOf("/") + 1);
            holder.tvTitle.setText(title);

            holder.mView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(mVideo.getUrl()), "video/*");
                startActivity(Intent.createChooser(intent, "Complete action using"));
            });

            holder.ibDownload.setOnClickListener(v -> {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("Download")
                        .setMessage("Are you sure want to download file?")
                        .setPositiveButton("Yes", (dialog, which) -> new Preference.DownloadFile(mContext).execute(mVideo.getUrl()))
                        .setNegativeButton("No", null)
                        .create();
                alertDialog.show();
            });

            holder.ibDelete.setOnClickListener(v -> {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to delete file?")
                        .setPositiveButton("Yes", (dialog1, which) -> TeacherHome.deleteFile(mContext, mVideo.getId() + "", new ResponseCallback() {
                            @Override
                            public void onResponse(RootModel response) {
                                if (response.isSuccess()) {
                                    Toast.makeText(mContext, response.getMessage() + "", Toast.LENGTH_SHORT).show();
                                    mList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d("ttt", t.getMessage());
                            }
                        }))
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
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
            ImageButton ibDelete;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                mView = itemView;
                ivImage = mView.findViewById(R.id.ivImage);
                tvTitle = mView.findViewById(R.id.tvTitle);
                ibDownload = mView.findViewById(R.id.ibDownload);
                ibDelete = mView.findViewById(R.id.ibDelete);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can access the video file here
                uploadVideo(imageUri);
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }
}
