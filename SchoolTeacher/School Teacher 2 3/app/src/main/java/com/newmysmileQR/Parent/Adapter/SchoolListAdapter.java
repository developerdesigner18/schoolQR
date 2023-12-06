package com.newmysmileQR.Parent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.newmysmileQR.APIModel.SchoolList;
import com.newmysmileQR.Parent.Activity.MediaActivity;
import com.newmysmileQR.R;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.ViewHolder> {
    private Context mContext;
    private String code;
    private ArrayList<SchoolList> list;

    public SchoolListAdapter(Context mContext, String code, ArrayList<SchoolList> list) {
        this.mContext = mContext;
        this.code = code;
        this.list = list;
    }

    @NonNull
    @Override
    public SchoolListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_school_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolListAdapter.ViewHolder holder, int position) {
        IImageLoader imageLoader = new PicassoLoader();
        if (list.get(position).getSchoolName() != null) {
            imageLoader.loadImage(holder.avSchool, "dhfkvjad", list.get(position).getSchoolName().getName() + "");
            holder.tvName.setText(list.get(position).getSchoolName().getName());
        }
        final SchoolList schoolList = list.get(position);
        holder.cvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MediaActivity.class);
                intent.putExtra(Intent.EXTRA_TITLE, new Gson().toJson(schoolList));
                intent.putExtra(Intent.EXTRA_TEXT, code);
                mContext.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private CardView cvName;
        private AvatarView avSchool;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            avSchool = itemView.findViewById(R.id.avSchool);
            cvName = itemView.findViewById(R.id.cvName);
        }
    }

}
