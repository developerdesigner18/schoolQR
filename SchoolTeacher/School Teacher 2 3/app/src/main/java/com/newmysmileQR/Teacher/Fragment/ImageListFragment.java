package com.newmysmileQR.Teacher.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.Image;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.ImageActivity;
import com.newmysmileQR.R;
import com.newmysmileQR.Teacher.Activity.MediaActivity;
import com.newmysmileQR.Utility.Preference;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.R)
public class ImageListFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE = 321;
    String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
    String[] PERMISSIONS1 = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private String img = "";
    private Uri imageUri;

    private MediaActivity mContext;
    private View mView;
    private FloatingActionButton fabAdd;
    private RecyclerView rvList;
    private LinearLayout llEmptyContent;

    private String code;
    private ArrayList<Image> mList = new ArrayList<>();
    private ImageAdapter mAdapter;
    private File mImageFile = null;
    private Bitmap bitmap;

    public ImageListFragment(String code, ArrayList<Image> mList) {
        this.mList = mList;
        this.code = code;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_image_list, container, false);
        initView();
        fabAdd.setOnClickListener(this);
        return mView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initView() {
        fabAdd = mView.findViewById(R.id.fabAdd);
        llEmptyContent = mView.findViewById(R.id.llEmptyContent);
        rvList = mView.findViewById(R.id.rvList);
        rvList.setLayoutManager(new GridLayoutManager(mContext, 2));
        mAdapter = new ImageAdapter(mList);
        mAdapter.notifyDataSetChanged();
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = (MediaActivity) context;
        super.onAttach(context);
    }

    @SuppressLint("NonConstantResourceId")
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
            builder.setTitle("Upload Image");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Camera")) {
                        img = "1";
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mImageFile = new File(Environment.getExternalStorageDirectory(), "fname_" + (System.currentTimeMillis()) + ".jpg");
                        Uri imageUri = FileProvider.getUriForFile(mContext, mContext.getPackageName(), mImageFile);
                        photoCaptureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(photoCaptureIntent, REQUEST_CODE);
                    } else if (options[item].equals("Gallery")) {
                        img = "2";
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.create().show();
        } else {
            ActivityCompat.requestPermissions(mContext, PERMISSIONS, 1);
            Toast.makeText(mContext, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                if (img.equalsIgnoreCase("1")) {
                    bitmap = BitmapFactory.decodeFile(mImageFile.getPath());
                    bitmap = Preference.getResizedBitmap(bitmap, 700);
                    ExifInterface ei = new ExifInterface(mImageFile.getPath());
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90: {
                            bitmap = TransformationUtils.rotateImage(bitmap, 90);
                            break;
                        }
                        case ExifInterface.ORIENTATION_ROTATE_180: {
                            bitmap = TransformationUtils.rotateImage(bitmap, 180);
                            break;
                        }
                        case ExifInterface.ORIENTATION_ROTATE_270: {
                            TransformationUtils.rotateImage(bitmap, 270);
                            break;
                        }
                    }
                } else if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUri);
                    Log.d(img + "Uri", imageUri.toString());
                }
                uploadImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage() {
        if (Preference.isNetworkAvailable(mContext)) {
            String school_code = Preference.getUser(mContext).getSchool().getCode();
            String teacher_id = Preference.getUser(mContext).getId() + "";
            String fileType = "0";


            TeacherHome.uploadFile(mContext, school_code, teacher_id, code, fileType, bitmap, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    imageUri = null;
                    mAdapter.addItem(response.getImage(), -1);
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
        private ArrayList<Image> mList;

        ImageAdapter(ArrayList<Image> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_image_list, parent, false);
            return new ViewHolder(view);
        }

        void addItem(Image mImage, int index) {
            if (index == -1)
                this.mList.add(mImage);
            else
                this.mList.add(index, mImage);
            notifyDataSetChanged();

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final Image mImage = this.mList.get(position);

            Glide.with(mContext)
                    .load(mImage.getUrl())
                    .error(R.drawable.ic_image_placeholder)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(holder.ivImage);

            final String title = mImage.getUrl().substring(mImage.getUrl().lastIndexOf("/") + 1);

            holder.tvTitle.setText(title);

            holder.mView.setOnClickListener(v -> {
                Intent mIntent = new Intent(mContext, ImageActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                mIntent.putExtra("url", mImage.getUrl());
                mIntent.putExtra("title", title);
                startActivity(mIntent);
            });

            holder.ibDownload.setOnClickListener(v -> {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("Download")
                        .setMessage("Are you sure want to download file?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Preference.DownloadFile(mContext).execute(mImage.getUrl());
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                alertDialog.show();
            });

            holder.ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setTitle("Delete")
                            .setMessage("Are you sure want to Delete file?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TeacherHome.deleteFile(mContext, mImage.getId() + "", new ResponseCallback() {
                                        @Override
                                        public void onResponse(RootModel response) {
                                            if (response.isSuccess()) {
                                                Toast.makeText(mContext, response.getMessage() + "", Toast.LENGTH_SHORT).show();
                                                mList.remove(position);
                                                notifyDataSetChanged();
                                                if (mList.isEmpty()) {
                                                    llEmptyContent.setVisibility(View.VISIBLE);
                                                    rvList.setVisibility(View.GONE);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            Log.d("ttt", t.getMessage());
                                        }
                                    });

                                }
                            })
                            .setNegativeButton("No", null)
                            .create();
                    dialog.show();

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
}
