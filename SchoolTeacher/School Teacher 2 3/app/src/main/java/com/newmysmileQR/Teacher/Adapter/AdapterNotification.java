package com.newmysmileQR.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newmysmileQR.APIModel.Notification;
import com.newmysmileQR.ImageActivity;
import com.newmysmileQR.R;

import java.util.ArrayList;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {

    private Context mContext;
    private ArrayList<Notification> list;

    public AdapterNotification(Context mContext, ArrayList<Notification> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_notification_list, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvDescription.setText(list.get(position).getDescription());
        holder.tvDate.setText(list.get(position).getCreatedAt());
        if (list.get(position).getImage() != null) {
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvDescription;
        private ImageView ivPhoto;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvTitle = mView.findViewById(R.id.tvTitle);
            tvDate = mView.findViewById(R.id.tvDate);
            tvDescription = mView.findViewById(R.id.tvDescription);
            ivPhoto = mView.findViewById(R.id.ivPhoto);


        }
    }

}
