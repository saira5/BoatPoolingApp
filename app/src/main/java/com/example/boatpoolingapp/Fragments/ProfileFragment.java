package com.example.boatpoolingapp.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boatpoolingapp.Dashboard;
import com.example.boatpoolingapp.R;
import com.example.boatpoolingapp.Urls;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText editTextFirstName,editTextLastName,editTextPhoneNum,editTextEmail,editTextCity,editTextGender;
    TextView textUserName;
    Dialog dialog;
    ProgressDialog pDialog;
    Button buttonLogout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

      public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

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
        return inflater.inflate(R.layout.fragment_profile, container, false);


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
        editTextFirstName=view.findViewById(R.id.editTextFirstName);
        editTextLastName=view.findViewById(R.id.editTextLastName);
        editTextPhoneNum=view.findViewById(R.id.editTextPhoneNum);
        editTextEmail=view.findViewById(R.id.editTextEmail);
        editTextCity=view.findViewById(R.id.editTextCity);
        editTextGender=view.findViewById(R.id.editTextGender);
        textUserName=view.findViewById(R.id.textUserName);
        buttonLogout=view.findViewById(R.id.buttonLogout);
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(getContext(),login.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        new getProfile().execute(login.id);

    }

    private class getProfile extends AsyncTask<String, String, String>
    {
       // ProgressDialog pdLoading = new ProgressDialog();
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.getting_address));

            pDialog.show();
//            pDialog.setMessage(getString(R.string.getting_address));
//
//            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            pDialog.dismiss();
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.getProfile);
                Log.d("params====",""+params.toString());

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("id", params[0]));
              //  nameValuePairs.add(new BasicNameValuePair("password",params[1]));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse=httpClient.execute(httpPost);
                return_text=readResponse(httpResponse);
                Log.d("Profile RESPONSE1",""+return_text);

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
            Log.d("profile RESPONSE",""+result);
            // pdLoading.dismiss();
          //  pDialog.dismiss();
            try{
                JSONObject jsonObject=new JSONObject(result);
               // Log.d("RESULT", jsonObject.toString()+""+jsonObject.getString("message"));

//               if(jsonObject.getString("message").contains("Cannot find user with username: undefined")){
//                    Toast.makeText(getContext(), "Something went wrong in getting profile ", Toast.LENGTH_LONG).show();
//
//                }

                String fname=jsonObject.getString("firstName");
                String lname=jsonObject.getString("lastName");

                editTextFirstName.setText(jsonObject.getString("firstName"));
                editTextLastName.setText(jsonObject.getString("lastName"));
                editTextEmail.setText(jsonObject.getString("username"));
                editTextGender.setText(jsonObject.getString("gender"));
                textUserName.setText(fname+" "+lname);
                jsonObject.getString("dob");


              //  }

//                else {
//                    Toast.makeText(getContext(), "Server Error ", Toast.LENGTH_LONG).show();
//                }
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getContext(), "Server Error ", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        if (context instanceof ToolbarListener) {
//            ((ToolbarListener) context).setTitle("My Profile");
//        }
        setTitle("My Profile");
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
