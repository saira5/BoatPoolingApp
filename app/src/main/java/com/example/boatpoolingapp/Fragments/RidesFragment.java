package com.example.boatpoolingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.login;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RidesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RidesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RidesFragment extends Fragment  {
    private static RidesFragment ridesFragment;
Button findridebtn,offerRidebtn;
    ConstraintLayout frameLayout;
    private OnFragmentInteractionListener mListener;



    public static RidesFragment newInstance(){
        RidesFragment fragment = new RidesFragment();

        return fragment;
    }




    public RidesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rides, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        offerRidebtn=view.findViewById(R.id.offerRidebtn);
        findridebtn=view.findViewById(R.id.findridebtn);
        frameLayout = view.findViewById(R.id.frameLayout);
        findridebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nestedFragment = new FindARide();
                frameLayout.removeAllViews();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.frameLayout, nestedFragment).commit();


//                FindARide nextFrag= new FindARide();
//                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
//                transaction1.add(R.id.frameLayout, nextFrag);
//                transaction1.commit();

//                FindARide nextFrag= new FindARide();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.frameLayout, nextFrag, "findThisFragment")
//                        .addToBackStack(null)
//                        .commit();
            }
        });

        offerRidebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nestedFragment = new OfferRide();
                frameLayout.removeAllViews();

                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.frameLayout, nestedFragment).commit();

//                FindARide nextFrag= new FindARide();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.frameLayout, nextFrag, "findFragment")
//                        .addToBackStack(null)
//                        .commit();
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        super.onAttach(context);
        super.onAttach(context);
//        if (context instanceof ToolbarListener) {
//            ((ToolbarListener) context).setTitle("My Profile");
//        }
        setTitle("Rides");
    }
    public void setTitle(String title){
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(toolbar.getContext(),android.R.color.white));
        toolbar.setTitle(title);

        //  ActivityUtils.setTitle(toolbar,title,false);

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
