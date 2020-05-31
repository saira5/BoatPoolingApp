package com.example.boatpoolingapp.RecyclerViews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boatpoolingapp.Dashboard;
import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.Urls;
import com.example.boatpoolingapp.login;
import com.example.boatpoolingapp.model.reservedUserUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class recyclerViewReservedRides extends RecyclerView.Adapter<recyclerViewReservedRides.ViewHolder>{
    reservedUserUtils obj;
    Boolean activate=true;
    public List<reservedUserUtils> ridesList = new ArrayList<>();
    Context ctx;
    ProgressDialog pDialog;
String rides_id;
    public recyclerViewReservedRides(List<reservedUserUtils> ridesList, Context ctx,String rides_id) {
        this.ctx = ctx;
        this.ridesList=ridesList;
        this.rides_id=rides_id;

    }

    @NonNull
    @Override
    public recyclerViewReservedRides.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.riding_card, parent, false);

        return new recyclerViewReservedRides.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final recyclerViewReservedRides.ViewHolder holder, int position) {

//        if (activate) {
//            holder.approveBtn.setVisibility(View.VISIBLE);
//        } else {
//            holder.approveBtn.setVisibility(View.INVISIBLE);
//        }

        obj = ridesList.get(position);
        // Log.d("BINDVIEWHOLDERDATA", "onBindViewHolder: " + obj.ge());

        holder.userNametv.setText(obj.getName());
        if(!(ridesList.get(position).getBooking_status().contains("pending"))){
            holder.approveBtn.setVisibility(View.INVISIBLE);
            holder.rejectBtn.setVisibility(View.INVISIBLE);

        }


               holder.approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activateButtons(true);
                holder.approveBtn.setVisibility(View.INVISIBLE);
                holder.rejectBtn.setVisibility(View.INVISIBLE);

                new updateRideCall().execute(rides_id,obj.getUser_id(),"yes");
            }
        });
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activateButtons(true);
                holder.approveBtn.setVisibility(View.INVISIBLE);
                holder.rejectBtn.setVisibility(View.INVISIBLE);

                new updateRideCall().execute(rides_id,obj.getUser_id(),"no");
            }
        });


    }
    public void activateButtons(boolean activate) {
        this.activate = activate;
        notifyDataSetChanged(); //need to call it for the child views to be re-created with buttons.
    }
    @Override
    public int getItemCount() {
        return ridesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNametv,datetv;

        ImageView approveBtn,rejectBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pDialog = new ProgressDialog(ctx);
            pDialog.setCancelable(false);
            userNametv=itemView.findViewById(R.id.userNametv);
            rejectBtn=itemView.findViewById(R.id.rejectBtn);
            approveBtn=itemView.findViewById(R.id.approveBtn);


        }
    }

    private class updateRideCall extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(ctx);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("loading");

            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{


                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.updatePendingRide);
                Log.d("params====",""+params[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("ride_id", params[0]));
                nameValuePairs.add(new BasicNameValuePair("user_id",params[1] ));
                nameValuePairs.add(new BasicNameValuePair("is_reservation",params[2]));
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
            Log.d("result===complet",""+result);
            pdLoading.dismiss();


            pDialog.dismiss();
            try{

                JSONObject obj=new JSONObject(result);
                if(obj!=null){
                    if(obj.getString("error").contains("false")){
                        Toast.makeText(ctx, obj.getString("message"), Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getActivity(), Dashboard.class);
//                        startActivity(intent);

                    }
                    pdLoading.dismiss();
                    pDialog.dismiss();
                }

                else{
                    pDialog.dismiss();

                    pdLoading.dismiss();

                    Toast.makeText(ctx, "Unsuccessfull", Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){
                pDialog.dismiss();

                Toast.makeText(ctx, "Server Error", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }
    }
}



