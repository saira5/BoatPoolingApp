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
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.boatpoolingapp.Urls;
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
 * {@link FindARide.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindARide#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindARide extends Fragment {

    Button searchbtn;
    FrameLayout frameLayout;
    EditText etdate,timepicker;
    Spinner citySpinner;
    String date,time,cities;
    String dateTime,dateTemp,timeTemp;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FindARide() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindARide.
     */
    // TODO: Rename and change types and number of parameters
    public static FindARide newInstance(String param1, String param2) {
        FindARide fragment = new FindARide();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_aride, container, false);
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchbtn =view.findViewById(R.id.searchbtn);
        frameLayout = view.findViewById(R.id.frameLayout);
        etdate = view.findViewById(R.id.etdate);

//        timepicker= view.findViewById(R.id.timepicker);


        citySpinner=view.findViewById(R.id.citySpinner);
        List<String> cityList = new ArrayList<String>();
        cityList.add("Berlin");
        cityList.add("Hamburg");
        cityList.add("Bavaria");
        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(getContext(),R.layout.seat_spinner_item, cityList);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapterCity);







//        timepicker.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        timepicker.setText( selectedHour + ":" + selectedMinute);
//                        timeTemp= selectedHour + ":" + selectedMinute+":"+"00";
//
//                    }
//                }, hour, minute, true);
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//
//            }
//        });


        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s=monthOfYear+1;
                        String a = year+"-"+s+"-"+dayOfMonth;
                        dateTemp=a;
                        etdate.setText(""+a);
                    }
                };
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
               // Time date = new Time();
                //   DatePickerDialog d = new DatePickerDialog(getContext(), dpd, date.year ,date.month, date.monthDay);
                DatePickerDialog d = new DatePickerDialog(getContext(), dpd, mYear ,mMonth,mDay);
                d.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//
//                Time date = new Time();
//                DatePickerDialog d = new DatePickerDialog(getContext(), dpd, date.year ,date.month, date.monthDay);
                d.show();

            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date=etdate.getText().toString();

                cities=citySpinner.getSelectedItem().toString();
              //  if(validation()){
                  //  dateTime=dateTemp+" "+timeTemp;
                  //  Log.d("dateTime",""+dateTime);


                    Fragment fr = new AvailableRides();
                    frameLayout.removeAllViews();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    Bundle args=new Bundle();
                    args.putString("date",dateTemp);
                    args.putString("cities",cities);
                    fr.setArguments(args);
                    transaction.add(R.id.frameLayout, fr).commit();



//                    new findRideCall().execute(cities,dateTime);
               // }
            }
        });
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
    public boolean validation(){

        date=etdate.getText().toString();
        time=timepicker.getText().toString();
        cities=citySpinner.getSelectedItem().toString();

        boolean validate=true;
//
//        if(date.isEmpty())
//        {
//            etdate.setError(getString(R.string.empty));
//            validate= false;
//        }
//        if(time.isEmpty())
//        {
//            timepicker.setError(getString(R.string.empty));
//            validate= false;
//        }
//

        return validate;
    }

}
