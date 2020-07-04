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
import android.widget.Toast;

import com.example.boatpoolingapp.Dashboard;
import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.RecyclerViews.recyclerViewRatings;
import com.example.boatpoolingapp.RecyclerViews.recyclerViewReservedRides;
import com.example.boatpoolingapp.RecyclerViews.recyclerviewNotification;
import com.example.boatpoolingapp.Urls;
import com.example.boatpoolingapp.login;
import com.example.boatpoolingapp.model.notification;
import com.example.boatpoolingapp.model.reservedUserUtils;
import com.example.boatpoolingapp.model.reviews;

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
 * {@link notificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link notificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<notification> notificationList=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    recyclerviewNotification adapter;
    ProgressDialog pDialog;

    RecyclerView recyclerView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public notificationFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static notificationFragment newInstance() {
        notificationFragment fragment = new notificationFragment();
        return fragment;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        new findNotificationsCall().execute();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
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
    private class findNotificationsCall extends AsyncTask<String, String, String>
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
                HttpPost httpPost=new HttpPost(Urls.findNotification);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("User_id", login.id));
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
            Log.d("result===notify",""+result);
            pdLoading.dismiss();


            pDialog.dismiss();
            try{
                JSONObject obj=new JSONObject(result);
                if(obj.getString("error").contains("false")){

                    JSONArray jsonArray=obj.getJSONArray("data");
                 //   for(int i =0 ;i<jsonArray.length();i++)
                        for(int i =jsonArray.length()-1 ;i>=0;i--)
                        {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        notification reviewsObj=new notification(jsonObject.getString("User_id"),jsonObject.getString("description"),jsonObject.getString("_id"),jsonObject.getString("time"));
                        notificationList.add(reviewsObj);
                    }


                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter = new recyclerviewNotification(notificationList, getContext());
                    recyclerView.setAdapter(adapter);


                    pdLoading.dismiss();
                    pDialog.dismiss();
                }


                else{
                    pDialog.dismiss();

                    pdLoading.dismiss();

                    Toast.makeText(getContext(), "No Notifications Found", Toast.LENGTH_LONG).show();
                }

            }catch (Exception e){
                pDialog.dismiss();

                Toast.makeText(getContext(), "Server Error "+e, Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }
    }
}
