package com.newmysmileQR.Teacher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.newmysmileQR.APICall.TeacherHome;
import com.newmysmileQR.APIManager.ResponseCallback;
import com.newmysmileQR.APIModel.RootModel;
import com.newmysmileQR.APIModel.Student;
import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Preference;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class SearchActivity extends AppCompatActivity {

    private String query;
    private LinearLayout llEmptyContent, llContent;
    private RecyclerView rvList;
    private SwipeRefreshLayout swipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        query = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        initToolbar();
        initView();
    }

    private void initView() {
        rvList = findViewById(R.id.rvList);
        llEmptyContent = findViewById(R.id.llEmptyContent);
        llContent = findViewById(R.id.llContent);
        swipeView = findViewById(R.id.swipeView);
        rvList.setLayoutManager(new GridLayoutManager(this, 1));
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(false);
                getStudent();
            }
        });
        swipeView.setRefreshing(false);
        getStudent();
    }

    private void getStudent() {
        if (Preference.isNetworkAvailable(this)) {
            String standardUId = Preference.getClassId(this) + "";
            TeacherHome.searchStudent(this, query, standardUId, new ResponseCallback() {
                @Override
                public void onResponse(RootModel response) {
                    if (response.getStudentList() != null && response.getStudentList().size() > 0) {
                        rvList.setAdapter(new StudentAdapter(response.getStudentList()));
                        llContent.setVisibility(View.VISIBLE);
                        llEmptyContent.setVisibility(View.GONE);
                    } else {
                        llContent.setVisibility(View.GONE);
                        llEmptyContent.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    llContent.setVisibility(View.GONE);
                    llEmptyContent.setVisibility(View.VISIBLE);
                    Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void initToolbar() {
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        mToolbar.setTitle("Search result for '" + query + "'");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Preference.hideKeyboard(this);
        super.onBackPressed();
    }

    class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
        private ArrayList<Student> mList;

        StudentAdapter(ArrayList<Student> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.layout_student_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Student student = mList.get(position);
            IImageLoader imageLoader = new PicassoLoader();
            String name = student.getFirstName() +" "+ student.getLastName();
            imageLoader.loadImage(holder.avStudent, student.getImage(), name);
            holder.tvName.setText(name);
            holder.tvDate.setText(Preference.getDefaultTimeZone("yyyy-MM-dd hh:mm", "dd/MM/yyyy hh:mm",student.getCreatedAt()));
            holder.vView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(SearchActivity.this, QRDetailActivity.class);
                    mIntent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(student));
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(mIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mList != null && mList.size() > 0)
                return mList.size();
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
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
            }
        }
    }
}
