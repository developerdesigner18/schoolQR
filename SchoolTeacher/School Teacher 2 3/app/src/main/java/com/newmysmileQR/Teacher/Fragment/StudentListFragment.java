package com.newmysmileQR.Teacher.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.Student;
import com.newmysmileQR.APIModel.StudentExport;
import com.newmysmileQR.R;
import com.newmysmileQR.Teacher.Activity.QRDetailActivity;
import com.newmysmileQR.Teacher.Activity.SearchActivity;
import com.newmysmileQR.Utility.Preference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentListFragment extends BaseFragment implements View.OnClickListener, View.OnKeyListener {

    public boolean isNext = false;
    public int currentPage = 0;
    private String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Bitmap bitmap;
    private View mView;
    private LinearLayout llEmptyContent, llContent;
    private RecyclerView rvList;
    private SwipeRefreshLayout swipeView;
    private EditText etSearch;
    private ImageButton ibSearch;
    private StudentAdapter mAdapter;
    private FloatingActionButton fabImport;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_student_list, container, false);
        initView();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
            }
        });
        rvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
//                    onScrolledToTop();
                } else if (!recyclerView.canScrollVertically(1)) {
                    loadMoreData();
                } else if (dy < 0) {
//                    onScrolledUp();
                } else if (dy > 0) {
//                    onScrolledDown();
                }
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etSearch.setHint("");
                ibSearch.setImageResource(R.drawable.ic_nav_next);
                return false;
            }
        });
        etSearch.setOnKeyListener(this);
        etSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!etSearch.getText().toString().trim().equals("")) {
                        Intent mIntent = new Intent(mContext, SearchActivity.class);
                        mIntent.putExtra(Intent.EXTRA_TEXT, etSearch.getText().toString());
                        startActivity(mIntent);
                    }
                    return true;
                }
                return false;
            }
        });
        ibSearch.setOnClickListener(this);
        fabImport.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onResume() {
        if (etSearch != null && etSearch.isFocused()) {
            etSearch.clearFocus();
            etSearch.setText("");
            etSearch.setHint("Search");
            ibSearch.setImageResource(R.drawable.ic_nav_search);
        }
        super.onResume();
    }

    public void loadMoreData() {
        mAdapter.callLoadMore(rvList);
    }

    private void initView() {
        try {
            rvList = mView.findViewById(R.id.rvList);
            llEmptyContent = mView.findViewById(R.id.llEmptyContent);
            llContent = mView.findViewById(R.id.llContent);
            swipeView = mView.findViewById(R.id.swipeView);
            etSearch = mView.findViewById(R.id.etSearch);
            ibSearch = mView.findViewById(R.id.ibSearch);
            fabImport = mView.findViewById(R.id.fabImport);
            mAdapter = new StudentAdapter(new ArrayList<Student>());
            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (isNext) {
                        mAdapter.setLoading();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getStudentList();
                            }
                        }, 2000);
                    } else {
                        mAdapter.setLoaded();
                    }
                }
            });
            rvList.setLayoutManager(new GridLayoutManager(mContext, 1));
            rvList.setAdapter(mAdapter);
            swipeView.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getStudentList();
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStudentList() {
        if (Preference.isNetworkAvailable(mContext)) {
            String code = Preference.getUser(mContext).getSchool().getCode();
            String classId = Preference.getClassId(mContext) + "";
            if (classId != null && !classId.equals("-1")) {
                TeacherHome.studentList(mContext, code, classId, currentPage + "", new ResponseCallback() {
                    @Override
                    public void onResponse(RootModel response) {
                        swipeView.setRefreshing(false);
                        if (response.getStudentList() != null && response.getStudentList().size() > 0) {
                            if (response.getClassList().size() >= 10) {
                                isNext = true;
                                currentPage = currentPage + 1;
                            } else {
                                isNext = false;
                            }
                            mAdapter.setItems(response.getStudentList());
                            llContent.setVisibility(View.VISIBLE);
                            llEmptyContent.setVisibility(View.GONE);
                        } else {
                            llContent.setVisibility(View.GONE);
                            llEmptyContent.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        swipeView.setRefreshing(false);
                        llContent.setVisibility(View.GONE);
                        llEmptyContent.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(mContext, "You don't have any class assigned", Toast.LENGTH_SHORT).show();
                swipeView.setRefreshing(false);
                llContent.setVisibility(View.GONE);
                fabImport.setVisibility(View.GONE);
                llEmptyContent.setVisibility(View.VISIBLE);
                return;
            }

        } else {
            Toast.makeText(mContext, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibSearch: {
                if (!etSearch.getText().toString().trim().equals("")) {
                    Intent mIntent = new Intent(mContext, SearchActivity.class);
                    mIntent.putExtra(Intent.EXTRA_TEXT, etSearch.getText().toString());
                    startActivity(mIntent);
                } else {
                    etSearch.setHint("");
                    etSearch.requestFocus();
                    etSearch.setFocusable(true);
                    etSearch.setFocusableInTouchMode(true);
                    ibSearch.setImageResource(R.drawable.ic_nav_next);
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(etSearch.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                }
                break;
            }
            case R.id.fabImport: {
                if ((Preference.hasPermissions(mContext, PERMISSIONS))) {

                    AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                            .setTitle("Save")
                            .setMessage("Do you want to save all student QR code?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    exportStudent();
                                }
                            })
                            .setNegativeButton("No", null)
                            .create();
                    alertDialog.show();
                } else {
                    Toast.makeText(mContext, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(mContext, PERMISSIONS, 1);
                }
                break;
            }
        }
    }

    private void exportStudent() {
        if (Preference.isNetworkAvailable(mContext)) {
            String code = Preference.getUser(mContext).getSchool().getCode();
            String classId = Preference.getClassId(mContext) + "";
            if (classId != null) {
                TeacherHome.studentExport(mContext, code, classId, new ResponseCallback() {
                    @Override
                    public void onResponse(RootModel response) {
                        swipeView.setRefreshing(false);
                        if (response.getStudentList() != null && response.getStudentList().size() > 0) {
                            new export(response.getExportStudent()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        } else {
                            llContent.setVisibility(View.GONE);
                            llEmptyContent.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        swipeView.setRefreshing(false);
                        llContent.setVisibility(View.GONE);
                        llEmptyContent.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(mContext, "You don't have any class assigned", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(mContext, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveQRCode(String qrCode) {
        if (Preference.hasPermissions(mContext, PERMISSIONS)) {
            // Assume block needs to be inside a Try/Catch block.
            try {
                String path = Environment.getExternalStorageDirectory().toString() + "/My Smile QR";
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
                Preference.RefreshGallery(mContext, file);
                Log.d("ttt", file + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "This app requires access to your Storage and Camera. Please grant the Storage and Camera permission.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(mContext, PERMISSIONS, 1);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (etSearch.isFocused()) {
                etSearch.clearFocus();
                etSearch.setText("");
                ibSearch.setImageResource(R.drawable.ic_nav_search);
                return true;
            }
        }
        return false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    class export extends AsyncTask<Void, Void, Void> {

        ArrayList<StudentExport> exports;

        public export(ArrayList<StudentExport> exports) {
            this.exports = exports;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            exports = new ArrayList<>();
            Toast.makeText(mContext, "saved", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < exports.size(); i++) {
                String otp = exports.get(i).getICnumber() + "";
                QRGEncoder qrgEncoder = new QRGEncoder(otp, null, QRGContents.Type.TEXT, 1000);
                try {
                    bitmap = qrgEncoder.encodeAsBitmap();
                    bitmap = Preference.drawMultilineTextToBitmap(mContext, bitmap, exports.get(i).getFirstName() + " " + exports.get(i).getLastName());
                    saveQRCode(otp);
                } catch (Exception e) {
                    Log.v("HEKK", e.toString());
                }
            }
            return null;
        }
    }

    class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
        private final int VIEW_ITEM = 1;
        private final int VIEW_PROGRESS = 0;
        private ArrayList<Student> mList;
        private OnLoadMoreListener onLoadMoreListener;
        private boolean loading;

        StudentAdapter(ArrayList<Student> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == VIEW_ITEM) {
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_student_list, parent, false);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            }
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (getItemViewType(position) == VIEW_ITEM) {
                final Student student = mList.get(position);
                IImageLoader imageLoader = new PicassoLoader();
                imageLoader.loadImage(holder.avStudent, student.getImage(), student.getFirstName());
                String name = student.getFirstName() + " " + student.getLastName();
                holder.tvName.setText(name);
                holder.tvDate.setText(Preference.getDefaultTimeZone("yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss", student.getCreatedAt()));

                holder.vView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(mContext, QRDetailActivity.class);
                        mIntent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(student));
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(mIntent);
                    }
                });
            } else {
                holder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            if (mList != null && mList.size() > 0)
                return mList.size();
            return 0;
        }

        public void callLoadMore(RecyclerView recyclerView) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastPos = layoutManager.findLastVisibleItemPosition();
            if (!loading && lastPos == getItemCount() - 1 && onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore();
            }
            loading = true;
        }

        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return this.mList.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
        }

        public void setLoaded() {
            loading = false;
            for (int i = 0; i < getItemCount(); i++) {
                if (mList.get(i) == null) {
                    mList.remove(i);
                    notifyItemRemoved(i);
                }
            }
        }

        public void setLoading() {
            if (getItemCount() != 0) {
                this.mList.add(null);
                notifyItemInserted(getItemCount() - 1);
                loading = true;
            }
        }

        public void setItems(ArrayList<Student> dataList) {
            setLoaded();
            int positionStart = getItemCount();
            int itemCount = dataList.size();
            this.mList.addAll(dataList);
            notifyItemRangeInserted(positionStart, itemCount);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;
            AvatarView avStudent;
            TextView tvName;
            TextView tvDate;
            View vView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                vView = itemView;
                avStudent = vView.findViewById(R.id.avStudent);
                tvName = vView.findViewById(R.id.tvName);
                tvDate = vView.findViewById(R.id.tvDate);
                progressBar = vView.findViewById(R.id.progress_load_more);
            }
        }
    }
}
