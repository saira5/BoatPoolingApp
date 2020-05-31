package com.example.boatpoolingapp.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.SignUp;
import com.example.boatpoolingapp.Urls;
import com.example.boatpoolingapp.VerifyEmail;
import com.example.boatpoolingapp.login;

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
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfferRide.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfferRide#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfferRide extends Fragment {
    Button searchbtn;
    EditText etdate,timepicker;
    Spinner BookingSpinner,seatsNumberSpinner,citySpinner;
    String date,time,bookingType,cities;
    FrameLayout frameLayout;
    String a;
    final Calendar myCalendar = Calendar.getInstance();

    public static  String dateTime,dateTemp,timeTemp,destination,seats;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OfferRide() {
        // Required empty public constructor
    }


    public static OfferRide newInstance(String param1, String param2) {
        OfferRide fragment = new OfferRide();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        searchbtn =view.findViewById(R.id.searchbtn);
        frameLayout = view.findViewById(R.id.frameLayout);
        etdate = view.findViewById(R.id.etdate);
        seatsNumberSpinner = (Spinner)view.findViewById(R.id.seatsNumberSpinner);
        BookingSpinner = (Spinner) view.findViewById(R.id.BookingSpinner);
        timepicker= view.findViewById(R.id.timepicker);


        citySpinner=view.findViewById(R.id.citySpinner);
        List<String> cityList = new ArrayList<String>();
        cityList.add("Berlin");
        cityList.add("Hamburg");
        cityList.add("Bavaria");
        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(getContext(),R.layout.seat_spinner_item, cityList);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapterCity);


        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.seat_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatsNumberSpinner.setAdapter(adapter);

        List<String> listBooking = new ArrayList<String>();
        listBooking.add("instant");
        listBooking.add("approval ");
        ArrayAdapter<String> adapterBooking = new ArrayAdapter<String>(getContext(),R.layout.book_spinner_item, listBooking);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BookingSpinner.setAdapter(adapterBooking);




        timepicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepicker.setText( selectedHour + ":" + selectedMinute);
                        timeTemp= selectedHour + ":" + selectedMinute+":"+"00";
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s=monthOfYear+1;
                        if(s<10){
                            a = year+"-0"+s+"-"+dayOfMonth;

                        }
                        else{
                            a = year+"-"+s+"-"+dayOfMonth;

                        }
                        dateTemp=a;
                        etdate.setText(""+a);
                    }
                };
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                Time date = new Time();
             //   DatePickerDialog d = new DatePickerDialog(getContext(), dpd, date.year ,date.month, date.monthDay);
                DatePickerDialog d = new DatePickerDialog(getContext(), dpd, mYear ,mMonth,mDay);
                d.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                d.show();

            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seats=seatsNumberSpinner.getSelectedItem().toString();
                date=etdate.getText().toString();
                time=timepicker.getText().toString();
                bookingType=BookingSpinner.getSelectedItem().toString();
                cities=citySpinner.getSelectedItem().toString();


              if(validation()){
                    destination=citySpinner.getSelectedItem().toString();
                    dateTime=dateTemp+" "+timeTemp;

                    Log.d("dateTime",""+dateTime);
                    new offerRideCall().execute(cities,dateTime,bookingType,seats);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer_ride, container, false);
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


    public boolean validation(){
        seats=seatsNumberSpinner.getSelectedItem().toString();
        date=etdate.getText().toString();
        time=timepicker.getText().toString();
        bookingType=BookingSpinner.getSelectedItem().toString();
        cities=citySpinner.getSelectedItem().toString();

        boolean validate=true;

        if(date.isEmpty())
        {
            etdate.setError(getString(R.string.empty));
            validate= false;
        }
        if(time.isEmpty())
        {
            timepicker.setError(getString(R.string.empty));
            validate= false;
        }


        return validate;
    }

    private class offerRideCall extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog.setMessage(getString(R.string.getting_address));
//
//            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.offerRide);
                Log.d("params====",login.id+""+params[0]+params[1]+params[2]+params[3]+login.name);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("user_id", login.id));
                nameValuePairs.add(new BasicNameValuePair("dearture_city", params[0]));
                nameValuePairs.add(new BasicNameValuePair("departure_time",params[1]));
                nameValuePairs.add(new BasicNameValuePair("ride_type",params[2]));
                nameValuePairs.add(new BasicNameValuePair("seats",params[3]));
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
            // pdLoading.dismiss();


//            pDialog.dismiss();
            try{

                JSONObject obj=new JSONObject(result);
                if(obj.getString("error").contains("false")&&obj.getString("message").contains("Ride offered successfully.")){
                    Fragment fr = new CurrentRides();
                    frameLayout.removeAllViews();

                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction().addToBackStack("offerride");
                    Bundle args = new Bundle();
                    args.putString("dateTime", dateTime);
                    args.putString("destination", destination);
                    args.putString("seatsOffered", seats);

                    fr.setArguments(args);
                    transaction.add(R.id.frameLayout, fr).commit();
                }
                else{
                    Log.d("result===1","in else");

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
                Log.d("result===1","in exception");

                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }
    }
}
