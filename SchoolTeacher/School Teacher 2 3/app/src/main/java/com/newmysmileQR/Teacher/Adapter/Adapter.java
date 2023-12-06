package com.newmysmileQR.Teacher.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newmysmileQR.APIModel.StudentByNotification;
import com.newmysmileQR.ImageActivity;
import com.newmysmileQR.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<StudentByNotification> list;

    public Adapter(Context mContext, ArrayList<StudentByNotification> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_notification_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvDescription.setText(list.get(position).getDescription());
        holder.tvDate.setText(list.get(position).getCreatedAt());
        holder.tvClass.setText(list.get(position).getStandards().getTitle());

        if (list.get(position).getImage() != null) {
            holder.ivPhoto.setVisibility(View.VISIBLE);
            holder.ivPhoto.setEnabled(true);
            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImageActivity.class);
                    intent.putExtra("url", list.get(position).getImage());
                    intent.putExtra("title", "Document");
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    mContext.startActivity(intent);

                }
            });
        } else {
            holder.ivPhoto.setVisibility(View.INVISIBLE);
            Log.d("checkkk_ollss", "onBindViewHolder: "+list.get(position).getTitle());
            holder.ivPhoto.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView tvTitle;
        TextView tvDate;
        TextView tvDescription;
        TextView tvClass;
        ImageView ivPhoto;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvTitle = mView.findViewById(R.id.tvTitle);
            tvDate = mView.findViewById(R.id.tvDate);
            tvDescription = mView.findViewById(R.id.tvDescription);
            tvClass = mView.findViewById(R.id.tvClass);
            ivPhoto = mView.findViewById(R.id.ivPhoto);
        }
    }
}
