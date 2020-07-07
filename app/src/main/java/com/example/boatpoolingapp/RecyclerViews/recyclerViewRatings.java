package com.example.boatpoolingapp.RecyclerViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.model.notification;
import com.example.boatpoolingapp.model.reviews;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.parseFloat;

public class recyclerViewRatings extends RecyclerView.Adapter<recyclerViewRatings.ViewHolder> {
    public List<reviews> ratingList = new ArrayList<>();
    Context ctx;
    reviews obj;
    String role;
    public recyclerViewRatings(List< reviews > ratingList, Context ctx,String role){
        this.ctx = ctx;
        this.ratingList=ratingList;
        this.role=role;
    }

    @NonNull

    @Override
    public recyclerViewRatings.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_card, parent, false);

        return new recyclerViewRatings.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewRatings.ViewHolder holder, int position) {
        obj = ratingList.get(position);
        if(role.contains("driver")){
            holder.nametv.setText(obj.getRider_name());

        }
        else{
            holder.nametv.setText(obj.getDriver_name());
        }
     //   holder.nametv.setText(obj.getRider_name());
        holder.ratingbar.setRating(parseFloat(obj.getReview()));
        //holder.nametv.setText(obj.getRider_name());

    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nametv;
        RatingBar ratingbar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            pDialog = new ProgressDialog(ctx);
//            pDialog.setCancelable(false);
            nametv=itemView.findViewById(R.id.nametv);
            ratingbar=itemView.findViewById(R.id.ratingbar);

        }
    }

}
