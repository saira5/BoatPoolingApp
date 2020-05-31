package com.example.boatpoolingapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boatpoolingapp.Dashboard;
import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.RecyclerViews.recyclerViewReservedRides;
import com.example.boatpoolingapp.Urls;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentRides.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentRides#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentRides extends Fragment {
    public static String dateTime,destination,seats;
    public static String rides_id;
    String _id;
    Button completeBtn;
    String user_id;
    String dearture_city;
    String ride_type;

    String departure_time;
    String name;
    String booking_status;
    List<reservedUserUtils> reservedList=new ArrayList<>();
    RecyclerView recyclerView;
    TextView dateTv,cityTv,seatsNumber;
   public static String seatsText;
    recyclerViewReservedRides adapter;
    LinearLayoutManager linearLayoutManager;
    ProgressDialog pDialog;
    public Date date = null;
    public SimpleDateFormat inputFormat,outputFormat;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CurrentRides() {
        // Required empty public constructor
    }

    public static CurrentRides newInstance() {
        CurrentRides fragment = new CurrentRides();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reservedList.clear();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        completeBtn=view.findViewById(R.id.completeBtn);
        dateTv=view.findViewById(R.id.userNametv);
        cityTv=view.findViewById(R.id.cityTv);
        seatsNumber=view.findViewById(R.id.seatsNumber);
        recyclerView=view.findViewById(R.id.recyclerView);
        rides_id = getArguments().getString("rides_id");

        if(rides_id==null){
          dateTime = getArguments().getString("dateTime");
            destination = getArguments().getString("destination");
            seats = getArguments().getString("seatsOffered");
            seatsNumber.setText("Seats available :"+seats );
            Log.d("result===current rides", "onViewCreated: "+dateTime+destination);

            dateTv.setText(dateTime);
            cityTv.setText(destination);
            completeBtn.setVisibility(view.INVISIBLE);

        }
        else{
            inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            try {
                date = inputFormat.parse(getArguments().getString("dateTime"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = outputFormat.format(date);
            dateTime=formattedDate;

          //  dateTime = getArguments().getString("dateTime");
            destination = getArguments().getString("destination");
            dateTv.setText(dateTime);
            cityTv.setText(destination);
            seats = getArguments().getString("seats");

            completeBtn.setVisibility(view.VISIBLE);

            new offerRideCall().execute(rides_id);
        }


        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              new completeRideCall().execute(rides_id);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_current_rides, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class offerRideCall extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.getting_address));

            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.findRiders);
                Log.d("params====",""+params[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("ride_id", params[0]));
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
                           JSONObject jsonObject=obj.getJSONObject("data");
//                           _id=jsonObject.getString("_id");
//                           dearture_city=jsonObject.getString("dearture_city");
//                           seats=jsonObject.getString("seats");
//                           departure_time=jsonObject.getString("departure_time");

                           JSONArray jsonArray1= jsonObject.getJSONArray("reserved_users");
                           if(seats!=null){
                               seatsNumber.setText("Seats available :"+seats);
                           }

//                           if(seats!=null){
//                               Log.d("result===length", "onPostExecute: "+jsonArray1.length()+"seats"+seats);
//                               int seatsNum=Integer.parseInt(seats)-jsonArray1.length();
//                               if(seatsNum<0){
//                                   seatsNum=0;
//                                   seatsNumber.setText("Seats available :"+seats);
//
//                               }
//                               else{
//                                   seatsNumber.setText("Seats available :"+seats);
//
//                               }
//                           }

                           for(int i =0;i<jsonArray1.length();i++){
                                JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                                user_id=jsonObject1.getString("user_id");
                                name=jsonObject1.getString("name");
                                booking_status=jsonObject1.getString("booking_status");
                               reservedUserUtils r=new reservedUserUtils(user_id,name,booking_status);
                               reservedList.add(r);
                            }


                           linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                           recyclerView.setLayoutManager(linearLayoutManager);
                           adapter = new recyclerViewReservedRides(reservedList, getContext(),rides_id);
                           recyclerView.setAdapter(adapter);

                       }
                    pdLoading.dismiss();
                    pDialog.dismiss();
             }

                else{
                    pDialog.dismiss();

                    pdLoading.dismiss();

                    Toast.makeText(getContext(), "Unsuccessfull", Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){
                pDialog.dismiss();

             //   Toast.makeText(getContext(), "Server Error", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }
        }



    private class completeRideCall extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.getting_address));

            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.endRide);
                Log.d("params====",""+params[0]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("ride_id", params[0]));
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
                    if(obj.getString("error").contains("false")&&obj.getString("message").contains("success")){
                        Intent intent = new Intent(getActivity(), Dashboard.class);
                        startActivity(intent);

                    }
                    pdLoading.dismiss();
                    pDialog.dismiss();
                }

                else{
                    pDialog.dismiss();

                    pdLoading.dismiss();

                    Toast.makeText(getContext(), "Unsuccessfull", Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){
                pDialog.dismiss();

                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }
    }
}
