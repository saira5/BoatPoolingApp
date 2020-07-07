package com.example.boatpoolingapp.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.model.availableRidesUtils;
import com.example.boatpoolingapp.model.notification;

import java.util.ArrayList;
import java.util.List;

public class recyclerviewNotification  extends RecyclerView.Adapter<recyclerviewNotification.ViewHolder> {
    public List<notification> notificationList = new ArrayList<>();
    Context ctx;
    notification obj;

 public recyclerviewNotification(List< notification > notificationList, Context ctx) {
           this.ctx = ctx;
           this.notificationList=notificationList;

       }


    @NonNull
    @Override
    public recyclerviewNotification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerviewNotification.ViewHolder holder, int position) {
        obj = notificationList.get(position);
        holder.descriptiontv.setText(obj.getDescription());
        holder.timetv.setText(obj.getTime());

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descriptiontv,timetv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptiontv=itemView.findViewById(R.id.descriptiontv);
            timetv=itemView.findViewById(R.id.timetv);


        }
    }

}
