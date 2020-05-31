package com.example.boatpoolingapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.boatpoolingapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link bookedRide.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link bookedRide#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bookedRide extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String name;
    String departure_time;
    String dearture_city,booking_status;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView userNametv,timetv,cityTv,statusTv;
    public Date date = null;
    public SimpleDateFormat inputFormat,outputFormat;
    private OnFragmentInteractionListener mListener;

    public bookedRide() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment bookedRide.
     */
    // TODO: Rename and change types and number of parameters
    public static bookedRide newInstance(String param1, String param2) {
        bookedRide fragment = new bookedRide();
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

        name = getArguments().getString("name");
        dearture_city = getArguments().getString("dearture_city");
        booking_status = getArguments().getString("booking_status");

        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            date = inputFormat.parse(getArguments().getString("departure_time"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);

        departure_time =formattedDate;
        statusTv=view.findViewById(R.id.statusTv);
        userNametv=view.findViewById(R.id.userNametv);
        timetv=view.findViewById(R.id.timetv);
        cityTv=view.findViewById(R.id.cityTv);
        statusTv.setText(booking_status);
        userNametv.setText(name);
        timetv.setText(departure_time);

        cityTv.setText(dearture_city);


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booked_ride, container, false);
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
}
