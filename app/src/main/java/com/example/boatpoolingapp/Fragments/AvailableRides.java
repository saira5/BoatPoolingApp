package com.example.boatpoolingapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.RecyclerViews.recyclerviewAvailableRides;
import com.example.boatpoolingapp.Urls;
import com.example.boatpoolingapp.login;
import com.example.boatpoolingapp.model.availableRidesUtils;

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
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AvailableRides.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AvailableRides#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailableRides extends Fragment {

    RecyclerView recyclerView;
    recyclerviewAvailableRides adapter;
    LinearLayoutManager linearLayoutManager;
    public int availableSeats;
    String _id;
    String user_id;
    String dearture_city;
    String ride_type;
    String seats;
    String departure_time,Name;
    String city,dateTime;
    TextView cityTv;
    String name;
    String booking_status;
    ProgressDialog pDialog;

     public static List<availableRidesUtils> ridesList=new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AvailableRides() {

        // Required empty public constructor
    }


    public static AvailableRides newInstance() {
        AvailableRides fragment = new AvailableRides();

        return fragment;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ridesList.clear();
        recyclerView=view.findViewById(R.id.recyclerView);
        cityTv=view.findViewById(R.id.cityTv);
        city = getArguments().getString("cities");
        dateTime= getArguments().getString("date");
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        cityTv.setText(city);
        new  ridesCall().execute(city,dateTime);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ridesList.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_available_rides, container, false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class ridesCall extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
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
                HttpPost httpPost=new HttpPost(Urls.findRide);
                Log.d("params====",""+params[0]+params[1]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("User_id", login.id));
                nameValuePairs.add(new BasicNameValuePair("city", params[0]));
                nameValuePairs.add(new BasicNameValuePair("date",params[1]));
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



            try{

                JSONObject obj=new JSONObject(result);
                if(obj.getString("error").contains("false")){
                    JSONArray jsonArray=obj.getJSONArray("data");
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        _id=jsonObject.getString("_id");
                        user_id=jsonObject.getString("user_id");
                        dearture_city=jsonObject.getString("dearture_city");
                        seats=jsonObject.getString("seats");
                        departure_time=jsonObject.getString("departure_time");
                        Name=jsonObject.getString("name");

                       JSONArray jsonArray1= jsonObject.getJSONArray("reserved_users");
                        availableSeats=jsonArray1.length();
//                        for(int j =0;i<jsonArray1.length();j++){
//                            JSONObject jsonObject1=jsonArray1.getJSONObject(i);
//                            user_id=jsonObject1.getString("user_id");
//                            name=jsonObject1.getString("name");
//                            booking_status=jsonObject1.getString("booking_status");
//
//                        }
                        Log.d("result===2",""+"id"+_id+"user_id"+user_id+"dearture_city"+dearture_city+"seats"+seats+"departure_time"+departure_time+"Name"+Name);


                        availableRidesUtils a=new availableRidesUtils(_id,user_id,dearture_city,seats,departure_time,Name);
                        ridesList.add(a);
                    }
                   // Log.d("result===name",""+ridesList.get(0).getName());
                    linearLayoutManager = new LinearLayoutManager( getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter = new recyclerviewAvailableRides(ridesList, getContext());
                    recyclerView.setAdapter(adapter);
//                    Fragment nestedFragment = new CurrentRides();
//                    frameLayout.removeAllViews();
//                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                    transaction.add(R.id.frameLayout, nestedFragment).commit();
                    pdLoading.dismiss();
                    pDialog.dismiss();
                }
                else{
                    pdLoading.dismiss();

                    Toast.makeText(getContext(), "Unsuccessfull", Toast.LENGTH_LONG).show();
                }

                //JSONObject obj=new JSONObject(result);
//                if(result.toString().contains("Sign up successfull. Please check your email for verification code.")){
//
//                    Toast.makeText(SignUp.this, getString(R.string.toast_login_success), Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(SignUp.this, VerifyEmail.class);
//                    startActivity(i);
//
//                }else {
//                    Toast.makeText(SignUp.this, "Unsuccessfull", Toast.LENGTH_LONG).show();
//                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
