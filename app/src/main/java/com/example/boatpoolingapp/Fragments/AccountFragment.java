package com.example.boatpoolingapp.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText etmobileNumber,etdriverID,etbio,etEmergencyMobileNumber,etfirstName,etlasttName,etCity;
    String mobileNumber,driverID,bio,EmergencyMobileNumber,firstName,lasttName,tCity;
    Button savebtn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog pDialog;

    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etmobileNumber =view.findViewById(R.id.etmobileNumber);
        etdriverID = view.findViewById(R.id.etdriverID);
        savebtn = view.findViewById(R.id.savebtn);
        etbio = view.findViewById(R.id.etbio);
        etfirstName = view.findViewById(R.id.etfirstName);
        etlasttName = view.findViewById(R.id.etlasttName);
        etCity = view.findViewById(R.id.etCity);
        etEmergencyMobileNumber = view.findViewById(R.id.etEmergencyMobileNumber);

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);


        if(login.firstName!=null){
            etfirstName.setText(login.firstName);

        }
        if(login.lastName!=null){
            etlasttName.setText(login.lastName);

        }

        if(login.city!=null){
            etCity.setText(login.city);

        }

        if(login.mobile!=null){
            etmobileNumber.setText(login.mobile);

        }


        if(login.emergencyMobile!=null){
            etEmergencyMobileNumber.setText(login.emergencyMobile);

        }


        if(login.driverID!=null){
            etdriverID.setText(login.driverID);

        }
        if(login.bio!=null){
            etbio.setText(login.bio);

        }

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation()){
                    new detailsCall().execute(etbio.getText().toString(),etmobileNumber.getText().toString(),etEmergencyMobileNumber.getText().toString(),etdriverID.getText().toString(),etCity.getText().toString(),etfirstName.getText().toString(),etlasttName.getText().toString());
                }
            }
        });

    }

    public boolean validation(){

        bio=etbio.getText().toString();
        mobileNumber=etmobileNumber.getText().toString();
        driverID=etdriverID.getText().toString();
        EmergencyMobileNumber=etEmergencyMobileNumber.getText().toString();
        tCity= etCity.getText().toString();
        firstName=etfirstName.getText().toString();
        lasttName=etlasttName.getText().toString();


        boolean validate=true;

        if(mobileNumber.isEmpty())
        {
            etmobileNumber.setError(getString(R.string.empty));
            validate= false;
        }
        if(driverID.isEmpty())
        {
            etdriverID.setError(getString(R.string.empty));
            validate= false;
        }
        if(EmergencyMobileNumber.isEmpty())
        {
            etEmergencyMobileNumber.setError(getString(R.string.empty));
            validate= false;
        }

        if(tCity.isEmpty())
        {
            etCity.setError(getString(R.string.empty));
            validate= false;
        }

        if(firstName.isEmpty())
        {
            etfirstName.setError(getString(R.string.empty));
            validate= false;
        }
        if(lasttName.isEmpty())
        {
            etlasttName.setError(getString(R.string.empty));
            validate= false;
        }

        return validate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
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
    private class detailsCall extends AsyncTask<String, String, String>
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
                HttpPost httpPost=new HttpPost(Urls.getAccountDetail);
                Log.d("params====",""+params[0]+params[1]);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("User_id", login.id));
                nameValuePairs.add(new BasicNameValuePair("bio", params[0]));
                nameValuePairs.add(new BasicNameValuePair("mobile",params[1]));
                nameValuePairs.add(new BasicNameValuePair("emergencyMobile",params[2]));
                nameValuePairs.add(new BasicNameValuePair("driverID",params[3]));
                nameValuePairs.add(new BasicNameValuePair("city",params[4]));
                nameValuePairs.add(new BasicNameValuePair("firstName",params[5]));
                nameValuePairs.add(new BasicNameValuePair("lastName",params[6]));

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

//                JSONObject obj=new JSONObject(result);
                if(result.contains("Verification Successful.")){


                    pdLoading.dismiss();
                    pDialog.dismiss();
                }
                else{
                    pdLoading.dismiss();
                    pDialog.dismiss();


                    Toast.makeText(getContext(), "Unsuccessfull", Toast.LENGTH_LONG).show();
                }


            }catch (Exception e){
                pdLoading.dismiss();
                pDialog.dismiss();
                e.printStackTrace();
            }
        }
    }
}
