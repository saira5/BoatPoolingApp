package com.example.boatpoolingapp.RecyclerViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boatpoolingapp.Dashboard;
import com.example.boatpoolingapp.Fragments.CurrentRides;
import com.example.boatpoolingapp.Fragments.bookedRide;
import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.Urls;
import com.example.boatpoolingapp.login;
import com.example.boatpoolingapp.model.availableRidesUtils;
import com.example.boatpoolingapp.model.reservedUserUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class recyclerviewAvailableRides extends RecyclerView.Adapter<recyclerviewAvailableRides.ViewHolder> {
    availableRidesUtils obj;
    public List<availableRidesUtils> ridesList = new ArrayList<>();
    Context ctx;
    ProgressDialog pDialog;
    public Date date = null;
    public SimpleDateFormat inputFormat,outputFormat;
    public FragmentManager f_manager;

    public recyclerviewAvailableRides(List<availableRidesUtils> ridesList, Context ctx) {
      this.ctx = ctx;
      this.ridesList=ridesList;

  }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        obj = ridesList.get(position);
      Log.d("result===recycler", "onBindViewHolder: " + obj.getName()+""+obj.getDeparture_time());

       holder.userNametv.setText(obj.getName());
        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            date = inputFormat.parse(obj.getDeparture_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);

        holder.datetv.setText(formattedDate);

        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              new bookCall().execute(obj.get_id());
//                         AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                        Fragment myFragment = new bookedRide();
//                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).addToBackStack(null).commit();



          }
      });
    }

    @Override
    public int getItemCount() {
        return ridesList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      TextView userNametv,datetv;
      Button bookBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pDialog = new ProgressDialog(ctx);
            pDialog.setCancelable(false);
            userNametv=itemView.findViewById(R.id.userNametv);
            datetv=itemView.findViewById(R.id.datetv);
            bookBtn=itemView.findViewById(R.id.bookBtn);
        }
    }

    private class  bookCall extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(ctx);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Loading");

            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.bookRide);
                Log.d("params====",""+params[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("ride_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("user_id", login.id));
                nameValuePairs.add(new BasicNameValuePair("name",login.name));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse=httpClient.execute(httpPost);
                return_text=readResponse(httpResponse);
                Log.d("RESPONSE",""+return_text);

            }catch(Exception e){
                e.printStackTrace();
            }
            return return_text;
        }
        public String readResponse(HttpResponse res) {
            InputStream is=null;
            String return_text="";
            try {
                is=res.getEntity().getContent();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String line="";
                StringBuffer sb=new StringBuffer();
                while ((line=bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                }
                return_text=sb.toString();
            } catch (Exception e)
            {
            }
            return return_text;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("result===",""+result);
             pdLoading.dismiss();


            pDialog.dismiss();
            try{

                JSONObject obj=new JSONObject(result);
                if(obj!=null){
                    if(obj.getString("error").contains("false")){
                        Toast.makeText(ctx, obj.getString("message").toString(), Toast.LENGTH_LONG).show();
                        Intent i =new Intent(ctx, Dashboard.class);
                        ctx.startActivity(i);
//                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                        Fragment myFragment = new CurrentRides();
//                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).addToBackStack(null).commit();


//                        Fragment fr = new CurrentRides();
//                        FragmentTransaction transaction =  f_manager.beginTransaction();
//                        transaction.add(R.id.frameLayout, fr).commit();
//                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction().addToBackStack("offerride");
//                        Bundle args = new Bundle();
//                        args.putString("name",name);
//                        args.putString("departure_time",departure_time);
//                        args.putString("dearture_city",dearture_city);

                       // fr.setArguments(args);

                    }
                    else if(obj.getString("error").contains("true")){
                        Toast.makeText(ctx, obj.getString("message").toString(), Toast.LENGTH_LONG).show();

                    }
                }

                else{
                    Toast.makeText(ctx, "Unsuccessfull", Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){
                Toast.makeText(ctx, "Server Error", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }
    }
}


